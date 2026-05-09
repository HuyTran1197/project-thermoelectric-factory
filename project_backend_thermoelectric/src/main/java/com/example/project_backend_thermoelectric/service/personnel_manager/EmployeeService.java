package com.example.project_backend_thermoelectric.service.personnel_manager;

import com.example.project_backend_thermoelectric.entity.Department;
import com.example.project_backend_thermoelectric.entity.Employee;
import com.example.project_backend_thermoelectric.entity.Position;
import com.example.project_backend_thermoelectric.repository.personnel_manager.IDepartmentRepo;
import com.example.project_backend_thermoelectric.repository.personnel_manager.IEmployeeRepo;
import com.example.project_backend_thermoelectric.repository.personnel_manager.IPositionRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeService implements IEmployeeService {
    @Autowired
    private IEmployeeRepo employeeRepo;
    @Autowired
    private IDepartmentRepo departmentRepo;
    @Autowired
    private IPositionRepo positionRepo;

    @Override
    public Employee createEmployee(Employee employee, Long departmentId, Long positionId) {
        Department department = departmentRepo.findById(departmentId).orElseThrow(
                ()->new RuntimeException("Department Not Found"));
        Position position = positionRepo.findById(positionId).orElseThrow(
                ()->new RuntimeException("Position Not Found"));
        employee.setDepartment(department);
        employee.setPosition(position);
        return employeeRepo.save(employee);
    }
    @Override
    public List<Employee> getAllEmployees() {
        return employeeRepo.findAll();
    }
    @Override
    public Employee getEmployeeById(Long id){
        return employeeRepo.findById(id).orElseThrow(
                ()->new RuntimeException("Employee Not Found"));
    }
    @Override
    public Employee updateEmployee(Long id, Employee request, Long departmentId, Long positionId) {
        Employee employee = getEmployeeById(id);
        Department department = departmentRepo.findById(departmentId).orElseThrow(
                ()->new RuntimeException("Department Not Found"));
        Position position = positionRepo.findById(positionId).orElseThrow(
                ()->new RuntimeException("Position Not Found"));
        employee.setFullName(request.getFullName());
        employee.setDepartment(department);
        employee.setPosition(position);
        return employeeRepo.save(employee);
    }
    @Override
    public void deleteEmployee(Long id){
        if(!employeeRepo.existsById(id)){
            throw new RuntimeException("Employee Not Found");
        }
        employeeRepo.deleteById(id);
    }
}
