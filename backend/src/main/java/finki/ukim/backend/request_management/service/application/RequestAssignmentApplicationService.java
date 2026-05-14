package finki.ukim.backend.request_management.service.application;


import finki.ukim.backend.auth_and_access.model.domain.User;
import finki.ukim.backend.request_management.model.dto.CreateRequestAssignmentDto;
import finki.ukim.backend.request_management.model.dto.DisplayRequestAssignmentBasicDto;
import finki.ukim.backend.request_management.model.dto.DisplayRequestAssignmentDto;
import finki.ukim.backend.request_management.model.dto.DisplayRequestAssignmentPageableDto;
import finki.ukim.backend.request_management.model.dto.filter.RequestAssignmentFilterDto;
import org.springframework.data.domain.Page;
import java.util.List;

public interface RequestAssignmentApplicationService {
    DisplayRequestAssignmentDto findById(
            Long assignmentId,
            User user
    );

    List<DisplayRequestAssignmentBasicDto> findAllByRequest(
            Long requestId,
            User user
    );

    Page<DisplayRequestAssignmentPageableDto> findAll(
            Long requestId,
            User user,
            RequestAssignmentFilterDto requestAssignmentFilterDto
    );

    Page<DisplayRequestAssignmentPageableDto> findAll(
            User user,
            RequestAssignmentFilterDto requestAssignmentFilterDto
    );

    DisplayRequestAssignmentDto assignEmployee(
            Long requestId,
            User user,
            CreateRequestAssignmentDto createRequestAssignmentDto
    );

//    List<DisplayRequestAssignmentDto> assignMultipleEmployees(
//            Long requestId,
//            User user,
//            List<CreateRequestAssignmentDto> createRequestAssignmentDtos
//    );

    void removeAssignment(
            Long requestId,
            User user,
            Long assignmentId
    );

    void removeMultipleAssignments(
            Long requestId,
            User user,
            List<Long> ids
    );

    void removeAllAssignments(
            Long requestId,
            User user
    );
}
