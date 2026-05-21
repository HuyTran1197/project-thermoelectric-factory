package com.example.project_backend_thermoelectric.service.personnel_manager;

import com.example.project_backend_thermoelectric.entity.Department;
import com.example.project_backend_thermoelectric.entity.Position;
import org.springframework.data.domain.Page;

import java.util.List;

public interface IPositionService {
    Position createPosition(Position position);
    List<Position> getAllPositions();
    Page<Position> searchPositions(String keyword, int page, int size);
    List<Position> searchPositions(String keyword);
    Position getPositionById(Long id);
    Position updatePosition(Long id, Position position);
    void deletePosition(Long id);
}
