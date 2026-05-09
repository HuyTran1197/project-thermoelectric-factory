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
@Table(name = "consumable_materials")
public class ConsumableMaterial {

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
    @Pattern(regexp = "^CON-[0-9]{4}$",
            message = "Định dạng mã: CON-XXXX với X là các số từ 0 đến 9"
    )
    @Column(nullable = false)
    private String code;

    @NotBlank(message = "Không được bỏ trống")
    @Pattern(regexp = "^\\p{Lu}\\p{L}+(\\s\\p{L}+)*$",
            message = "Yêu cầu chữ cái đầu in HOA và không chứa kí tự đặc biệt"
    )
    @Column(nullable = false)
    private String unit;

    @NotBlank(message = "Không được bỏ trống")
    @Pattern(regexp = "^\\p{Lu}\\p{L}+(\\s\\p{L}+)*$",
            message = "Yêu cầu chữ cái đầu in HOA và không chứa kí tự đặc biệt"
    )
    @Column(nullable = false)
    private String location;

    @Column(columnDefinition = "TEXT")
    private String description;
}