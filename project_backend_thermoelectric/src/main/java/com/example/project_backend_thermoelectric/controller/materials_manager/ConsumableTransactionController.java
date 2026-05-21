package com.example.project_backend_thermoelectric.controller.materials_manager;

import com.example.project_backend_thermoelectric.dto.materials_manager.ConsumableInventoryDto;
import com.example.project_backend_thermoelectric.dto.materials_manager.ConsumableTransactionDto;
import com.example.project_backend_thermoelectric.entity.ConsumableTransaction;
import com.example.project_backend_thermoelectric.enums.TransactionType;
import com.example.project_backend_thermoelectric.service.materials_manager.IConsumableTransactionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/consumable-transactions")
public class ConsumableTransactionController {
    @Autowired
    private IConsumableTransactionService consumableTransactionService;
    @GetMapping("/history")
    public ResponseEntity<?> getHistory(
            Pageable pageable,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to
    ) {
        LocalDateTime fromDateTime = (from != null) ? from.atStartOfDay() : null; // 2026-05-17T00:00:00
        LocalDateTime toDateTime = (to != null) ? to.atTime(23, 59, 59, 999999999) : null; // 2026-05-17T23:59:59.999...

        Page<ConsumableTransactionDto> result = consumableTransactionService.getConsumableTransactions(
                pageable, keyword, type, fromDateTime, toDateTime
        );

        return ResponseEntity.ok(result);
    }
    @PostMapping
    public ResponseEntity<?> save(
            @RequestBody ConsumableTransaction consumableTransaction) {

        ConsumableTransaction saved = consumableTransactionService.importConsumable(consumableTransaction);
        return ResponseEntity.ok(saved);
    }
    @GetMapping
    public ResponseEntity<Page<ConsumableInventoryDto>> getInventory(@RequestParam(required = false) String code,
                                                                     @RequestParam(required = false) String name,
                                                                     @RequestParam(defaultValue = "0") int page,
                                                                     @RequestParam(defaultValue = "5") int size) {

        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(
                consumableTransactionService.getConsumableInventory(pageable, code, name)
        );
    }
}
