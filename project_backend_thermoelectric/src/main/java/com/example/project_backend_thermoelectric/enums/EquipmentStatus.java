package com.example.project_backend_thermoelectric.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum EquipmentStatus {

    ACTIVE("Đang vận hành"),
    UNDER_REPAIR("Đang sửa chữa"),
    CLOSING("Đang đóng");

    private final String displayName;
}