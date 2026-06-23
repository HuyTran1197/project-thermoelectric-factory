package com.example.project_backend_thermoelectric.service.technical_report;

import com.example.project_backend_thermoelectric.dto.technical_report.CreateTechnicalReportDto;
import com.example.project_backend_thermoelectric.dto.technical_report.EquipmentReportDto;
import com.example.project_backend_thermoelectric.entity.TechnicalReport;
import com.example.project_backend_thermoelectric.entity.WorkOrder;
import com.example.project_backend_thermoelectric.repository.materials_manager.IReplacementMaterialRepository;
import com.example.project_backend_thermoelectric.repository.operations_manager.equipment.IEquipmentRepo;
import com.example.project_backend_thermoelectric.repository.personnel_manager.IUserRepo;
import com.example.project_backend_thermoelectric.repository.technical_report.ITechnicalReportRepo;
//import com.example.project_backend_thermoelectric.repository.technical_report.IWorkOrderRepo;
import com.example.project_backend_thermoelectric.repository.work_orders.IWorkOrderRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TechnicalReportService implements ITechnicalReportService {
    @Autowired
    private IEquipmentRepo equipmentRepo;

    @Autowired
    private IReplacementMaterialRepository materialRepo;
    @Autowired
    private IUserRepo userRepo;

    @Autowired
    private ITechnicalReportRepo technicalReportRepository;

    @Autowired
    private IWorkOrderRepository workOrderRepository;

    private ObjectMapper objectMapper = new ObjectMapper();

    // Tạo biên bản
    @Override
    @Transactional
    public TechnicalReport createTechnicalReport(CreateTechnicalReportDto dto) {
//        WorkOrder workOrder = workOrderRepository.findById(dto.getWorkOrderId())
//                .orElseThrow(() -> new RuntimeException("WorkOrder không tồn tại"));
        WorkOrder workOrder = workOrderRepository.findByCode(dto.getWorkOrderCode())
                .orElseThrow(() -> new RuntimeException("WorkOrder không tồn tại"));

//        User user = userRepo.findById(dto.getCreatedBy())
//                .orElseThrow(() -> new RuntimeException("User không tồn tại"));

        TechnicalReport report = new TechnicalReport();
        report.setWorkOrder(workOrder);
//        report.setCreatedBy(user);
        report.setCreatedBy(null);
        report.setCreatedAt(LocalDateTime.now());

        // Gán tên thiết bị trong content như trước
        if (dto.getEquipmentReports() != null) {
            for (EquipmentReportDto eq : dto.getEquipmentReports()) {
                if (eq.getEquipmentId() != null) {
                    equipmentRepo.findById(eq.getEquipmentId())
                            .ifPresent(e -> {
                                eq.setEquipmentName(e.getName());
                                eq.setEquipmentCode(e.getCode());
                            });
                }
            }
        }
        try {
            String contentJson = objectMapper.writeValueAsString(dto);
            report.setContent(contentJson);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Lỗi serialize JSON cho biên bản", e);
        }

        return technicalReportRepository.save(report);
    }

    // Cập nhật biên bản
    @Override
    @Transactional
    public TechnicalReport updateTechnicalReport(Long id, CreateTechnicalReportDto dto) {
        TechnicalReport existing = technicalReportRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Biên bản không tồn tại"));

//        WorkOrder workOrder = workOrderRepository.findById(dto.getWorkOrderId())
//                .orElseThrow(() -> new RuntimeException("WorkOrder không tồn tại"));
        WorkOrder workOrder = workOrderRepository.findByCode(dto.getWorkOrderCode())
                .orElseThrow(() -> new RuntimeException("WorkOrder không tồn tại"));

        existing.setWorkOrder(workOrder);
        //up
//        if (dto.getCreatedBy() != null) {
//            User user = userRepo.findById(dto.getCreatedBy())
//                    .orElseThrow(() -> new RuntimeException("User không tồn tại"));
//            existing.setCreatedBy(user);
//        }
        try {
            String contentJson = objectMapper.writeValueAsString(dto);
            existing.setContent(contentJson);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Lỗi serialize JSON cho biên bản", e);
        }
        return technicalReportRepository.save(existing);
    }

    // Xóa biên bản
    @Override
    @Transactional
    public void deleteTechnicalReport(Long id) {
        if (!technicalReportRepository.existsById(id)) {
            throw new RuntimeException("Biên bản không tồn tại");
        }
        technicalReportRepository.deleteById(id);
    }

    // Xem chi tiết biên bản
    @Override
    public TechnicalReport getTechnicalReportById(Long id) {
        return technicalReportRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Biên bản không tồn tại"));
    }

    // Lấy tất cả biên bản theo WorkOrder
    @Override
    public List<TechnicalReport> getTechnicalReportsByWorkOrderId(Long workOrderId) {
        WorkOrder workOrder = workOrderRepository.findById(workOrderId)
                .orElseThrow(() -> new RuntimeException("WorkOrder không tồn tại"));

        return technicalReportRepository.findByWorkOrder(workOrder);
    }

    // Tìm kiếm + phân trang
    @Override
    public Page<TechnicalReport> searchTechnicalReports(String keyword, Long workOrderId, Pageable pageable) {
        return technicalReportRepository.findByContentContainingIgnoreCaseOrWorkOrderId(
                keyword, workOrderId, pageable
        );
    }
}
