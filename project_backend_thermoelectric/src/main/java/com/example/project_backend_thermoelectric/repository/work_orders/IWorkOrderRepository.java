package com.example.project_backend_thermoelectric.repository.work_orders;

import com.example.project_backend_thermoelectric.entity.WorkOrder;
import com.example.project_backend_thermoelectric.enums.MaterialStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository

public interface IWorkOrderRepository extends JpaRepository<WorkOrder,Long> {
    List<WorkOrder> getWorkOrdersByMaterialStatus(MaterialStatus materialStatus);

    long countByMaterialStatus(MaterialStatus materialStatus);
}
