package finki.ukim.backend.auth_and_access.model.dto.filter;

import finki.ukim.backend.auth_and_access.model.enums.Role;
import finki.ukim.backend.common.dto.FilterDto;
import finki.ukim.backend.common.helper.FilterUtils;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserFilterDto extends FilterDto {
    private Long id;
    private String username;
    private String email;
    private Role role;

    @Override
    public void normalizeTextFields() {
        this.username = FilterUtils.normalizeTextFilter(this.username);
        this.email = FilterUtils.normalizeTextFilter(this.email);
    }
}
