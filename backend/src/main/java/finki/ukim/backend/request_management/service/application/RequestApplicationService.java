package finki.ukim.backend.request_management.service.application;

import finki.ukim.backend.auth_and_access.model.domain.User;
import finki.ukim.backend.request_management.model.dto.CreateRequestDto;
import finki.ukim.backend.request_management.model.dto.DisplayBasicRequestDto;
import finki.ukim.backend.request_management.model.dto.DisplayRequestDto;
import finki.ukim.backend.request_management.model.dto.DisplayRequestPageableDto;
import finki.ukim.backend.request_management.model.dto.filter.RequestFilterDto;
import org.springframework.data.domain.Page;

import java.util.List;

public interface RequestApplicationService {
    List<DisplayBasicRequestDto> findAll(User user);

    DisplayRequestDto findById(Long id, User user);

    DisplayRequestDto create(User user, CreateRequestDto createRequestDto);

    Page<DisplayRequestPageableDto> findAll(User user, RequestFilterDto requestFilterDto);

    DisplayRequestDto cancel(Long id, User user);

    void delete(Long id);

    void deleteBulk(List<Long> ids);


}
