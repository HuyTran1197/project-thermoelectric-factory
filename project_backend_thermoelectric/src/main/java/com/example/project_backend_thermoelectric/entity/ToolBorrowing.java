package com.example.project_backend_thermoelectric.entity;

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
@Table(name = "tool_borrowings")
public class ToolBorrowing {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Số lượng mượn không được để trống")
    @Min(value = 1, message = "Số lượng mượn phải ít nhất là 1")
    private Integer quantity;

    private String status;

    private String note;

    @NotNull(message = "Ngày mượn không được để trống")
    @Column(name = "borrow_date")
    private LocalDateTime borrowDate;

    @Column(name = "due_date")
    private LocalDateTime dueDate;

    @Column(name = "return_date")
    private LocalDateTime returnDate;

    @NotNull(message = "Dụng cụ không được để trống")
    @ManyToOne
    @JoinColumn(name = "tool_id")
    private Tool tool;

    @NotNull(message = "Nhân viên mượn không được để trống")
    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;
}
