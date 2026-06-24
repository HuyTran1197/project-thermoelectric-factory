//package com.example.project_backend_thermoelectric.repository.technical_report;
//
//import com.example.project_backend_thermoelectric.entity.RepairOrder;
//import com.example.project_backend_thermoelectric.entity.WorkOrder;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.jpa.repository.JpaRepository;
//
//import java.util.List;
//
//public interface IWorkOrderRepo extends JpaRepository<WorkOrder,Long> {
//    // 1. Lấy tất cả WorkOrder theo Request
//    List<WorkOrder> findByRequest(RepairOrder request);
//    // 2. Kiểm tra tồn tại WorkOrder theo Request
//    boolean existsByRequest(RepairOrder request);
//    // 3. Phân trang tất cả WorkOrder
//    Page<WorkOrder> findAll(Pageable pageable);
//    // 4. Tìm kiếm theo status hoặc request id
//    Page<WorkOrder> findByStatusContainingIgnoreCaseOrRequestId(String status, Long requestId, Pageable pageable);
//    // 5. Xóa WorkOrder theo ID
//    void deleteById(Long id);
//}
