package finki.ukim.backend.request_management.service.application.impl;

import finki.ukim.backend.auth_and_access.model.domain.User;
import finki.ukim.backend.auth_and_access.service.domain.UserService;
import finki.ukim.backend.request_management.model.domain.Request;
import finki.ukim.backend.request_management.model.domain.RequestAssignment;
import finki.ukim.backend.request_management.model.dto.CreateRequestAssignmentDto;
import finki.ukim.backend.request_management.model.dto.DisplayRequestAssignmentBasicDto;
import finki.ukim.backend.request_management.model.dto.DisplayRequestAssignmentDto;
import finki.ukim.backend.request_management.model.dto.DisplayRequestAssignmentPageableDto;
import finki.ukim.backend.request_management.model.dto.filter.RequestAssignmentFilterDto;
import finki.ukim.backend.request_management.service.application.RequestAssignmentApplicationService;
import finki.ukim.backend.request_management.service.domain.RequestAssignmentService;
import finki.ukim.backend.request_management.service.domain.RequestService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class RequestAssignmentApplicationServiceImpl implements RequestAssignmentApplicationService {
    private final RequestAssignmentService requestAssignmentService;
    private final RequestService requestService;
    private final UserService userService;

    @Override
    public DisplayRequestAssignmentDto findById(Long assignmentId, User user) {
        return DisplayRequestAssignmentDto.from(
                requestAssignmentService.findById(assignmentId, user)
        );
    }

    @Override
    public List<DisplayRequestAssignmentBasicDto> findAllByRequest(Long requestId, User user) {
        Request request = requestService.findById(requestId, user);
        return DisplayRequestAssignmentBasicDto.from(
                requestAssignmentService.findAllByRequest(request, user)
        );
    }

    @Override
    public Page<DisplayRequestAssignmentPageableDto> findAll(Long requestId, User user, RequestAssignmentFilterDto requestAssignmentFilterDto) {
        Request request = requestService.findById(requestId, user);

        return requestAssignmentService
                .findAll(request, user, requestAssignmentFilterDto)
                .map(DisplayRequestAssignmentPageableDto::from);
    }

    @Override
    public Page<DisplayRequestAssignmentPageableDto> findAll(User user, RequestAssignmentFilterDto requestAssignmentFilterDto) {
        return requestAssignmentService
                .findAll(user, requestAssignmentFilterDto)
                .map(DisplayRequestAssignmentPageableDto::from);
    }

    @Override
    public DisplayRequestAssignmentDto assignEmployee(Long requestId, User user, CreateRequestAssignmentDto createRequestAssignmentDto) {
        Request request = requestService.findById(requestId, user);
        User employee = userService.findById(createRequestAssignmentDto.employeeId());
        return DisplayRequestAssignmentDto.from(requestAssignmentService.assignEmployee(
                request,
                user,
                createRequestAssignmentDto.toRequestAssignment(request, employee, user)
        ));
    }

//    @Override
//    public List<DisplayRequestAssignmentDto> assignMultipleEmployees(Long requestId, User user, List<CreateRequestAssignmentDto> createRequestAssignmentDtos) {
//        return List.of();
//    }

    @Override
    public void removeAssignment(Long requestId, User user, Long assignmentId) {
        RequestAssignment assignment = requestAssignmentService.findById(assignmentId);
        Request request = requestService.findById(requestId, user);
        requestAssignmentService.removeAssignment(request, user, assignment);
    }

    @Override
    public void removeMultipleAssignments(Long requestId, User user, List<Long> ids) {
        Request request = requestService.findById(requestId, user);
        requestAssignmentService.removeMultipleAssignments(request, user, ids);
    }

    @Override
    public void removeAllAssignments(Long requestId, User user) {
        Request request = requestService.findById(requestId, user);
        requestAssignmentService.removeAllAssignments(request, user);
    }
}
