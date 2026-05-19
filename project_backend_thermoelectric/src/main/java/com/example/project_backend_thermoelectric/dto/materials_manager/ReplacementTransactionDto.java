package com.example.project_backend_thermoelectric.dto.materials_manager;
import com.example.project_backend_thermoelectric.enums.TransactionType;


import java.time.LocalDateTime;

public interface ReplacementTransactionDto {
    Long getId();

    TransactionType getType();

    Integer getQuantity();

    LocalDateTime getCreatedAt();

    String getMaterialCode();

    String getMaterialName();

    String getUsername();
}
