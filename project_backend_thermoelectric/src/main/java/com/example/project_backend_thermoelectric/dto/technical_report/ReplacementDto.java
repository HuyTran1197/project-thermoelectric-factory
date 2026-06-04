package com.example.project_backend_thermoelectric.dto.technical_report;

import lombok.*;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReplacementDto {
    private Long materialId; // ID vật tư trong DB
    private String name;     // Tên vật tư
    private Integer quantity; // Số lượng đề xuất
}
