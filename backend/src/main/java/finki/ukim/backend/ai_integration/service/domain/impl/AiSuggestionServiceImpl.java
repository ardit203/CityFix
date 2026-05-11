package finki.ukim.backend.ai_integration.service.domain.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import finki.ukim.backend.administration.model.domain.Category;
import finki.ukim.backend.administration.service.domain.CategoryService;
import finki.ukim.backend.ai_integration.model.domain.AiSuggestion;
import finki.ukim.backend.ai_integration.model.dto.AiFileContext;
import finki.ukim.backend.ai_integration.model.dto.AiSuggestionRequest;
import finki.ukim.backend.ai_integration.model.enums.SuggestionStatus;
import finki.ukim.backend.ai_integration.repository.AiSuggestionRepository;
import finki.ukim.backend.ai_integration.service.domain.AiSuggestionService;
import finki.ukim.backend.ai_integration.service.port.LlmClientPort;
import finki.ukim.backend.file_handling.model.enums.FileType;
import finki.ukim.backend.file_handling.service.domain.FileStorageService;
import finki.ukim.backend.request_management.model.domain.Request;
import finki.ukim.backend.request_management.model.domain.RequestFile;
import finki.ukim.backend.request_management.model.enums.LogAction;
import finki.ukim.backend.request_management.model.enums.Priority;
import finki.ukim.backend.request_management.repository.RequestRepository;
import finki.ukim.backend.request_management.service.domain.RequestLogService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class AiSuggestionServiceImpl implements AiSuggestionService {
    // Only image types that Gemini supports natively via inlineData
    private static final Set<FileType> SUPPORTED_IMAGE_TYPES = Set.of(FileType.PNG, FileType.JPEG);

    private final LlmClientPort llmClient;
    private final CategoryService categoryService;
    private final RequestRepository requestRepository;
    private final AiSuggestionRepository aiSuggestionRepository;
    private final RequestLogService requestLogService;
    private final FileStorageService fileStorageService;
    private final ObjectMapper objectMapper;

    @Async
    @Transactional
    public void suggest(Request request) {
        try {
            log.info("Starting async AI suggestion generation for Request ID: {}", request.getId());

            // 2. Fetch available categories
            List<Category> categories = categoryService.findAll();
            if (categories.isEmpty()) {
                log.warn("No categories exist in the system. Skipping AI suggestion for Request ID: {}", request.getId());
                return;
            }

            String categoryNames = categories.stream()
                    .map(Category::getName)
                    .collect(Collectors.joining(", "));

            // 3. Build the image file contexts from the request's attached image files
            List<AiFileContext> imageContexts = buildImageContexts(request);
            if (!imageContexts.isEmpty()) {
                log.info("Providing {} image(s) to AI for Request ID: {}", imageContexts.size(), request.getId());
            }

            // 4. Construct the prompt
            String systemPrompt = String.format("""
                You are an intelligent assistant for a city maintenance application called CityFix.
                Your task is to analyze an issue reported by a citizen and provide:
                1. A classification into exactly ONE of the following existing categories: [%s]
                2. A priority level (LOW, MEDIUM, HIGH)
                3. A short summary of the issue (max 3 sentences)
                
                If images are provided, use them as additional visual context to improve your analysis.
                
                You must return your response in JSON format exactly like this:
                {
                   "category_name": "The chosen category name",
                   "priority": "HIGH",
                   "ai_summary": "Summary text here"
                }
                
                If you are unsure or none of the categories fit perfectly, choose the one that fits best.
                """, categoryNames);

            String userMessage = String.format("Title: %s\nDescription: %s",
                    request.getTitle(),
                    request.getDescription() != null ? request.getDescription() : "N/A");

            // 5. Call the LLM (with optional image contexts)
            String rawJsonResponse = llmClient.getCompletion(systemPrompt, userMessage, imageContexts);

            // 6. Parse the response
            JsonNode rootNode = objectMapper.readTree(rawJsonResponse);
            if (!rootNode.has("category_name") || !rootNode.has("priority") || !rootNode.has("ai_summary")) {
                log.error("AI failed to return the expected JSON format for Request ID: {}", request.getId());
                return;
            }

            String suggestedCategoryName = rootNode.get("category_name").asText();
            String suggestedPriorityStr = rootNode.get("priority").asText();
            String aiSummary = rootNode.get("ai_summary").asText();

            // Match Category (fallback to first if no exact match)
            Category matchedCategory = categories.stream()
                    .filter(c -> c.getName().equalsIgnoreCase(suggestedCategoryName))
                    .findFirst()
                    .orElse(categories.get(0));

            // Match Priority (fallback to MEDIUM)
            Priority matchedPriority;
            try {
                matchedPriority = Priority.valueOf(suggestedPriorityStr.toUpperCase());
            } catch (Exception e) {
                matchedPriority = Priority.MEDIUM;
            }

            // 7. Save AiSuggestion
            AiSuggestion suggestion = new AiSuggestion();
            suggestion.setRequest(request);
            suggestion.setCategory(matchedCategory);
            suggestion.setPriority(matchedPriority);
            suggestion.setAiSummary(aiSummary);
            suggestion.setSuggestionStatus(SuggestionStatus.PENDING_REVIEW);
            aiSuggestionRepository.save(suggestion);

            // 8. Log Action
            requestLogService.create(
                    request,
                    request.getUser(),
                    LogAction.AI_SUGGESTION_CREATED,
                    "None",
                    "Category: " + matchedCategory.getName() + ", Priority: " + matchedPriority.name(),
                    "AI generated a suggestion asynchronously"
            );

            log.info("Successfully saved AI suggestion for Request ID: {}", request.getId());

        } catch (Exception e) {
            log.error("Async AI suggestion failed for Request ID: {} — {}", request.getId(), e.getMessage(), e);
        }
    }

    /**
     * Reads all image files attached to the given request and encodes them as Base64 for the AI.
     * Non-image files (PDFs, CSVs, etc.) are silently skipped.
     */
    private List<AiFileContext> buildImageContexts(Request request) {
        List<AiFileContext> contexts = new ArrayList<>();

        if (request.getFiles() == null || request.getFiles().isEmpty()) {
            return contexts;
        }

        for (RequestFile requestFile : request.getFiles()) {
            var file = requestFile.getFile();

            if (!SUPPORTED_IMAGE_TYPES.contains(file.getFileType())) {
                log.debug("Skipping non-image file '{}' (type: {})", file.getFileName(), file.getFileType());
                continue;
            }

            try {
                Path filePath = fileStorageService.load(file.getFileUrl());
                byte[] bytes = Files.readAllBytes(filePath);
                String base64 = Base64.getEncoder().encodeToString(bytes);
                String mimeType = toMimeType(file.getFileType());
                contexts.add(new AiFileContext(mimeType, base64));
            } catch (Exception e) {
                log.warn("Failed to read image file '{}' for AI context: {}", file.getFileName(), e.getMessage());
            }
        }

        return contexts;
    }

    private String toMimeType(FileType fileType) {
        return switch (fileType) {
            case PNG -> "image/png";
            case JPEG -> "image/jpeg";
            default -> "application/octet-stream";
        };
    }
}
