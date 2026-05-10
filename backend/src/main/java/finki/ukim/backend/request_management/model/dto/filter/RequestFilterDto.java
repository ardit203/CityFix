package finki.ukim.backend.request_management.model.dto.filter;

import finki.ukim.backend.common.dto.FilterDto;
import finki.ukim.backend.common.helper.FilterUtils;
import finki.ukim.backend.request_management.model.enums.Priority;
import finki.ukim.backend.request_management.model.enums.RequestStatus;
import finki.ukim.backend.request_management.model.enums.RoutingStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class RequestFilterDto extends FilterDto {
    private Long id;
    private Long citizenId;
    private Long departmentId;
    private Long categoryId;
    private Long municipalityId;
    private Long assignedEmployeeUserId;
    private RequestStatus status;
    private RoutingStatus routingStatus;
    private Priority priority;
    private String text;
    private LocalDateTime submittedFrom;
    private LocalDateTime submittedTo;

    @Override
    public void normalizeTextFields() {
        this.text = FilterUtils.normalizeTextFilter(this.text);
    }
}
