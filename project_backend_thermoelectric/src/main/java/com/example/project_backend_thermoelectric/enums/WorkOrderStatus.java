package com.example.project_backend_thermoelectric.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum WorkOrderStatus {

    DA_PHAN_CONG("Đã phân công"),
    DANG_THUC_HIEN("Đang thực hiện"),
    CHO_VAT_TU("Chờ vật tư"),
    HOAN_THANH("Hoàn thành");

    private final String displayName;
}