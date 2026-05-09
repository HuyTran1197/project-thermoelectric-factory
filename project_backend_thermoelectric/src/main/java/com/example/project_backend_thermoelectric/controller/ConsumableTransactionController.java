package com.example.project_backend_thermoelectric.controller;

import com.example.project_backend_thermoelectric.entity.ConsumableTransaction;
import com.example.project_backend_thermoelectric.service.impl.IConsumableTransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("*")
@RequestMapping("/consumable-transactions")
public class ConsumableTransactionController {
    @Autowired
    private IConsumableTransactionService consumableTransactionService;
    @PostMapping
    public ResponseEntity<ConsumableTransaction> save(@RequestBody ConsumableTransaction consumableTransaction) {
        ConsumableTransaction saved = consumableTransactionService.importConsumable(consumableTransaction);
        return ResponseEntity.ok(saved);
    }
}
