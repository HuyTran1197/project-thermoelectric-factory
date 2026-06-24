package com.example.project_backend_thermoelectric.service.work_order;

import com.example.project_backend_thermoelectric.dto.work_orders.WorkOrderDetailDto;
import com.example.project_backend_thermoelectric.entity.Equipment;
import com.example.project_backend_thermoelectric.entity.RepairOrder;
import com.example.project_backend_thermoelectric.entity.WorkOrder;
import com.example.project_backend_thermoelectric.enums.EquipmentStatus;
import com.example.project_backend_thermoelectric.enums.RepairOrderStatus;
import com.example.project_backend_thermoelectric.enums.WorkOrderStatus;
import com.example.project_backend_thermoelectric.repository.operations_manager.equipment.IEquipmentRepo;
import com.example.project_backend_thermoelectric.repository.repair_order.IRepairOrderRepository;
import com.example.project_backend_thermoelectric.repository.work_orders.IWorkOrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class WorkOrderCompletionService {

    private final IWorkOrderRepository workOrderRepository;
    private final IRepairOrderRepository repairOrderRepository;
    private final IEquipmentRepo equipmentRepo;
    private final IWorkOrderService workOrderService;

    @Transactional(readOnly = true)
    public WorkOrderDetailDto getByRepairOrder(Long repairOrderId) {
        WorkOrder workOrder = workOrderRepository.findByRequestId(repairOrderId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy Work Order của Repair Order"));

        return workOrderService.detail(workOrder.getId());
    }


    @Transactional
    public void closeWorkOrder(Long workOrderId) {
        WorkOrder workOrder = workOrderRepository.findById(workOrderId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy Work Order"));

        if (workOrder.getStatus() == WorkOrderStatus.HOAN_THANH) {
            throw new RuntimeException("Phiếu này đã được đóng trước đó");
        }

        if (workOrder.getStatus() == WorkOrderStatus.CHO_VAT_TU) {
            throw new RuntimeException("Phiếu công tác đang chờ vật tư, không thể đóng phiếu");
        }

        // Đóng phiếu kỹ thuật
        workOrder.setStatus(WorkOrderStatus.HOAN_THANH);
        workOrder.setEndDate(LocalDateTime.now());
        workOrderRepository.save(workOrder);

        // Nghiệm thu yêu cầu sửa chữa
        RepairOrder repairOrder = workOrder.getRequest();
        repairOrder.setStatus(RepairOrderStatus.DA_HOAN_THANH);
        repairOrderRepository.save(repairOrder);

        // Đưa thiết bị quay lại vận hành
        Equipment equipment = repairOrder.getEquipment();
        equipment.setStatus(EquipmentStatus.DANG_VAN_HANH);
        equipmentRepo.save(equipment);
    }
}