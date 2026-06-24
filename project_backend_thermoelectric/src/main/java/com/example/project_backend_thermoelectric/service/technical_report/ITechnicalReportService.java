package com.example.project_backend_thermoelectric.service.technical_report;

import com.example.project_backend_thermoelectric.dto.technical_report.CreateTechnicalReportDto;
import com.example.project_backend_thermoelectric.entity.TechnicalReport;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ITechnicalReportService {
    // Thêm mới biên bản
    TechnicalReport createTechnicalReport(CreateTechnicalReportDto dto);

    // Cập nhật biên bản
    TechnicalReport updateTechnicalReport(Long id, CreateTechnicalReportDto dto);

    // Xóa biên bản
    void deleteTechnicalReport(Long id);

    // Lấy chi tiết biên bản
    TechnicalReport getTechnicalReportById(Long id);

    // Lấy tất cả biên bản theo WorkOrder
    List<TechnicalReport> getTechnicalReportsByWorkOrderId(Long workOrderId);

    // Phân trang + tìm kiếm (content hoặc workOrderId)
    Page<TechnicalReport> searchTechnicalReports(String keyword, Long workOrderId, Pageable pageable);

//    TechnicalReportDto mapToDto(TechnicalReport report);
}
