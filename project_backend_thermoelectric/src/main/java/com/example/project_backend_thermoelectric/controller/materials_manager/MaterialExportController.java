package com.example.project_backend_thermoelectric.controller.materials_manager;

import com.example.project_backend_thermoelectric.dto.materials_manager.FullMaterialExportDto;
import com.example.project_backend_thermoelectric.entity.WorkOrder;
import com.example.project_backend_thermoelectric.entity.WorkOrderConsumable;
import com.example.project_backend_thermoelectric.entity.WorkOrderReplacement;
import com.example.project_backend_thermoelectric.repository.work_orders.IWorkOrderConsumableRepository;
import com.example.project_backend_thermoelectric.repository.work_orders.IWorkOrderReplacementRepository;
import com.example.project_backend_thermoelectric.repository.work_orders.IWorkOrderRepository;
import com.example.project_backend_thermoelectric.service.materials_manager.IMaterialExportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/material-export")
@CrossOrigin("*")
public class MaterialExportController {

    @Autowired
    private IWorkOrderConsumableRepository workOrderConsumableRepository;

    @Autowired
    private IWorkOrderReplacementRepository workOrderReplacementRepository;

    @Autowired
    private IWorkOrderRepository workOrderRepository;

    @Autowired
    private IMaterialExportService materialExportService;

    // 1. API QUẢN ĐỐC: Gửi yêu cầu cấp phát vật tư
    @PostMapping("/supply-slip")
    public ResponseEntity<?> exportMaterials(@RequestBody FullMaterialExportDto exportDTO) {
        try {
            // Mọi logic lưu vật tư VÀ đổi trạng thái thành "PENDING_RELEASE" đã được gom vào đây
            materialExportService.exportMaterialToWorkOrder(exportDTO);

            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Cấp phát toàn bộ vật tư và gửi yêu cầu lên Kho thành công!"
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("success", false, "message", "Có lỗi hệ thống: " + e.getMessage()));
        }
    }

    // 2. API THỦ KHO: Duyệt xuất kho thực tế
    @PostMapping("/approve/{workOrderId}")
    public ResponseEntity<?> approveAndReleaseMaterials(@PathVariable Long workOrderId, @RequestParam Long staffId) {
        try {
            // Mọi logic trừ kho VÀ đổi trạng thái thành "RELEASED" đã được gom vào đây
            materialExportService.approveAndReleaseMaterials(workOrderId, staffId);

            return ResponseEntity.ok(Map.of("success", true, "message", "Duyệt xuất kho thành công!"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", e.getMessage()));
        }
    }

    // 3. API BỔ SUNG: Lấy thông tin trạng thái của WorkOrder (Frontend đang gọi hàm này để check lock giao diện)
    @GetMapping("/work-order/{id}")
    public ResponseEntity<?> getWorkOrderDetails(@PathVariable Long id) {
        try {
            WorkOrder workOrder = workOrderRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy phiếu sửa chữa ID: " + id));
            return ResponseEntity.ok(workOrder);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("success", false, "message", e.getMessage()));
        }
    }

    // 4. API Lấy danh sách vật tư tiêu hao đã xin của phiếu
    @GetMapping("/work-order-consumables/work-order/{requestId}")
    public ResponseEntity<?> getConsumablesByWorkOrder(@PathVariable Long requestId) {
        try {
            WorkOrder workOrder = workOrderRepository.findById(requestId)
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy phiếu sửa chữa"));
            List<WorkOrderConsumable> list = workOrderConsumableRepository.findByWorkOrder(workOrder);
            return ResponseEntity.ok(list);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", e.getMessage()));
        }
    }

    // 5. API Lấy danh sách phụ tùng thay thế đã xin của phiếu
    @GetMapping("/work-order-replacements/work-order/{requestId}")
    public ResponseEntity<?> getReplacementsByWorkOrder(@PathVariable Long requestId) {
        try {
            WorkOrder workOrder = workOrderRepository.findById(requestId)
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy phiếu sửa chữa"));
            List<WorkOrderReplacement> list = workOrderReplacementRepository.findByWorkOrder(workOrder);
            return ResponseEntity.ok(list);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", e.getMessage()));
        }
    }
}