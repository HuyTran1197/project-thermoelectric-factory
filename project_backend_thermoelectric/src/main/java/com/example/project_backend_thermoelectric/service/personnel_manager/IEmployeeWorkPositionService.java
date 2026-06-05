package com.example.project_backend_thermoelectric.service.personnel_manager;

import com.example.project_backend_thermoelectric.dto.personnel_manager.EmployeeWorkPositionDto;
import com.example.project_backend_thermoelectric.entity.WorkPosition;
import org.springframework.data.domain.Page;

import java.util.List;

public interface IEmployeeWorkPositionService {
    List<WorkPosition> getPositionsByEmployee(Long employeeId);
    void assignPosition(Long employeeId, Long positionId);
    void removePosition(Long employeeId, Long positionId);
}
