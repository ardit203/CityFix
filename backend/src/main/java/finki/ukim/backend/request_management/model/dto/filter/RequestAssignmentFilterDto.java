package finki.ukim.backend.request_management.model.dto.filter;

import finki.ukim.backend.common.dto.FilterDto;
import finki.ukim.backend.request_management.model.enums.RequestStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class RequestAssignmentFilterDto extends FilterDto {
    private Long id;

    private Long requestId;

    private Long employeeId;

    private Long assignedByUserId;

    private Long departmentId;

    private Long municipalityId;

    private RequestStatus requestStatus;

    private LocalDateTime assignedFrom;

    private LocalDateTime assignedTo;

    @Override
    public void normalizeTextFields() {

    }
}
