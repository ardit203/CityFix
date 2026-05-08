package finki.ukim.backend.common.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Getter
@Setter
public abstract class FilterDto {
    protected int page = 0;
    protected int size = 10;
    protected String sortBy = "id";
    protected String sortDir = "asc";

    public Pageable toPageable() {
        return PageRequest.of(
                page,
                size,
                Sort.by(Sort.Direction.fromString(sortDir), sortBy)
        );
    }

    public abstract void normalizeTextFields();
}
