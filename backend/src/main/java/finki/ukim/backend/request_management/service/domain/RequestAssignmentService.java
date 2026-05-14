package finki.ukim.backend.request_management.service.domain;

import finki.ukim.backend.auth_and_access.model.domain.User;
import finki.ukim.backend.request_management.model.domain.Request;
import finki.ukim.backend.request_management.model.domain.RequestAssignment;
import finki.ukim.backend.request_management.model.dto.filter.RequestAssignmentFilterDto;
import finki.ukim.backend.request_management.model.projection.RequestAssignmentPageableProjection;
import org.springframework.data.domain.Page;

import java.util.List;

public interface RequestAssignmentService {
    RequestAssignment findById(Long id);

    RequestAssignment findById(Long id, User user);

    List<RequestAssignment> findAllByRequest(
            Request request,
            User user
    );

    Page<RequestAssignmentPageableProjection> findAll(
            Request request,
            User user,
            RequestAssignmentFilterDto requestAssignmentFilterDto
    );

    Page<RequestAssignmentPageableProjection> findAll(
            User user,
            RequestAssignmentFilterDto requestAssignmentFilterDto
    );

    RequestAssignment assignEmployee(
            Request request,
            User user,
            RequestAssignment assignment
    );

//    List<RequestAssignment> assignMultipleEmployees(
//            Request request,
//            User user,
//            List<RequestAssignment> assignments
//    );

    void removeAssignment(
            Request request,
            User user,
            RequestAssignment assignment
    );

    void removeMultipleAssignments(
            Request request,
            User user,
            List<Long> ids
    );

    void removeAllAssignments(
            Request request,
            User user
    );
}
