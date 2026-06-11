package com.example.project_backend_thermoelectric.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DashboardDto {

    private Long totalEquipments;
    private Long totalRepairOrders;
    private Long totalUsers;
    private Long totalTools;
}
