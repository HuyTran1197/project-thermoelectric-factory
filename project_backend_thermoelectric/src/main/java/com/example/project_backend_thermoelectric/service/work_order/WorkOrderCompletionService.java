package com.example.project_backend_thermoelectric.service.work_order;

import com.example.project_backend_thermoelectric.dto.work_orders.WorkOrderDetailDto;
import com.example.project_backend_thermoelectric.entity.RepairOrder;
import com.example.project_backend_thermoelectric.entity.WorkOrder;
import com.example.project_backend_thermoelectric.enums.RepairOrderStatus;
import com.example.project_backend_thermoelectric.enums.WorkOrderStatus;
import com.example.project_backend_thermoelectric.repository.repair_order.IRepairOrderRepository;
import com.example.project_backend_thermoelectric.repository.work_orders.IWorkOrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class WorkOrderCompletionService {

    private final IWorkOrderRepository workOrderRepository;
    private final IRepairOrderRepository repairOrderRepository;
    private final IWorkOrderService workOrderService;

    @Transactional(readOnly = true)
    public WorkOrderDetailDto getByRepairOrder(Long repairOrderId) {
        WorkOrder workOrder = workOrderRepository.findByRequestId(repairOrderId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy phiếu công tác của yêu cầu sửa chữa"));

        return workOrderService.detail(workOrder.getId());
    }


    @Transactional
    public void closeWorkOrder(Long workOrderId) {
        WorkOrder workOrder = workOrderRepository.findById(workOrderId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy phiếu công tác"));

        if (workOrder.getStatus() != WorkOrderStatus.HOAN_THANH) {
            throw new RuntimeException("Chỉ được đóng phiếu công tác khi ở trạng thái HOAN_THANH");
        }

        RepairOrder repairOrder = workOrder.getRequest();

        // ✅ Chặn đóng lần 2
        if (repairOrder.getStatus() == RepairOrderStatus.DA_HOAN_THANH) {
            throw new RuntimeException("Phiếu này đóng trước đó");
        }

        repairOrder.setStatus(RepairOrderStatus.DA_HOAN_THANH);
        repairOrderRepository.save(repairOrder);
    }
//    @Transactional
//    public void closeWorkOrder(Long workOrderId) {
//        WorkOrder workOrder = workOrderRepository.findById(workOrderId)
//                .orElseThrow(() -> new RuntimeException("Khong tim thay Work Order"));
//
//        if (workOrder.getStatus() != WorkOrderStatus.HOAN_THANH) {
//            throw new RuntimeException("Chi duoc dong phieu khi Work Order o trang thai HOAN_THANH");
//        }
//
//        RepairOrder repairOrder = workOrder.getRequest();
//
//        if (repairOrder.getStatus() != RepairOrderStatus.CHO_NGHIEM_THU) {
//            throw new RuntimeException("Chi duoc dong phieu khi Repair Order o trang thai CHO_NGHIEM_THU");
//        }
//
//        repairOrder.setStatus(RepairOrderStatus.DA_HOAN_THANH);
//        repairOrderRepository.save(repairOrder);
//    }
}
