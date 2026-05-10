package finki.ukim.backend.request_management.service.application;

import finki.ukim.backend.auth_and_access.model.domain.User;
import finki.ukim.backend.request_management.model.dto.DisplayRequestDto;
import finki.ukim.backend.request_management.model.dto.RejectRoutingDto;
import finki.ukim.backend.request_management.model.dto.UpdateRequestRoutingDto;

public interface RequestRoutingApplicationService {

    DisplayRequestDto updateRouting(Long requestId, User currentUser, UpdateRequestRoutingDto dto);

    DisplayRequestDto confirmRouting(Long requestId, User currentUser);

    DisplayRequestDto rejectRouting(Long requestId, User currentUser, RejectRoutingDto dto);

    DisplayRequestDto reopenRouting(Long requestId, User currentUser);
}
