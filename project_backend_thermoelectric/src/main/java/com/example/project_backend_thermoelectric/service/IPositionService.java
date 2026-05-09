package com.example.project_backend_thermoelectric.service;

import com.example.project_backend_thermoelectric.entity.Position;

import java.util.List;

public interface IPositionService {
    Position createPosition(Position position);
    List<Position> getAllPositions();
    Position getPositionById(Long id);
    Position updatePosition(Long id, Position position);
    void deletePosition(Long id);
}
