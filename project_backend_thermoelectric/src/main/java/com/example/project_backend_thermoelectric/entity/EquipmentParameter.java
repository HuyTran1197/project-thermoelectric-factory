package com.example.project_backend_thermoelectric.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "equipment_parameters")
public class EquipmentParameter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Không được bỏ trống")
    private String value;

    @ManyToOne
    @JoinColumn(name = "equipment_id",nullable = false)
    private Equipment equipment;

    @ManyToOne
    @JoinColumn(name = "parameter_id",nullable = false)
    private ParameterDefinition parameter;
}
