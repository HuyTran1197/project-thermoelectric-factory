package com.example.project_backend_thermoelectric.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum WorkOrderStatus {

    ASSIGNED("Đã phân công"),
    IN_PROGRESS("Đang thực hiện"),
    WAITING_FOR_MATERIALS("Chờ vật tư"),
    COMPLETED("Hoàn thành");

    private final String displayName;
}