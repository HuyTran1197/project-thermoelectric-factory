package com.example.project_backend_thermoelectric.service.personnel_manager;

import com.example.project_backend_thermoelectric.dto.personnel_manager.EmployeeWorkPositionDto;
import com.example.project_backend_thermoelectric.entity.Employee;
import com.example.project_backend_thermoelectric.entity.EmployeeWorkPosition;
import com.example.project_backend_thermoelectric.entity.WorkPosition;
import com.example.project_backend_thermoelectric.repository.personnel_manager.IEmployeeRepo;
import com.example.project_backend_thermoelectric.repository.personnel_manager.IEmployeeWorkPositionRepo;
import com.example.project_backend_thermoelectric.repository.personnel_manager.IWorkPositionRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EmployeeWorkPositionService implements IEmployeeWorkPositionService{
    @Autowired private IEmployeeWorkPositionRepo empWorkPosRepo;
    @Autowired private IEmployeeRepo empRepo;
    @Autowired private IWorkPositionRepo workPosRepo;

    @Override
    public List<WorkPosition> getPositionsByEmployee(Long employeeId) {
        List<EmployeeWorkPosition> list = empWorkPosRepo.findByEmployeeId(employeeId);
        return list.stream().map(EmployeeWorkPosition::getWorkPosition).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void assignPosition(Long employeeId, Long positionId) {
        if (empWorkPosRepo.existsByEmployeeIdAndWorkPositionId(employeeId, positionId)) return;
        Employee emp = empRepo.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Nhân viên không tồn tại"));
        WorkPosition pos = workPosRepo.findById(positionId)
                .orElseThrow(() -> new RuntimeException("WorkPosition không tồn tại"));
        EmployeeWorkPosition ew = new EmployeeWorkPosition();
        ew.setEmployee(emp);
        ew.setWorkPosition(pos);
        empWorkPosRepo.save(ew);
    }

    @Override
    @Transactional
    public void removePosition(Long employeeId, Long positionId) {
        if (!empWorkPosRepo.existsByEmployeeIdAndWorkPositionId(employeeId, positionId)) return;
        empWorkPosRepo.deleteByEmployeeIdAndWorkPositionId(employeeId, positionId);
    }
}
