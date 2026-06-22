package com.example.project_backend_thermoelectric.repository.work_orders;

import com.example.project_backend_thermoelectric.entity.WorkOrderAssignment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IWorkOrderAssignmentRepo extends JpaRepository<WorkOrderAssignment,Long> {
    List<WorkOrderAssignment> findByWorkOrderId(Long workOrderId);

    void deleteByWorkOrderId(Long workOrderId);
}
