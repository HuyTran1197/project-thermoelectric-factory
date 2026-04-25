package com.example.project_backend_thermoelectric.entity;

import com.example.project_backend_thermoelectric.enums.TransactionType; // Import enum của bạn
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "replacement_transactions")
public class ReplacementTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Loại giao dịch không được để trống")
    @Column(nullable = false, length = 10)
    private TransactionType type;

    @NotNull(message = "Số lượng không được để trống")
    @Min(value = 1, message = "Số lượng phải lớn hơn hoặc bằng 1")
    @Column(nullable = false)
    private Integer quantity;

    @NotNull(message = "Ngày tạo không được để trống")
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @NotNull(message = "Vật tư không được bỏ trống")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "material_id", nullable = false)
    private ReplacementMaterial material;

    @NotNull(message = "Người tạo không được bỏ trống")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "created_by", nullable = false)
    private User createdBy;
}