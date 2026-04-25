package com.example.project_backend_thermoelectric.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tools")
public class Tool {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Tên dụng cụ không được để trống")
    private String name;

    @NotBlank(message = "Mã dụng cụ không được để trống")
    private String code;

    private String type;

    @NotNull(message = "Tổng số lượng không được để trống")
    @Min(value = 0, message = "Tổng số lượng phải lớn hơn hoặc bằng 0")
    @Column(name = "total_quantity")
    private Integer totalQuantity;

    @NotNull(message = "Số lượng có sẵn không được để trống")
    @Min(value = 0, message = "Số lượng có sẵn phải lớn hơn hoặc bằng 0")
    @Column(name = "available_quantity")
    private Integer availableQuantity;

    private String location;

    @Column(columnDefinition = "TEXT")
    private String description;
}
