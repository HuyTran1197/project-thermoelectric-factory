package com.example.project_backend_thermoelectric.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum WorkOrderRole {

    LANH_DAO_CONG_VIEC("Lãnh đạo công việc"),

    CHI_HUY_TRUC_TIEP("Chỉ huy trực tiếp"),

    GIAM_SAT_AN_TOAN("Giám sát an toàn"),

    NHAN_VIEN_LAM_VIEC("Nhân viên làm việc");

    private final String displayName;
}