package finki.ukim.backend.administration.model.domain;


import finki.ukim.backend.common.model.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "departments")
public class Department extends BaseEntity {
    @Column(unique = true, nullable = false)
    private String name;
    private String description;
}
