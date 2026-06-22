package com.example.project_backend_thermoelectric.dto.work_orders;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReplacementDetailDto {

    private Long materialId;

    private String code;

    private String name;

    private String unit;

    private Integer quantity;
}
