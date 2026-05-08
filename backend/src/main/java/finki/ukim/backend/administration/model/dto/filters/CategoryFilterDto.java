package finki.ukim.backend.administration.model.dto.filters;

import finki.ukim.backend.common.dto.FilterDto;
import finki.ukim.backend.common.helper.FilterUtils;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoryFilterDto extends FilterDto {
    private Long id;
    private String text;
    private Long departmentId;

    public void normalizeTextFields() {
        this.text = FilterUtils.normalizeTextFilter(this.text);
    }
}
