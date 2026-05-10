package finki.ukim.backend.request_management.service.application;

import finki.ukim.backend.auth_and_access.model.domain.User;
import finki.ukim.backend.request_management.model.dto.*;
import finki.ukim.backend.request_management.model.dto.filter.RequestFilterDto;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface RequestApplicationService {
    List<DisplayBasicRequestDto> findAll(User user);

    DisplayRequestDto findById(Long id, User user);

    DisplayRequestDto create(User user, CreateRequestDto createRequestDto, List<MultipartFile> files);

    Page<DisplayRequestPageableDto> findAll(User user, RequestFilterDto requestFilterDto);

    DisplayRequestDto cancel(Long id, User user);

    DisplayRequestDto changeStatus(Long id, User user, ChangeRequestStatusDto changeRequestStatusDto);

    void delete(Long id);

    void deleteBulk(List<Long> ids);


}
