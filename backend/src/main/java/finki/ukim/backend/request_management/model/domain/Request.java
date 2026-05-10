package finki.ukim.backend.request_management.model.domain;

import finki.ukim.backend.administration.model.domain.Category;
import finki.ukim.backend.administration.model.domain.Department;
import finki.ukim.backend.administration.model.domain.Municipality;
import finki.ukim.backend.auth_and_access.model.domain.User;
import finki.ukim.backend.common.model.BaseAuditableEntity;
import finki.ukim.backend.file_handling.model.domain.File;
import finki.ukim.backend.request_management.model.enums.Priority;
import finki.ukim.backend.request_management.model.enums.RequestStatus;
import finki.ukim.backend.request_management.model.enums.RoutingStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "requests")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Request extends BaseAuditableEntity {

    private String title;

    @Column(length = 2000)
    private String description;

    @Embedded
    private RequestLocation location;

    @Column(length = 2000)
    private String summary;

    @Enumerated(EnumType.STRING)
    private Priority priority;

    @Enumerated(EnumType.STRING)
    private RequestStatus status;

    @Enumerated(EnumType.STRING)
    private RoutingStatus routingStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    private Municipality municipality;

    @ManyToOne(fetch = FetchType.LAZY)
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    private Department department;

    @OneToMany(
            mappedBy = "request",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<RequestFile> files = new ArrayList<>();

    public void addFile(File file) {
        RequestFile requestFile = new RequestFile(this, file);
        this.files.add(requestFile);
    }

    public void addFiles(List<File> files) {
        files.forEach(this::addFile);
    }

    public Request(String title, String description, User user, Municipality municipality, RequestLocation requestLocation) {
        this.title = title;
        this.description = description;
        this.user = user;
        this.municipality = municipality;
        this.location = requestLocation;
    }
}