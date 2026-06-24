package com.example.project_backend_thermoelectric.controller.technical_report;

import com.example.project_backend_thermoelectric.dto.technical_report.CreateTechnicalReportDto;
import com.example.project_backend_thermoelectric.entity.TechnicalReport;
import com.example.project_backend_thermoelectric.entity.WorkOrder;
import com.example.project_backend_thermoelectric.repository.work_orders.IWorkOrderRepository;
import com.example.project_backend_thermoelectric.service.technical_report.ITechnicalReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/technical-reports")
@CrossOrigin("*")
public class TechnicalReportController {

    @Autowired
    private ITechnicalReportService service;

    @Autowired
    private IWorkOrderRepository workOrderRepository;
    // 1. Thêm biên bản
    @PostMapping
    public ResponseEntity<TechnicalReport> create(@RequestBody CreateTechnicalReportDto dto) {
        TechnicalReport report = service.createTechnicalReport(dto);
        return ResponseEntity.ok(report);
    }
    // 2. Cập nhật biên bản
    @PutMapping("/{id}")
    public ResponseEntity<TechnicalReport> update(
            @PathVariable Long id,
            @RequestBody CreateTechnicalReportDto dto
    ) {
        TechnicalReport report = service.updateTechnicalReport(id, dto);
        return ResponseEntity.ok(report);
    }
    // 3. Xóa biên bản
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        service.deleteTechnicalReport(id);
        return ResponseEntity.ok("Đã xóa biên bản id=" + id);
    }
    // 4. Xem chi tiết biên bản
    @GetMapping("/{id}")
    public ResponseEntity<TechnicalReport> getDetail(@PathVariable Long id) {
        TechnicalReport report = service.getTechnicalReportById(id);
        return ResponseEntity.ok(report);
    }
    // 5. Lấy tất cả biên bản theo WorkOrder
    @GetMapping("/work-order/{workOrderId}")
    public ResponseEntity<List<TechnicalReport>> getByWorkOrder(@PathVariable Long workOrderId) {
        List<TechnicalReport> reports = service.getTechnicalReportsByWorkOrderId(workOrderId);
        return ResponseEntity.ok(reports);
    }
    // 6. Phân trang + tìm kiếm
    @GetMapping("/search")
    public ResponseEntity<Page<TechnicalReport>> search(
            @RequestParam(required = false, defaultValue = "") String keyword,
            @RequestParam(required = false) Long workOrderId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "createdAt") String sort,
            @RequestParam(defaultValue = "desc") String direction
    ) {
        Sort sortObj = direction.equalsIgnoreCase("desc") ?
                Sort.by(sort).descending() : Sort.by(sort).ascending();
        Pageable pageable = PageRequest.of(page, size, sortObj);

        Page<TechnicalReport> result = service.searchTechnicalReports(keyword, workOrderId, pageable);
        return ResponseEntity.ok(result);
    }

    // 7. NEW: GET WORK ORDER LIST (FOR DROPDOWN)
    // =========================
    @GetMapping("/work-orders")
    public ResponseEntity<List<WorkOrder>> getWorkOrders() {
        return ResponseEntity.ok(
                workOrderRepository.findAllByOrderByCodeAsc()
        );
    }

    // =========================
    // 8. NEW: FIND WORK ORDER BY CODE (optional helper)
    // =========================
    @GetMapping("/work-orders/{code}")
    public ResponseEntity<WorkOrder> getWorkOrderByCode(@PathVariable String code) {
        WorkOrder wo = workOrderRepository.findByCode(code)
                .orElseThrow(() -> new RuntimeException("WorkOrder không tồn tại"));
        return ResponseEntity.ok(wo);
    }
}
