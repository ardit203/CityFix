package finki.ukim.backend.request_management.web.controller;

import finki.ukim.backend.auth_and_access.model.domain.User;
import finki.ukim.backend.request_management.model.dto.CreateRequestAssignmentDto;
import finki.ukim.backend.request_management.model.dto.DisplayRequestAssignmentBasicDto;
import finki.ukim.backend.request_management.model.dto.DisplayRequestAssignmentDto;
import finki.ukim.backend.request_management.model.dto.DisplayRequestAssignmentPageableDto;
import finki.ukim.backend.request_management.model.dto.filter.RequestAssignmentFilterDto;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/requests/{requestId}/assignments")
@AllArgsConstructor
public class RequestAssignmentController {
    @GetMapping
    public ResponseEntity<List<DisplayRequestAssignmentBasicDto>> findAllByRequest(
            @PathVariable Long requestId,
            @AuthenticationPrincipal User user
    ) {
        return ResponseEntity.ok().build();
    }


    @GetMapping("/paged")
    public ResponseEntity<Page<DisplayRequestAssignmentPageableDto>> findAll(
            @PathVariable Long requestId,
            @AuthenticationPrincipal User user,
            @Valid @ModelAttribute RequestAssignmentFilterDto requestAssignmentFilterDto
    ) {
        return ResponseEntity.ok().build();
    }

    @PostMapping
    public ResponseEntity<DisplayRequestAssignmentDto> assignEmployee(
            @PathVariable Long requestId,
            @AuthenticationPrincipal User user,
            @Valid @RequestBody CreateRequestAssignmentDto createRequestAssignmentDto
    ) {
        return ResponseEntity.ok().build();
    }

    @PostMapping("/bulk")
    public ResponseEntity<List<DisplayRequestAssignmentDto>> assignMultipleEmployees(
            @PathVariable Long requestId,
            @AuthenticationPrincipal User user,
            @Valid @RequestBody List<CreateRequestAssignmentDto> createRequestAssignmentDtos
    ) {
        return ResponseEntity.ok().build();
    }


    @DeleteMapping("/{assignmentId}")
    public ResponseEntity<Void> removeAssignment(
            @PathVariable Long requestId,
            @AuthenticationPrincipal User user,
            @PathVariable Long assignmentId
    ) {
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/bulk")
    public ResponseEntity<Void> removeMultipleAssignments(
            @PathVariable Long requestId,
            @AuthenticationPrincipal User user,
            List<Long> ids
    ) {
        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> removeAllAssignments(
            @PathVariable Long requestId,
            @AuthenticationPrincipal User user
    ) {
        return ResponseEntity.ok().build();
    }
}

//@PostMapping
/// / Assign one employee to a request
//
//@PostMapping("/bulk")
/// / Assign multiple employees to a request
//
//@PutMapping("/{assignmentId}")
/// / Update one assignment, usually change the assigned employee
//
//@DeleteMapping("/{assignmentId}")
/// / Remove one assignment
//
//@DeleteMapping("/bulk")
/// / Remove multiple assignments
//
//@DeleteMapping
/// / Remove all assignments from the request
//
//@GetMapping
/// / Get all assignments for one request
