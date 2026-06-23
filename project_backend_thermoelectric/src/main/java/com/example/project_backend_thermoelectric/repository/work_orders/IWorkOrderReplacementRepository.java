package com.example.project_backend_thermoelectric.repository.work_orders;

import com.example.project_backend_thermoelectric.entity.WorkOrder;
import com.example.project_backend_thermoelectric.entity.WorkOrderReplacement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IWorkOrderReplacementRepository extends JpaRepository<WorkOrderReplacement, Long> {
    List<WorkOrderReplacement> findByWorkOrder(WorkOrder workOrder);
    void deleteByWorkOrder(WorkOrder workOrder);

    List<WorkOrderReplacement>
    findByWorkOrderId(Long workOrderId);
}
