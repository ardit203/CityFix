package finki.ukim.backend.administration.service.application;

import finki.ukim.backend.administration.model.dto.CreateDepartmentDto;
import finki.ukim.backend.administration.model.dto.DisplayDepartmentDto;

import java.util.List;
import java.util.Optional;

public interface DepartmentApplicationService {
    List<DisplayDepartmentDto> findAll();

    Optional<DisplayDepartmentDto> findById(Long id);

    Optional<DisplayDepartmentDto> findByName(String name);


    DisplayDepartmentDto create(CreateDepartmentDto createDepartmentDto);

    Optional<DisplayDepartmentDto> update(Long id, CreateDepartmentDto createDepartmentDto);

    Optional<DisplayDepartmentDto> deleteById(Long id);
}
