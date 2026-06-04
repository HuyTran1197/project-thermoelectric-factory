package com.example.project_backend_thermoelectric.dto.repair_order;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateRepairOrderDto {

    private String title;

    private String description;

    private Long equipmentId;

    private Long createdById;
}
