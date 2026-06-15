package com.example.project_backend_thermoelectric.controller;

import com.example.project_backend_thermoelectric.dto.DashboardDto;
import com.example.project_backend_thermoelectric.repository.operations_manager.equipment.IEquipmentRepo;
import com.example.project_backend_thermoelectric.repository.personnel_manager.IUserRepo;
import com.example.project_backend_thermoelectric.repository.repair_order.RepairOrderRepository;
import com.example.project_backend_thermoelectric.repository.tool.ToolRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final IEquipmentRepo equipmentRepo;
    private final RepairOrderRepository repairOrderRepo;
    private final IUserRepo userRepo;
    private final ToolRepository toolRepo;

    @GetMapping
    public
    DashboardDto getDashboard() {

        return new DashboardDto(
                equipmentRepo.count(),
                repairOrderRepo.count(),
                userRepo.count(),
                toolRepo.count()
        );
    }
}