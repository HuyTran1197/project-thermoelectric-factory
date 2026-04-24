package com.example.project_backend_thermoelectric.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "systems")
public class SystemEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Không được bỏ trống")
    @Pattern(regexp = "^\\p{Lu}\\p{L}+(\\s\\p{L}+)*$",
            message = "Yêu cầu chữ cái đầu in HOA và không chứa kí tự đặc biệt"
    )
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;
}
