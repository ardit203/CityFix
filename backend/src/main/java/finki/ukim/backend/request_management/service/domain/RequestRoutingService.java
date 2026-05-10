package finki.ukim.backend.request_management.service.domain;

import finki.ukim.backend.auth_and_access.model.domain.User;
import finki.ukim.backend.request_management.model.domain.Request;
import finki.ukim.backend.request_management.model.dto.RejectRoutingDto;
import finki.ukim.backend.request_management.model.dto.UpdateRequestRoutingDto;

public interface RequestRoutingService {

    Request updateRouting(Long requestId, User currentUser, UpdateRequestRoutingDto dto);

    Request confirmRouting(Long requestId, User currentUser);

    Request rejectRouting(Long requestId, User currentUser, RejectRoutingDto dto);

    Request reopenRouting(Long requestId, User currentUser);
}
