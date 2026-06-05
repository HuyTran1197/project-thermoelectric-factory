package com.example.project_backend_thermoelectric.service.personnel_manager;

import com.example.project_backend_thermoelectric.dto.personnel_manager.WorkPositionDto;
import org.springframework.data.domain.Page;

import java.util.List;

public interface IWorkPositionService {
    Page<WorkPositionDto> search(String keyword, int page, int size);
    List<WorkPositionDto> getAll();
    WorkPositionDto create(String name);
    WorkPositionDto update(Long id, String name);
    void delete(Long id);
}
