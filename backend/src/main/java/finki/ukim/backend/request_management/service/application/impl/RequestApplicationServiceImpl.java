package finki.ukim.backend.request_management.service.application.impl;

import finki.ukim.backend.administration.model.domain.Municipality;
import finki.ukim.backend.administration.service.domain.MunicipalityService;
import finki.ukim.backend.auth_and_access.model.domain.User;
import finki.ukim.backend.auth_and_access.service.domain.UserService;
import finki.ukim.backend.file_handling.helper.FileHelper;
import finki.ukim.backend.file_handling.model.exception.FileTypeNotAllowedException;
import finki.ukim.backend.request_management.model.domain.Request;
import finki.ukim.backend.request_management.model.dto.*;
import finki.ukim.backend.request_management.model.dto.filter.RequestFilterDto;
import finki.ukim.backend.request_management.service.application.RequestApplicationService;
import finki.ukim.backend.request_management.service.domain.RequestService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@AllArgsConstructor
public class RequestApplicationServiceImpl implements RequestApplicationService {
    private final RequestService requestService;
    private final MunicipalityService municipalityService;
    private final FileHelper fileHelper;
    private final UserService userService;

    @Override
    public List<DisplayBasicRequestDto> findAll(User user) {
        return DisplayBasicRequestDto.from(requestService.findAll(user));
    }

    @Override
    public DisplayRequestDto findById(Long id, User user) {
        return DisplayRequestDto.from(
                requestService.findById(id, user)
        );
    }

    @Override
    public DisplayRequestDto create(User user, CreateRequestDto createRequestDto, List<MultipartFile> files) {
        if (!fileHelper.areValidFileTypes(files)) {
            throw new FileTypeNotAllowedException("You have uploaded a file which are not acceptable");
        }
        Municipality municipality = municipalityService.findById(createRequestDto.municipalityId());
        return DisplayRequestDto.from(requestService.create(createRequestDto.toRequest(user, municipality), files));
    }

    @Override
    public Page<DisplayRequestPageableDto> findAll(User user, RequestFilterDto requestFilterDto) {
        return requestService.findAll(user, requestFilterDto)
                .map(DisplayRequestPageableDto::from);
    }

    @Override
    public DisplayRequestDto cancel(Long id, User user) {
        return DisplayRequestDto.from(requestService.cancel(id, user));
    }

    @Override
    public DisplayRequestDto changeStatus(Long id, User user, ChangeRequestStatusDto changeRequestStatusDto) {
        Request request = requestService.findById(id, user);
        return DisplayRequestDto.from(requestService.changeStatus(request,  user, changeRequestStatusDto));
    }

    @Override
    public void delete(Long id) {
        requestService.delete(id);
    }

    @Override
    public void deleteBulk(List<Long> ids) {
        requestService.deleteBulk(ids);
    }
}
