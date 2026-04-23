package com.example.project_backend_thermoelectric.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "replacement_materials")
public class ReplacementMaterial {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String code;

    private String unit;

    private String location;

    @Column(columnDefinition = "TEXT")
    private String description;
}
