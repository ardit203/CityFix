package finki.ukim.backend.administration.model.dto.filters;

import finki.ukim.backend.common.dto.FilterDto;
import finki.ukim.backend.common.helper.FilterUtils;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StaffFilterDto extends FilterDto {
    private Long id;
    private Long userId;
    private Long departmentId;
    private Long municipalityId;

    private String username;
    private String municipalityCode;
    private String municipalityName;

    public void normalizeTextFields() {
        this.username = FilterUtils.normalizeTextFilter(this.username);
        this.municipalityCode = FilterUtils.normalizeTextFilter(this.municipalityCode);
        this.municipalityName = FilterUtils.normalizeTextFilter(this.municipalityName);
    }
}