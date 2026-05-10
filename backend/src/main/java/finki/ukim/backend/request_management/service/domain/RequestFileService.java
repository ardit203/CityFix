package finki.ukim.backend.request_management.service.domain;
import finki.ukim.backend.request_management.model.projection.RequestFileProjection;

import java.util.List;

public interface RequestFileService {
    RequestFileProjection findById(Long id);

    List<RequestFileProjection> findAllByRequestId(Long requestId);
}
