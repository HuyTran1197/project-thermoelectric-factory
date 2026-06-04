package com.example.project_backend_thermoelectric.repository.work_orders;

import com.example.project_backend_thermoelectric.entity.WorkOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface IWorkOrderRepository extends JpaRepository<WorkOrder,Long> {
}
