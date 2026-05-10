package finki.ukim.backend.request_management.service.domain.impl;

import finki.ukim.backend.request_management.model.exception.RequestFileNotFoundException;
import finki.ukim.backend.request_management.model.projection.RequestFileProjection;
import finki.ukim.backend.request_management.repository.RequestFileRepository;
import finki.ukim.backend.request_management.service.domain.RequestFileService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class RequestFileServiceImpl implements RequestFileService {
    private final RequestFileRepository requestFileRepository;

    @Override
    public RequestFileProjection findById(Long id) {
        return requestFileRepository.findProjectedById(id)
                .orElseThrow(() -> new RequestFileNotFoundException(id));
    }

    @Override
    public List<RequestFileProjection> findAllByRequestId(Long requestId) {
        return requestFileRepository.findAllProjectedByRequestId(requestId);
    }
}
