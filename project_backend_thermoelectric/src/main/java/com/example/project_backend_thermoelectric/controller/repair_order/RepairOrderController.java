package com.example.project_backend_thermoelectric.controller.repair_order;

import com.example.project_backend_thermoelectric.dto.repair_order.CreateRepairOrderDto;
import com.example.project_backend_thermoelectric.dto.repair_order.UpdateRepairOrderDto;
import com.example.project_backend_thermoelectric.service.repair_order.IRepairOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/repair-orders")
@RequiredArgsConstructor
@CrossOrigin("*")
public class RepairOrderController {

    private final IRepairOrderService repairOrderService;

    @GetMapping
    public ResponseEntity<?> getAll(
            @RequestParam(defaultValue = "") String keyword,
            @RequestParam(defaultValue = "0") int page
    ) {

        return ResponseEntity.ok(
                repairOrderService.getAll(
                        keyword,
                        page
                )
        );
    }

    @PostMapping
    public ResponseEntity<?> create(
            @RequestBody
            CreateRepairOrderDto dto
    ) {

        return ResponseEntity.status(
                HttpStatus.CREATED
        ).body(
                repairOrderService.create(dto)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(
            @PathVariable Long id,
            @RequestBody UpdateRepairOrderDto dto
    ) {

        System.out.println(dto.getTitle());
        System.out.println(dto.getDescription());
        System.out.println(dto.getStatus());
        System.out.println(dto.getEquipmentId());

        return ResponseEntity.ok(
                repairOrderService.update(id, dto)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(
            @PathVariable Long id
    ) {

        repairOrderService.delete(id);

        return ResponseEntity.noContent().build();
    }

}