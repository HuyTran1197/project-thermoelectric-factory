package com.example.project_backend_thermoelectric.controller.work_order;

import com.example.project_backend_thermoelectric.dto.work_orders.AssignWorkOrderDto;
import com.example.project_backend_thermoelectric.dto.work_orders.CreateWorkOrderDto;
import com.example.project_backend_thermoelectric.dto.work_orders.CreateWorkOrderResponseDto;
import com.example.project_backend_thermoelectric.dto.work_orders.WorkOrderResponseDto;
import com.example.project_backend_thermoelectric.entity.WorkOrder;
import com.example.project_backend_thermoelectric.service.work_order.IWorkOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/work-orders")
@RequiredArgsConstructor
@CrossOrigin("*")
public class WorkOrderController {

    private final IWorkOrderService workOrderService;

    @GetMapping
    public ResponseEntity<Page<WorkOrderResponseDto>> search(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "") String code,
            @RequestParam(defaultValue = "") String equipment,
            @RequestParam(required = false) String status
    ) {
        Pageable pageable = PageRequest.of(page,5);
        return ResponseEntity.ok(
                workOrderService.search(
                        code,
                        equipment,
                        status,
                        pageable
                )
        );
    }

    @GetMapping("/repair-orders")
    public ResponseEntity<?> repairOrdersForWorkOrder(@RequestParam(defaultValue = "0") int page,
                                                      @RequestParam(defaultValue = "") String title,
                                                      @RequestParam(defaultValue = "") String createdBy,
                                                      @RequestParam(required = false) Long equipmentId,
                                                      @RequestParam(required = false) String repairStatus,
                                                      @RequestParam(required = false) Boolean hasWorkOrder) {

        Pageable pageable = PageRequest.of(page,5);

        return ResponseEntity.ok(
                workOrderService.searchForWorkOrder(
                        title,
                        createdBy,
                        equipmentId,
                        repairStatus,
                        hasWorkOrder,
                        pageable
                )
        );
    }

    @GetMapping("/notifications")
    public ResponseEntity<?> notification() {

        return ResponseEntity.ok(workOrderService.getNotification());
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody CreateWorkOrderDto dto) {
        WorkOrder workOrder = workOrderService.create(dto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        new CreateWorkOrderResponseDto(
                                workOrder.getId(),
                                workOrder.getCode()
                        )
                );
    }

    @PutMapping("/{id}/assignments")
    public ResponseEntity<?> updateAssignments(
            @PathVariable Long id,
            @RequestBody AssignWorkOrderDto dto
    ) {
        workOrderService.updateAssignments(
                id,
                dto
        );
        return ResponseEntity.ok(
                "Cập nhật phân công thành công"
        );
    }

    @PutMapping("/{id}/complete")
    public ResponseEntity<?> complete(
            @PathVariable Long id
    ) {
        workOrderService.complete(id);
        return ResponseEntity.ok("Hoan thanh Work Order thanh cong");
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> detail(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(
                workOrderService.detail(id)
        );
    }
}
