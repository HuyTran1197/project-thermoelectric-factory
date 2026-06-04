package com.example.project_backend_thermoelectric.service.materials_manager;

import com.example.project_backend_thermoelectric.dto.materials_manager.FullMaterialExportDto;
import com.example.project_backend_thermoelectric.entity.*;
import com.example.project_backend_thermoelectric.enums.TransactionType;
import com.example.project_backend_thermoelectric.repository.materials_manager.*;
import com.example.project_backend_thermoelectric.repository.personnel_manager.IUserRepo;
import com.example.project_backend_thermoelectric.repository.work_orders.IWorkOrderConsumableRepository;
import com.example.project_backend_thermoelectric.repository.work_orders.IWorkOrderReplacementRepository;
import com.example.project_backend_thermoelectric.repository.work_orders.IWorkOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class MaterialExportService implements IMaterialExportService {

    @Autowired
    private IConsumableTransactionRepository consumableTxRepository;

    @Autowired
    private IWorkOrderConsumableRepository workOrderConsumableRepository;

    @Autowired
    private IConsumableMaterialRepository consumableMaterialRepository;

    @Autowired
    private IReplacementTransactionRepository replacementTxRepository;

    @Autowired
    private IWorkOrderReplacementRepository workOrderReplacementRepository;

    @Autowired
    private IReplacementMaterialRepository replacementMaterialRepository;

    @Autowired
    private IUserRepo userRepository;

    @Autowired
    private IWorkOrderRepository workOrderRepository;

    @Override
    @Transactional
    public void exportMaterialToWorkOrder(FullMaterialExportDto exportDTO) {

        // 1. Tìm phiếu sửa chữa (WorkOrder) cha xem có tồn tại không
        WorkOrder workOrder = workOrderRepository.findById(exportDTO.getWorkOrderId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy phiếu sửa chữa ID: " + exportDTO.getWorkOrderId()));

        // 🔥 CHẶN KHÔNG CHO QUẢN ĐỐC CẤP TRÙNG NẾU PHIẾU ĐÃ GỬI ĐI RỒI
        if (workOrder.getMaterialStatus() != null && !"NOT_REQUESTED".equals(workOrder.getMaterialStatus())) {
            throw new RuntimeException("❌ Phiếu sửa chữa này đã được gửi yêu cầu cấp vật tư trước đó!");
        }

        // 2. Xử lý lưu danh sách VẬT TƯ TIÊU HAO được yêu cầu
        if (exportDTO.getConsumables() != null && !exportDTO.getConsumables().isEmpty()) {
            for (FullMaterialExportDto.MaterialItem item : exportDTO.getConsumables()) {
                ConsumableMaterial material = consumableMaterialRepository.findById(item.getMaterialId())
                        .orElseThrow(() -> new RuntimeException("Không tìm thấy vật tư tiêu hao ID: " + item.getMaterialId()));

                WorkOrderConsumable woc = new WorkOrderConsumable();
                woc.setWorkOrder(workOrder);
                woc.setMaterial(material);
                woc.setQuantity(item.getQuantity());

                workOrderConsumableRepository.save(woc);
            }
        }

        // 3. Xử lý lưu danh sách PHỤ TÙNG THAY THẾ được yêu cầu
        if (exportDTO.getReplacements() != null && !exportDTO.getReplacements().isEmpty()) {
            for (FullMaterialExportDto.MaterialItem item : exportDTO.getReplacements()) {
                ReplacementMaterial material = replacementMaterialRepository.findById(item.getMaterialId())
                        .orElseThrow(() -> new RuntimeException("Không tìm thấy phụ tùng thay thế ID: " + item.getMaterialId()));

                WorkOrderReplacement wor = new WorkOrderReplacement();
                wor.setWorkOrder(workOrder);
                wor.setMaterial(material);
                wor.setQuantity(item.getQuantity());

                workOrderReplacementRepository.save(wor);
            }
        }

        // =================================================================
        // ĐOẠN CODE CÒN THIẾU CỦA QUẢN ĐỐC: CẬP NHẬT TRẠNG THÁI PHIẾU MẸ
        // =================================================================
        // Chuyển trạng thái sang "Chờ thủ kho duyệt" để kích hoạt isReadOnly ở Frontend
        workOrder.setMaterialStatus("PENDING_RELEASE");
        workOrderRepository.save(workOrder); // Ghi nhận thay đổi xuống DB
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void approveAndReleaseMaterials(Long workOrderId, Long warehouseStaffId) {

        // 1. Kiểm tra thông tin Thủ kho
        User staff = userRepository.findById(warehouseStaffId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy thông tin Thủ kho ID: " + warehouseStaffId));

        // 2. Tìm phiếu sửa chữa (WorkOrder) cha
        WorkOrder workOrder = workOrderRepository.findById(workOrderId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy phiếu sửa chữa ID: " + workOrderId));

        // 🔥 CHẶN KHÔNG CHO THỦ KHO XUẤT PHIẾU NÀY LẦN NỮA NẾU ĐÃ RELEASED
        if ("RELEASED".equals(workOrder.getMaterialStatus())) {
            throw new RuntimeException("❌ Vật tư của phiếu sửa chữa này đã được xuất kho thực tế, không thể xuất lại!");
        }

        // =================================================================
        // PHẦN 1: KIỂM TRA & XUẤT KHO VẬT TƯ TIÊU HAO
        // =================================================================
        List<WorkOrderConsumable> requestedConsumables = workOrderConsumableRepository.findByWorkOrder(workOrder);

        for (WorkOrderConsumable req : requestedConsumables) {
            ConsumableMaterial material = req.getMaterial();
            int qtyToExport = req.getQuantity();

            int currentStock = consumableTxRepository.getStockQuantity(material.getId());

            if (currentStock < qtyToExport) {
                throw new RuntimeException("Kho không đủ số lượng cho vật tư tiêu hao: " + material.getName()
                        + " (Hiện tồn: " + currentStock + ", Quản đốc yêu cầu: " + qtyToExport + ")");
            }

            ConsumableTransaction tx = new ConsumableTransaction();
            tx.setMaterial(material);
            tx.setType(TransactionType.EXPORT);
            tx.setQuantity(qtyToExport);
            tx.setCreatedBy(staff);
            tx.setCreatedAt(LocalDateTime.now());

            consumableTxRepository.save(tx);
        }

        // =================================================================
        // PHẦN 2: KIỂM TRA & XUẤT KHO PHỤ TÙNG THAY THẾ
        // =================================================================
        List<WorkOrderReplacement> requestedReplacements = workOrderReplacementRepository.findByWorkOrder(workOrder);

        for (WorkOrderReplacement req : requestedReplacements) {
            ReplacementMaterial material = req.getMaterial();
            int qtyToExport = req.getQuantity();

            int currentStock = replacementTxRepository.getStockQuantity(material.getId());

            if (currentStock < qtyToExport) {
                throw new RuntimeException("Kho không đủ số lượng cho phụ tùng thay thế: " + material.getName()
                        + " (Hiện tồn: " + currentStock + ", Quản đốc yêu cầu: " + qtyToExport + ")");
            }

            ReplacementTransaction tx = new ReplacementTransaction();
            tx.setMaterial(material);
            tx.setType(TransactionType.EXPORT);
            tx.setQuantity(qtyToExport);
            tx.setCreatedBy(staff);
            tx.setCreatedAt(LocalDateTime.now());

            replacementTxRepository.save(tx);
        }

        // =================================================================
        // ĐOẠN CODE CÒN THIẾU CỦA THỦ KHO: CẬP NHẬT TRẠNG THÁI PHIẾU MẸ
        // =================================================================
        // Đánh dấu chính thức xuất kho hoàn tất để khóa vĩnh viễn quy trình cấp phát
        workOrder.setMaterialStatus("RELEASED");
        workOrderRepository.save(workOrder);
    }
}