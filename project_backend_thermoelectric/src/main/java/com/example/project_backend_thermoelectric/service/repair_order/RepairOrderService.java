package com.example.project_backend_thermoelectric.service.repair_order;

import com.example.project_backend_thermoelectric.dto.repair_order.CreateRepairOrderDto;
import com.example.project_backend_thermoelectric.dto.repair_order.UpdateRepairOrderDto;
import com.example.project_backend_thermoelectric.entity.Equipment;
import com.example.project_backend_thermoelectric.entity.RepairOrder;
import com.example.project_backend_thermoelectric.entity.User;
import com.example.project_backend_thermoelectric.enums.RepairOrderStatus;
import com.example.project_backend_thermoelectric.repository.operations_manager.equipment.IEquipmentRepo;
import com.example.project_backend_thermoelectric.repository.personnel_manager.IUserRepo;
import com.example.project_backend_thermoelectric.repository.repair_order.RepairOrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;


import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class RepairOrderService implements IRepairOrderService {

    private final RepairOrderRepository repairOrderRepository;
    private final IEquipmentRepo equipmentRepository;
    private final IUserRepo userRepo;


    @Override
    public Page<RepairOrder> getAll(String keyword, int page) {

        return repairOrderRepository.search(
                keyword,
                PageRequest.of(page, 3)
        );
    }

    @Override
    public RepairOrder create(CreateRepairOrderDto dto) {

        Equipment equipment =
                equipmentRepository.findById(dto.getEquipmentId())
                        .orElseThrow(
                                () -> new RuntimeException("Không tìm thấy thiết bị")
                        );

        String username =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getName();

        User user =
                userRepo.findByUsername(username)
                        .orElseThrow(
                                () -> new RuntimeException("Không tìm thấy user")
                        );

        RepairOrder order = new RepairOrder();

        order.setTitle(dto.getTitle());
        order.setDescription(dto.getDescription());
        order.setEquipment(equipment);
        order.setCreatedBy(user);
        order.setStatus(RepairOrderStatus .CHO_DUYET);
        order.setCreatedAt(LocalDateTime.now());

        return repairOrderRepository.save(order);
    }

    @Override
    public RepairOrder update(Long id, UpdateRepairOrderDto dto) {

        RepairOrder order =
                repairOrderRepository.findById(id)
                        .orElseThrow(
                                () -> new RuntimeException("Không tìm thấy yêu cầu")
                        );

        Equipment equipment =
                equipmentRepository.findById(dto.getEquipmentId())
                        .orElseThrow(
                                () -> new RuntimeException("Không tìm thấy thiết bị")
                        );

        order.setTitle(dto.getTitle());
        order.setDescription(dto.getDescription());
        order.setEquipment(equipment);
        order.setStatus(dto.getStatus());
        if (order.getStatus() != RepairOrderStatus.CHO_DUYET) {
            throw new RuntimeException(
                    "Yêu cầu đã được xử lý, không thể chỉnh sửa"
            );
        }
        return repairOrderRepository.save(order);
    }

    @Override
    public void delete(Long id) {

        RepairOrder order =
                repairOrderRepository.findById(id)
                        .orElseThrow(
                                () -> new RuntimeException("Không tìm thấy yêu cầu")
                        );

        if (order.getStatus() != RepairOrderStatus.CHO_DUYET) {
            throw new RuntimeException(
                    "Chỉ được xóa yêu cầu ở trạng thái PENDING"
            );
        }

        repairOrderRepository.delete(order);
    }
}