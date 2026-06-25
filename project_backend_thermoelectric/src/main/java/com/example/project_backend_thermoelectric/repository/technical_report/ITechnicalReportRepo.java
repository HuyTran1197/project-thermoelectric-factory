package com.example.project_backend_thermoelectric.repository.technical_report;

import com.example.project_backend_thermoelectric.dto.technical_report.TechnicalReportResponseDto;
import com.example.project_backend_thermoelectric.dto.work_orders.WorkOrderResponseDto;
import com.example.project_backend_thermoelectric.entity.TechnicalReport;
import com.example.project_backend_thermoelectric.entity.WorkOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

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
    @Query(value = "select tr.id as id,wo.code as workOrderCode, e.code as equipmentCode, e.name as equipmentName, " +
            "tr.created_at as createdAt " +
            "from technical_reports tr " +
            "join work_orders wo on wo.id = tr.work_order_id " +
            "join repair_order ro on ro.id = wo.request_id " +
            "join equipments e on e.id = ro.equipment_id " +
            "where (:searchWorkOrderCode is null or :searchWorkOrderCode = '' or wo.code like concat('%',:searchWorkOrderCode,'%'))",
            countQuery =
                    "select count(*) " +
                            "from technical_reports tr " +
                            "join work_orders wo on wo.id = tr.work_order_id " +
                            "join repair_order ro on ro.id = wo.request_id " +
                            "join equipments e on e.id = ro.equipment_id " +
                            "where (:searchWorkOrderCode is null or :searchWorkOrderCode = '' or wo.code like concat('%',:searchWorkOrderCode,'%'))",
            nativeQuery = true)
    Page<TechnicalReportResponseDto> search(@Param("searchWorkOrderCode") String workOrderCode,
                                            Pageable pageable);

    // 6. Xóa biên bản theo ID
    void deleteById(Long id);
}
