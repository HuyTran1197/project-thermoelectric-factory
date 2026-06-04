package com.example.project_backend_thermoelectric.service.repair_order;

import com.example.project_backend_thermoelectric.dto.repair_order.CreateRepairOrderDto;
import com.example.project_backend_thermoelectric.dto.repair_order.UpdateRepairOrderDto;
import com.example.project_backend_thermoelectric.entity.RepairOrder;
import org.springframework.data.domain.Page;

public interface IRepairOrderService {

    Page<RepairOrder> getAll(String keyword, int page);

    RepairOrder create(CreateRepairOrderDto dto);

    RepairOrder update(Long id, UpdateRepairOrderDto dto);

    void delete(Long id);
}