package com.example.project_backend_thermoelectric.dto.work_orders;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AssignWorkOrderDto {

    private List<AssignmentItemDto> assignments;
}
