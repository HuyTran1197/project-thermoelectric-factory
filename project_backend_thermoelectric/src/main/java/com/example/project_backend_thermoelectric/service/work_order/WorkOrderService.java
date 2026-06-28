package com.example.project_backend_thermoelectric.service.work_order;

import com.example.project_backend_thermoelectric.dto.work_orders.*;
import com.example.project_backend_thermoelectric.entity.*;
import com.example.project_backend_thermoelectric.enums.EquipmentStatus;
import com.example.project_backend_thermoelectric.enums.MaterialStatus;
import com.example.project_backend_thermoelectric.enums.WorkOrderStatus;
import com.example.project_backend_thermoelectric.repository.operations_manager.equipment.IEquipmentRepo;
import com.example.project_backend_thermoelectric.repository.personnel_manager.IEmployeeRepo;
import com.example.project_backend_thermoelectric.repository.personnel_manager.IUserRepo;
import com.example.project_backend_thermoelectric.repository.repair_order.IRepairOrderRepository;
import com.example.project_backend_thermoelectric.repository.work_orders.IWorkOrderAssignmentRepo;
import com.example.project_backend_thermoelectric.repository.work_orders.IWorkOrderConsumableRepository;
import com.example.project_backend_thermoelectric.repository.work_orders.IWorkOrderReplacementRepository;
import com.example.project_backend_thermoelectric.repository.work_orders.IWorkOrderRepository;
import com.example.project_backend_thermoelectric.enums.RepairOrderStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class WorkOrderService implements IWorkOrderService {

    private final IWorkOrderRepository workOrderRepository;
    private final IWorkOrderConsumableRepository workOrderConsumableRepository;
    private final IWorkOrderReplacementRepository workOrderReplacementRepository;
    private final IRepairOrderRepository repairOrderRepository;
    private final IUserRepo userRepo;
    private final IEmployeeRepo employeeRepo;
    private final IWorkOrderAssignmentRepo assignmentRepo;
    private final IEquipmentRepo equipmentRepo;

    private String generateCode() {
        LocalDate now = LocalDate.now();
        int year = now.getYear();
        int month = now.getMonthValue();
        long sequence = workOrderRepository.countByYear(year) + 1;
        return String.format(
                "%04d/%02d/%d",
                sequence,
                month,
                year
        );
    }

    @Override
    public List<WorkOrder> getList() {
        return workOrderRepository.findAll();
    }

    @Override
    public Page<WorkOrderResponseDto> search(String code, String equipment, String status, Pageable pageable) {
        return workOrderRepository.search(code,equipment,status,pageable);
    }

    private void saveAssignments(WorkOrder workOrder, List<AssignmentItemDto> assignments) {
        for (AssignmentItemDto item : assignments) {
            Employee employee = employeeRepo.findById(item.getEmployeeId())
                    .orElseThrow(
                            () -> new RuntimeException(
                                    "Không tìm thấy nhân viên"
                            )
                    );

            WorkOrderAssignment assignment = new WorkOrderAssignment();

            assignment.setWorkOrder(workOrder);
            assignment.setEmployee(employee);
            assignment.setRoleInWork(item.getRole());

            assignmentRepo.save(assignment);
        }
    }

    @Override
    public WorkOrder create(CreateWorkOrderDto dto) {
        RepairOrder repairOrder = repairOrderRepository.findById(dto.getRepairOrderId())
                        .orElseThrow(
                                () -> new RuntimeException(
                                        "Không tìm thấy yêu cầu sửa chữa"
                                )
                        );

        if (workOrderRepository.existsByRequestId(repairOrder.getId())) {
            throw new RuntimeException(
                    "Yêu cầu này đã có phiếu công tác"
            );
        }

        String username =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getName();

        User currentUser = userRepo.findByUsername(username)
                        .orElseThrow(
                                () -> new RuntimeException(
                                        "Không tìm thấy người dùng"
                                )
                        );

        WorkOrder workOrder = new WorkOrder();

        workOrder.setCode(generateCode());
        workOrder.setRequest(repairOrder);
        workOrder.setCreatedBy(currentUser);

        workOrder.setStatus(WorkOrderStatus.ASSIGNED);

        workOrder.setMaterialStatus(MaterialStatus.ISSUANCE_NOT_YET_REQUESTED);

        workOrder.setStartDate(LocalDateTime.now());
        repairOrder.setStatus(RepairOrderStatus.IN_PROGRESS);

        repairOrderRepository.save(repairOrder);
        Equipment equipment = repairOrder.getEquipment();
        equipment.setStatus(EquipmentStatus.UNDER_REPAIR);
        equipmentRepo.save(equipment);
        WorkOrder savedWorkOrder = workOrderRepository.save(workOrder);

        saveAssignments(savedWorkOrder, dto.getAssignments());
        return savedWorkOrder;
    }

    @Override
    public void updateAssignments(Long workOrderId, AssignWorkOrderDto dto) {
        WorkOrder workOrder = workOrderRepository.findById(workOrderId)
                        .orElseThrow(
                                () -> new RuntimeException(
                                        "Không tìm thấy phiếu công tác"
                                )
                        );

        assignmentRepo.deleteByWorkOrderId(workOrderId);
        assignmentRepo.flush(); // ép Hibernate xoá thật xuống DB ngay, tránh xung đột unique key với insert sau
        saveAssignments(
                workOrder,
                dto.getAssignments()
        );
    }

    @Override
    @Transactional(readOnly = true)
    public WorkOrderDetailDto detail(Long id) {

        WorkOrder workOrder = workOrderRepository.findById(id).orElseThrow(
                                () -> new RuntimeException(
                                        "Không tìm thấy phiếu công tác"));

        RepairOrder repairOrder = workOrder.getRequest();
        Equipment equipment = repairOrder.getEquipment();

        List<WorkOrderAssignment> assignments = assignmentRepo.findByWorkOrderId(id);
        List<WorkOrderConsumable> consumables = workOrderConsumableRepository.findByWorkOrderId(id);
        List<WorkOrderReplacement> replacements = workOrderReplacementRepository.findByWorkOrderId(id);

        WorkOrderDetailDto dto = new WorkOrderDetailDto();

        dto.setId(workOrder.getId());
        dto.setCode(workOrder.getCode());
        dto.setStatus(workOrder.getStatus().getDisplayName());
        dto.setMaterialStatus(workOrder.getMaterialStatus().getDisplayName());
        dto.setStartDate(workOrder.getStartDate());
        dto.setEndDate(workOrder.getEndDate());

        Employee creator = workOrder.getCreatedBy().getEmployee();

        dto.setCreatedBy(creator.getFullName());
        dto.setCreatedDepartment(creator.getDepartment().getName());
        dto.setCreatedPosition(creator.getPosition().getName());

        dto.setRepairTitle(repairOrder.getTitle());
        dto.setRepairDescription(repairOrder.getDescription());

        dto.setEquipmentName(equipment.getName());
        dto.setEquipmentCode(equipment.getCode());
        dto.setSystemName(equipment.getSystem().getName());

        dto.setAssignments(
                assignments.stream()
                        .map(a ->
                                new AssignmentItemDto(
                                        a.getEmployee().getId(),
                                        a.getEmployee().getFullName(),
                                        a.getRoleInWork()
                                )
                        )
                        .toList()
        );

        dto.setConsumables(
                consumables.stream()
                        .map(c ->
                                new ConsumableDetailDto(
                                        c.getMaterial().getId(),
                                        c.getMaterial().getCode(),
                                        c.getMaterial().getName(),
                                        c.getMaterial().getUnit(),
                                        c.getQuantity()
                                )
                        )
                        .toList()
        );

        dto.setReplacements(
                replacements.stream()
                        .map(r ->
                                new ReplacementDetailDto(
                                        r.getMaterial().getId(),
                                        r.getMaterial().getCode(),
                                        r.getMaterial().getName(),
                                        r.getMaterial().getUnit(),
                                        r.getQuantity()
                                )
                        )
                        .toList()
        );

        return dto;
    }

    @Override
    public Page<RepairOrderForWorkOrderDto> searchForWorkOrder(String title,
                                                               String createdBy,
                                                               Long equipmentId,
                                                               String repairStatus,
                                                               Boolean hasWorkOrder,
                                                               Pageable pageable) {
        return repairOrderRepository.searchForWorkOrder(
                title,
                createdBy,
                equipmentId,
                repairStatus,
                hasWorkOrder,
                pageable);
    }

    @Override
    public PendingWorkOrderNotificationDto getNotification() {
        Long count = repairOrderRepository.countRepairOrderWithoutWorkOrder();
        return new PendingWorkOrderNotificationDto(count);
    }
}
