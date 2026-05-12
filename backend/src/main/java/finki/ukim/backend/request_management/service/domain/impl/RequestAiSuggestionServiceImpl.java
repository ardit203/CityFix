package finki.ukim.backend.request_management.service.domain.impl;

import finki.ukim.backend.administration.model.domain.Category;
import finki.ukim.backend.administration.service.domain.CategoryService;
import finki.ukim.backend.ai_integration.model.domain.AiSuggestion;
import finki.ukim.backend.ai_integration.model.enums.SuggestionStatus;
import finki.ukim.backend.ai_integration.model.exception.AiSuggestionNotFoundException;
import finki.ukim.backend.ai_integration.repository.AiSuggestionRepository;
import finki.ukim.backend.auth_and_access.model.domain.User;
import finki.ukim.backend.auth_and_access.service.domain.AccessScopeService;
import finki.ukim.backend.request_management.model.domain.Request;
import finki.ukim.backend.request_management.model.dto.ProcessAiSuggestionDto;
import finki.ukim.backend.request_management.model.dto.RejectAiSuggestionDto;
import finki.ukim.backend.request_management.model.enums.LogAction;
import finki.ukim.backend.request_management.model.exception.RequestNotFoundException;
import finki.ukim.backend.request_management.repository.RequestRepository;
import finki.ukim.backend.request_management.service.domain.RequestAiSuggestionService;
import finki.ukim.backend.request_management.service.domain.RequestLogService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class RequestAiSuggestionServiceImpl implements RequestAiSuggestionService {

    private final AiSuggestionRepository aiSuggestionRepository;
    private final AccessScopeService accessScopeService;
    private final RequestRepository requestRepository;
    private final CategoryService categoryService;
    private final RequestLogService requestLogService;

    @Override
    public AiSuggestion findSuggestion(Long requestId, User user) {
        Request request = findRequestById(requestId);
        accessScopeService.hasAccessToRequest(user, request);
        return findAiSuggestionById(requestId);
    }

    @Override
    @Transactional
    public void processSuggestion(Long requestId, User user, ProcessAiSuggestionDto dto) {
        Request request = findRequestById(requestId);
        accessScopeService.hasAccessToRequest(user, request);
        AiSuggestion suggestion = findAiSuggestionById(requestId);

        if (suggestion.getSuggestionStatus() != SuggestionStatus.PENDING_REVIEW) {
            throw new IllegalStateException("Suggestion is not in PENDING_REVIEW status");
        }

        boolean categoryDiffers = !suggestion.getCategory().getId().equals(dto.getCategoryId());
        boolean priorityDiffers = suggestion.getPriority() != dto.getPriority();

        // Mark as APPROVED or OVERRIDDEN based on differences
        if (categoryDiffers || priorityDiffers) {
            suggestion.setSuggestionStatus(SuggestionStatus.OVERRIDDEN);
        } else {
            suggestion.setSuggestionStatus(SuggestionStatus.APPROVED);
        }

        aiSuggestionRepository.save(suggestion);

        // Apply changes to Request
        Category newCategory = categoryService.findById(dto.getCategoryId());

        boolean actualCategoryChanged = request.getCategory() == null || !request.getCategory().getId().equals(newCategory.getId());
        boolean actualPriorityChanged = request.getPriority() != dto.getPriority();

        request.setCategory(newCategory);
        request.setPriority(dto.getPriority());
        if (dto.getSummary() != null && !dto.getSummary().isBlank()) {
            request.setSummary(dto.getSummary());
        }

        requestRepository.save(request);

        // Logs
        requestLogService.create(
                request,
                user,
                LogAction.AI_SUGGESTION_APPROVED,
                "Status: " + suggestion.getSuggestionStatus(),
                "Category: " + newCategory.getName() + ", Priority: " + dto.getPriority(),
                categoryDiffers || priorityDiffers ? "User overridden AI suggestion" : "User approved AI suggestion"
        );

        if (actualCategoryChanged) {
            requestLogService.create(
                    request,
                    user,
                    LogAction.CATEGORY_CHANGED,
                    request.getCategory() != null ? request.getCategory().getName() : "None",
                    newCategory.getName(),
                    "AI Suggestion Applied"
            );
        }

        if (actualPriorityChanged) {
            requestLogService.create(
                    request,
                    user,
                    LogAction.PRIORITY_CHANGED,
                    request.getPriority() != null ? request.getPriority().name() : "None",
                    dto.getPriority().name(),
                    "AI Suggestion Applied"
            );
        }
    }

    @Override
    @Transactional
    public void rejectSuggestion(Long requestId, User user, RejectAiSuggestionDto dto) {
        Request request = findRequestById(requestId);
        accessScopeService.hasAccessToRequest(user, request);
        AiSuggestion suggestion = findAiSuggestionById(requestId);

        if (suggestion.getSuggestionStatus() != SuggestionStatus.PENDING_REVIEW) {
            throw new IllegalStateException("Suggestion is not in PENDING_REVIEW status");
        }

        suggestion.setSuggestionStatus(SuggestionStatus.REJECTED);
        aiSuggestionRepository.save(suggestion);

        requestLogService.create(
                suggestion.getRequest(),
                user,
                LogAction.AI_SUGGESTION_REJECTED,
                "None",
                "None",
                "Reason: " + dto.getReason()
        );
    }

    private Request findRequestById(Long requestId) {
        return requestRepository.findById(requestId)
                .orElseThrow(() -> new RequestNotFoundException(requestId));
    }

    private AiSuggestion findAiSuggestionById(Long requestId) {
        return aiSuggestionRepository.findByRequest_Id(requestId)
                .orElseThrow(() -> new AiSuggestionNotFoundException(requestId));
    }
}
