package com.example.project_backend_thermoelectric.dto.work_orders;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateWorkOrderDto {

    private Long repairOrderId;
    private List<AssignmentItemDto> assignments;

}
