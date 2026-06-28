package com.example.project_backend_thermoelectric.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum RepairOrderStatus {
    PENDING,
    IN_PROGRESS,
    COMPLETED
}
