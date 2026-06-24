package com.example.project_backend_thermoelectric.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum EquipmentStatus {

    DANG_VAN_HANH("Đang vận hành"),
    DANG_SUA_CHUA("Đang sửa chữa"),
    DANG_DONG("Đang đóng");

    private final String displayName;
}