package com.example.project_backend_thermoelectric.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "replacement_materials",
        uniqueConstraints = { @UniqueConstraint(columnNames = "code") })
public class ReplacementMaterial {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Không được bỏ trống")
    @Pattern(regexp = "^\\p{Lu}\\p{L}+(\\s\\p{L}+)*$",
            message = "Yêu cầu chữ cái đầu in HOA và không chứa kí tự đặc biệt"
    )
    @Column(nullable = false)
    private String name;

    @NotBlank(message = "Không được bỏ trống")
    @Pattern(regexp = "^MAT-[0-9]{4}$",
            message = "Định dạng mã: MAT-XXXX với X là các số từ 0 đến 9"
    )
    @Column(nullable = false, unique = true)
    private String code;

    @NotBlank(message = "Không được bỏ trống")
    @Pattern(regexp = "^\\p{Lu}\\p{L}+(\\s\\p{L}+)*$",
            message = "Yêu cầu chữ cái đầu in HOA và không chứa kí tự đặc biệt"
    )
    @Column(nullable = false)
    private String unit;

    private String location;

    @Column(columnDefinition = "TEXT")
    private String description;
}