package com.example.project_backend_thermoelectric.dto.repair_order;

import com.example.project_backend_thermoelectric.enums.RepairOrderStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateRepairOrderDto {

    private String title;

    private String description;

    private RepairOrderStatus status;

    private Long equipmentId;
}
