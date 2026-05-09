package com.example.project_backend_thermoelectric.controller.materials_manager;

import com.example.project_backend_thermoelectric.entity.ReplacementTransaction;
import com.example.project_backend_thermoelectric.service.materials_manager.IReplacementTransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/replacement-transactions")
public class ReplacementTransactionController {
    @Autowired
    private IReplacementTransactionService replacementTransactionService;

    @PostMapping
    public ResponseEntity<ReplacementTransaction> importReplacement(@RequestBody ReplacementTransaction replacementTransaction) {
        ReplacementTransaction saved = replacementTransactionService.importReplacement(replacementTransaction);
        return ResponseEntity.ok(saved);
    }
}
