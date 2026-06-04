package com.example.project_backend_thermoelectric.controller.personnel_manager;


import com.example.project_backend_thermoelectric.dto.personnel_manager.CreateEmployeeDto;
import com.example.project_backend_thermoelectric.dto.personnel_manager.UpdateEmployeeDto;
import com.example.project_backend_thermoelectric.entity.Employee;
import com.example.project_backend_thermoelectric.service.personnel_manager.IEmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/employees")
@CrossOrigin("*")
public class EmployeeController {
    @Autowired
    private IEmployeeService employeeService;

    @PostMapping
    public Employee createEmployee(@RequestBody CreateEmployeeDto request) {
        Employee employee = new Employee();
        employee.setFullName(request.getFullName());

        return employeeService.createEmployee(
                employee,
                request.getDepartmentId(),
                request.getPositionId()
        );
    }

    @GetMapping
    public List<Employee> getAllEmployees() {
        return employeeService.getAllEmployees();
    }

    @GetMapping("/search")
    public Page<Employee> searchEmployees(
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        return employeeService.searchEmployees(keyword, page, size);
    }

    @GetMapping("/{id}")
    public Employee getEmployeeById(@PathVariable Long id) {
        return employeeService.getEmployeeById(id);
    }

    @PutMapping("/{id}")
    public Employee updateEmployee(
            @PathVariable Long id,
            @RequestBody UpdateEmployeeDto request
    ) {
        Employee employee = new Employee();
        employee.setFullName(request.getFullName());

        return employeeService.updateEmployee(
                id,
                employee,
                request.getDepartmentId(),
                request.getPositionId()
        );
    }

    @DeleteMapping("/{id}")
    public String deleteEmployee(@PathVariable Long id) {
        employeeService.deleteEmployee(id);
        return "Delete employee successfully";
    }
}
