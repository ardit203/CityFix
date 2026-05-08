package finki.ukim.backend.administration.model.dto.filters;

import finki.ukim.backend.common.dto.FilterDto;
import finki.ukim.backend.common.helper.FilterUtils;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MunicipalityFilterDto extends FilterDto {
    private Long id;
    private String code;
    private String name;

    @Override
    public void normalizeTextFields() {
        this.code = FilterUtils.normalizeTextFilter(this.code);
        this.name = FilterUtils.normalizeTextFilter(this.name);
    }
}
