package com.example.project_backend_thermoelectric.service.materials_manager;

import com.example.project_backend_thermoelectric.dto.materials_manager.ReplacementInventoryDto;
import com.example.project_backend_thermoelectric.dto.materials_manager.ReplacementTransactionDto;
import com.example.project_backend_thermoelectric.entity.ReplacementTransaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;

public interface IReplacementTransactionService {
    Page<ReplacementInventoryDto> getReplacementInventory(Pageable pageable, String code, String name);
    Page<ReplacementTransactionDto> getReplacementTransactions(Pageable pageable, String keyword, String type, LocalDateTime from, LocalDateTime to);
    ReplacementTransaction importReplacement(ReplacementTransaction replacementTransaction);
    ReplacementTransaction exportReplacement(ReplacementTransaction replacementTransaction);
}
