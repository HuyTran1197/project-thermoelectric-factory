package com.example.project_backend_thermoelectric.enums;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MaterialStatus {
    ISSUANCE_NOT_YET_REQUESTED("Chưa yêu cầu cấp phát"),
    PENDING_ISSUANCE("Chờ cấp phát"),
    ISSUED("Đã cấp phát");
    private final String displayName;

}


