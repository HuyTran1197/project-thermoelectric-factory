package com.example.project_backend_thermoelectric.repository.technical_report;

import com.example.project_backend_thermoelectric.entity.TechnicalReport;
import com.example.project_backend_thermoelectric.entity.WorkOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ITechnicalReportRepo extends JpaRepository<TechnicalReport,Long> {
    // 1. Lấy tất cả biên bản theo WorkOrder
    List<TechnicalReport> findByWorkOrder(WorkOrder workOrder);
    // 2. Kiểm tra tồn tại biên bản cho WorkOrder
    boolean existsByWorkOrder(WorkOrder workOrder);
    // 3. Lấy biên bản theo WorkOrder + người tạo
    List<TechnicalReport> findByWorkOrderAndCreatedById(WorkOrder workOrder, Long createdById);
    // 4. Phân trang tất cả biên bản
    Page<TechnicalReport> findAll(Pageable pageable);
    // 5. Tìm kiếm theo nội dung JSON (content) hoặc WorkOrder ID
    //    - Pageable để phân trang
    Page<TechnicalReport> findByContentContainingIgnoreCaseOrWorkOrderId(
            String keyword, Long workOrderId, Pageable pageable
    );
    // 6. Xóa biên bản theo ID
    void deleteById(Long id);
}
