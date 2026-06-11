package com.example.project_backend_thermoelectric.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum RepairOrderStatus {
    CHO_DUYET("Chờ duyệt"),
    DA_DUYET("Đã duyệt"),
    DANG_SUA_CHUA("Đang sửa chữa"),
    DA_HOAN_THANH("Đã hoàn thành"),
    KHONG_DUYET("Không duyệt"),
    DA_HUY("DA_HUY");
    private final  String displayName;
}
