package com.example.project_backend_thermoelectric.dto.technical_report;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EquipmentReportDto {
    private Long equipmentId;
    private String equipmentName;
    private String damageDescription;  // Mô tả hư hỏng
    private String assessment;         // Đánh giá kỹ thuật
    private String proposedSolution;   // Phương án xử lý
    private List<ReplacementDto> replacements; // Danh sách vật tư đề xuất
}
