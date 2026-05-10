package finki.ukim.backend.request_management.model.dto.filter;

import finki.ukim.backend.common.dto.FilterDto;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class RequestAssignmentFilterDto extends FilterDto {
    private Long id;

    private Long employeeId;

    private Long assignedByUserId;

    private LocalDateTime assignedFrom;

    private LocalDateTime assignedTo;

    @Override
    public void normalizeTextFields() {

    }
}
