package finki.ukim.backend.administration.service.application;

import finki.ukim.backend.administration.model.dto.CreateDepartmentDto;
import finki.ukim.backend.administration.model.dto.DisplayDepartmentDto;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface DepartmentApplicationService {
    List<DisplayDepartmentDto> findAll();

    DisplayDepartmentDto findById(Long id);

    DisplayDepartmentDto findByName(String name);


    DisplayDepartmentDto create(CreateDepartmentDto createDepartmentDto);

    DisplayDepartmentDto update(Long id, CreateDepartmentDto createDepartmentDto);

    DisplayDepartmentDto deleteById(Long id);

    Page<DisplayDepartmentDto> findAll(int page, int size, String sortBy, Long id, String text);
}
