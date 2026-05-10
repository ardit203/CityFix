package finki.ukim.backend.request_management.model.dto.filter;

import finki.ukim.backend.common.dto.FilterDto;
import finki.ukim.backend.common.helper.FilterUtils;
import finki.ukim.backend.request_management.model.enums.LogAction;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class RequestLogFilterDto extends FilterDto {

    private Long id;

    private Long actionByUserId;

    private LogAction action;

    private String text;

    private LocalDateTime createdFrom;

    private LocalDateTime createdTo;

    @Override
    public void normalizeTextFields() {
        this.text = FilterUtils.normalizeTextFilter(this.text);
    }
}