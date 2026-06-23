package com.example.project_backend_thermoelectric.service.work_order;

import com.example.project_backend_thermoelectric.dto.work_orders.*;
import com.example.project_backend_thermoelectric.entity.WorkOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

public interface IWorkOrderService {
    Page<WorkOrderResponseDto> search(
            @Param("searchCode") String code,
            @Param("searchEquipment") String equipment,
            @Param("searchStatus") String status,
            Pageable pageable);

    WorkOrder create(CreateWorkOrderDto dto);

    void updateAssignments(
            Long workOrderId,
            AssignWorkOrderDto dto
    );

    void complete(Long workOrderId);

    WorkOrderDetailDto detail(Long id);

    Page<RepairOrderForWorkOrderDto> searchForWorkOrder(@Param("searchTitle") String title,
                                                        @Param("searchCreatedBy") String createdBy,
                                                        @Param("equipmentId") Long equipmentId,
                                                        @Param("repairStatus") String repairStatus,
                                                        @Param("hasWorkOrder") Boolean hasWorkOrder,
                                                        Pageable pageable);
    PendingWorkOrderNotificationDto getNotification();
}
