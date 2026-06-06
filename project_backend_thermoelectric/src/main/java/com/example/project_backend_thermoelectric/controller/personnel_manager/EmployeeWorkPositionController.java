package com.example.project_backend_thermoelectric.controller.personnel_manager;

import com.example.project_backend_thermoelectric.dto.personnel_manager.EmployeeWorkPositionDto;
import com.example.project_backend_thermoelectric.entity.Employee;
import com.example.project_backend_thermoelectric.entity.WorkPosition;
import com.example.project_backend_thermoelectric.repository.personnel_manager.IEmployeeRepo;
import com.example.project_backend_thermoelectric.service.personnel_manager.IEmployeeWorkPositionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/employees/{employeeId}/positions")
@CrossOrigin("*")
public class EmployeeWorkPositionController {
    @Autowired
    private IEmployeeWorkPositionService service;



    @GetMapping
    public List<WorkPosition> getPositions(@PathVariable Long employeeId) {
        return service.getPositionsByEmployee(employeeId);
    }


    @PostMapping("/{positionId}")
    public String assign(@PathVariable Long employeeId, @PathVariable Long positionId) {
        service.assignPosition(employeeId, positionId);
        return "Gán vị trí thành công";
    }

    @DeleteMapping("/{positionId}")
    public String remove(@PathVariable Long employeeId, @PathVariable Long positionId) {
        service.removePosition(employeeId, positionId);
        return "Gỡ vị trí thành công";
    }

}
