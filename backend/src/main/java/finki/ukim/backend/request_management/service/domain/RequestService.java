package finki.ukim.backend.request_management.service.domain;

import finki.ukim.backend.auth_and_access.model.domain.User;
import finki.ukim.backend.request_management.model.domain.Request;
import finki.ukim.backend.request_management.model.dto.filter.RequestFilterDto;
import finki.ukim.backend.request_management.model.projection.RequestPageableProjection;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface RequestService {
    List<Request> findAll(User user);

    Request findById(Long id, User user);

    Request create(Request request, List<MultipartFile> files);

    Page<RequestPageableProjection> findAll(User user, RequestFilterDto requestFilterDto);

    Request cancel(Long id, User user);

    void delete(Long id);

    void deleteBulk(List<Long> ids);
}
