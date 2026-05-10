package finki.ukim.backend.request_management.service.application.impl;

import finki.ukim.backend.auth_and_access.model.domain.User;
import finki.ukim.backend.request_management.model.domain.Request;
import finki.ukim.backend.request_management.model.dto.DisplayRequestDto;
import finki.ukim.backend.request_management.model.dto.RejectRoutingDto;
import finki.ukim.backend.request_management.model.dto.UpdateRequestRoutingDto;
import finki.ukim.backend.request_management.service.application.RequestRoutingApplicationService;
import finki.ukim.backend.request_management.service.domain.RequestRoutingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RequestRoutingApplicationServiceImpl implements RequestRoutingApplicationService {

    private final RequestRoutingService requestRoutingService;

    @Override
    public DisplayRequestDto updateRouting(Long requestId, User currentUser, UpdateRequestRoutingDto dto) {
        Request request = requestRoutingService.updateRouting(requestId, currentUser, dto);
        return DisplayRequestDto.from(request);
    }

    @Override
    public DisplayRequestDto confirmRouting(Long requestId, User currentUser) {
        Request request = requestRoutingService.confirmRouting(requestId, currentUser);
        return DisplayRequestDto.from(request);
    }

    @Override
    public DisplayRequestDto rejectRouting(Long requestId, User currentUser, RejectRoutingDto dto) {
        Request request = requestRoutingService.rejectRouting(requestId, currentUser, dto);
        return DisplayRequestDto.from(request);
    }

    @Override
    public DisplayRequestDto reopenRouting(Long requestId, User currentUser) {
        Request request = requestRoutingService.reopenRouting(requestId, currentUser);
        return DisplayRequestDto.from(request);
    }
}
