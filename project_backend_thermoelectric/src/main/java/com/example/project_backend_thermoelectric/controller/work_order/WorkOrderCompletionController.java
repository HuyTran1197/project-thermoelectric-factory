package com.example.project_backend_thermoelectric.controller.work_order;

import com.example.project_backend_thermoelectric.dto.work_orders.WorkOrderDetailDto;
import com.example.project_backend_thermoelectric.service.work_order.WorkOrderCompletionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/work-order-completion")
@RequiredArgsConstructor
@CrossOrigin("*")
public class WorkOrderCompletionController {

    private final WorkOrderCompletionService service;

    @GetMapping("/repair-orders/{repairOrderId}")
    public ResponseEntity<WorkOrderDetailDto> getByRepairOrder(
            @PathVariable Long repairOrderId
    ) {
        return ResponseEntity.ok(
                service.getByRepairOrder(repairOrderId)
        );
    }

    @PutMapping("/{id}/close")
    public ResponseEntity<?> closeWorkOrder(
            @PathVariable Long id
    ) {
        service.closeWorkOrder(id);
        return ResponseEntity.ok("Dong phieu thanh cong");
    }
}
