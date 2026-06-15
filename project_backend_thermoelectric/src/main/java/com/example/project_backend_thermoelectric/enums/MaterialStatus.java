package com.example.project_backend_thermoelectric.enums;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MaterialStatus {
    CHUA_YEU_CAU_CAP_PHAT("Chưa yêu cầu cấp phát"),
    CHO_CAP_PHAT("Chờ cấp phát"),
    DA_CAP_PHAT("Đã cấp phát");
    private final String displayName;

}


