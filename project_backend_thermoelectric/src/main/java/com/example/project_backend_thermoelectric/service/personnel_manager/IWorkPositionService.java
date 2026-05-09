package com.example.project_backend_thermoelectric.service.personnel_manager;

import com.example.project_backend_thermoelectric.entity.WorkPosition;

import java.util.List;

public interface IWorkPositionService {
    WorkPosition createWorkPosition(WorkPosition workPosition);
    List<WorkPosition> getAllWorkPositions();
    WorkPosition getWorkPositionById(Long id);
    WorkPosition updateWorkPosition(Long id, WorkPosition workPosition);
    void deleteWorkPosition(Long id);
}
