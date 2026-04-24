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
@Table(name = "equipments")
public class Equipment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Không được bỏ trống")
    @Pattern(regexp = "^\\p{Lu}\\p{L}+(\\s\\p{L}+)*$",
            message = "Yêu cầu chữ cái đầu in HOA và không chứa kí tự đặc biệt"
    )
    private String name;

    @NotBlank(message = "Không được bỏ trống")
    @Pattern(regexp = "^KKS-[0-9]{4}$",
            message = "Định dạng mã: KKS-XXXX với X là các số từ 0 đến 9"
    )
    private String code;

    @ManyToOne
    @JoinColumn(name = "system_id",nullable = false)
    private SystemEntity system;

    @ManyToOne
    @JoinColumn(name = "type_id",nullable = false)
    private EquipmentType type;

    @NotBlank(message = "Không được bỏ trống")
    @Pattern(regexp = "^\\p{Lu}\\p{L}+(\\s\\p{L}+)*$",
            message = "Yêu cầu chữ cái đầu in HOA và không chứa kí tự đặc biệt"
    )
    private String status;

}
