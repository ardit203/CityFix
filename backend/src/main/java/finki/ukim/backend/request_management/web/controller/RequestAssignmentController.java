package finki.ukim.backend.request_management.web.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/requests/{requestId}/assignments")
@AllArgsConstructor
public class RequestAssignmentController {
//
//    @GetMapping
//    public List<DisplayRequestAssignmentDto> findAllByRequest(
//            @PathVariable Long requestId
//    ) {
//        return requestAssignmentApplicationService.findAllByRequest(requestId);
//    }
//
//    @PostMapping
//    public DisplayRequestAssignmentDto assignEmployee(
//            @PathVariable Long requestId,
//            @RequestBody AssignEmployeeDto dto
//    ) {
//        return requestAssignmentApplicationService.assignEmployee(requestId, dto);
//    }
//
//    @PostMapping("/bulk")
//    public List<DisplayRequestAssignmentDto> assignMultipleEmployees(
//            @PathVariable Long requestId,
//            @RequestBody AssignMultipleEmployeesDto dto
//    ) {
//        return requestAssignmentApplicationService.assignMultipleEmployees(requestId, dto);
//    }
//
//    @PutMapping("/{assignmentId}")
//    public DisplayRequestAssignmentDto updateAssignment(
//            @PathVariable Long requestId,
//            @PathVariable Long assignmentId,
//            @RequestBody UpdateRequestAssignmentDto dto
//    ) {
//        return requestAssignmentApplicationService.updateAssignment(requestId, assignmentId, dto);
//    }
//
//    @DeleteMapping("/{assignmentId}")
//    public DisplayRequestAssignmentDto removeAssignment(
//            @PathVariable Long requestId,
//            @PathVariable Long assignmentId
//    ) {
//        return requestAssignmentApplicationService.removeAssignment(requestId, assignmentId);
//    }
//
//    @DeleteMapping("/bulk")
//    public List<DisplayRequestAssignmentDto> removeMultipleAssignments(
//            @PathVariable Long requestId,
//            @RequestBody RemoveMultipleAssignmentsDto dto
//    ) {
//        return requestAssignmentApplicationService.removeMultipleAssignments(requestId, dto);
//    }
//
//    @DeleteMapping
//    public void removeAllAssignments(
//            @PathVariable Long requestId
//    ) {
//        requestAssignmentApplicationService.removeAllAssignments(requestId);
//    }
}

//@PostMapping
//// Assign one employee to a request
//
//@PostMapping("/bulk")
//// Assign multiple employees to a request
//
//@PutMapping("/{assignmentId}")
//// Update one assignment, usually change the assigned employee
//
//@DeleteMapping("/{assignmentId}")
//// Remove one assignment
//
//@DeleteMapping("/bulk")
//// Remove multiple assignments
//
//@DeleteMapping
//// Remove all assignments from the request
//
//@GetMapping
//// Get all assignments for one request
