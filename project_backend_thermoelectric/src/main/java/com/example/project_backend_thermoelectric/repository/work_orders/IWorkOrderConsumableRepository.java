package com.example.project_backend_thermoelectric.repository.work_orders;

import com.example.project_backend_thermoelectric.entity.WorkOrder;
import com.example.project_backend_thermoelectric.entity.WorkOrderConsumable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IWorkOrderConsumableRepository extends JpaRepository<WorkOrderConsumable, Long> {
    List<WorkOrderConsumable> findByWorkOrder(WorkOrder workOrder);
    void deleteByWorkOrder(WorkOrder workOrder);

    List<WorkOrderConsumable>
    findByWorkOrderId(Long workOrderId);
}
