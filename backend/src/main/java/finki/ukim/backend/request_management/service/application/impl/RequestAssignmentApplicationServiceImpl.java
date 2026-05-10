package finki.ukim.backend.request_management.service.application.impl;

import finki.ukim.backend.auth_and_access.model.domain.User;
import finki.ukim.backend.request_management.model.dto.CreateRequestAssignmentDto;
import finki.ukim.backend.request_management.model.dto.DisplayRequestAssignmentBasicDto;
import finki.ukim.backend.request_management.model.dto.DisplayRequestAssignmentDto;
import finki.ukim.backend.request_management.model.dto.DisplayRequestAssignmentPageableDto;
import finki.ukim.backend.request_management.model.dto.filter.RequestAssignmentFilterDto;
import finki.ukim.backend.request_management.service.application.RequestAssignmentApplicationService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class RequestAssignmentApplicationServiceImpl implements RequestAssignmentApplicationService {

    @Override
    public List<DisplayRequestAssignmentBasicDto> findAllByRequest(Long requestId, User user) {
        return List.of();
    }

    @Override
    public Page<DisplayRequestAssignmentPageableDto> findAll(Long requestId, User user, RequestAssignmentFilterDto requestAssignmentFilterDto) {
        return null;
    }

    @Override
    public DisplayRequestAssignmentDto assignEmployee(Long requestId, User user, CreateRequestAssignmentDto createRequestAssignmentDto) {
        return null;
    }

    @Override
    public List<DisplayRequestAssignmentDto> assignMultipleEmployees(Long requestId, User user, List<CreateRequestAssignmentDto> createRequestAssignmentDtos) {
        return List.of();
    }

    @Override
    public void removeAssignment(Long requestId, User user, Long assignmentId) {

    }

    @Override
    public void removeMultipleAssignments(Long requestId, User user, List<Long> ids) {

    }

    @Override
    public void removeAllAssignments(Long requestId, User user) {

    }
}
