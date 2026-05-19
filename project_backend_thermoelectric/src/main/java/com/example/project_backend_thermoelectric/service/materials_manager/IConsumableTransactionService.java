package com.example.project_backend_thermoelectric.service.materials_manager;

import com.example.project_backend_thermoelectric.dto.materials_manager.ConsumableInventoryDto;
import com.example.project_backend_thermoelectric.dto.materials_manager.ConsumableTransactionDto;
import com.example.project_backend_thermoelectric.entity.ConsumableTransaction;
import com.example.project_backend_thermoelectric.enums.TransactionType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;

public interface IConsumableTransactionService {
    Page<ConsumableInventoryDto> getConsumableInventory(Pageable pageable, String code, String name);
    Page<ConsumableTransactionDto> getConsumableTransactions(Pageable pageable, String keyword, String type, LocalDateTime from, LocalDateTime to);
    ConsumableTransaction importConsumable(ConsumableTransaction consumableTransaction);
    ConsumableTransaction exportConsumable(ConsumableTransaction consumableTransaction);
}
