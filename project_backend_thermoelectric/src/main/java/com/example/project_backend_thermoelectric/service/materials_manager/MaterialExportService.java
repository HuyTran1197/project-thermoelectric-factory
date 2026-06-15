package com.example.project_backend_thermoelectric.service.materials_manager;

import com.example.project_backend_thermoelectric.dto.materials_manager.FullMaterialExportDto;
import com.example.project_backend_thermoelectric.entity.*;
import com.example.project_backend_thermoelectric.enums.MaterialStatus;
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

        WorkOrder workOrder = workOrderRepository.findById(exportDTO.getWorkOrderId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy phiếu sửa chữa ID: " + exportDTO.getWorkOrderId()));

        MaterialStatus currentStatus = workOrder.getMaterialStatus();
        if (currentStatus == MaterialStatus.DA_CAP_PHAT) {
            throw new RuntimeException("Vật tư của phiếu sửa chữa này đã được xuất kho thực tế, không thể chỉnh sửa!");
        }

        if (currentStatus == MaterialStatus.CHO_CAP_PHAT) {
            workOrderConsumableRepository.deleteByWorkOrder(workOrder);
            workOrderConsumableRepository.flush();

            workOrderReplacementRepository.deleteByWorkOrder(workOrder);
            workOrderReplacementRepository.flush();
        }
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

        workOrder.setMaterialStatus(MaterialStatus.CHO_CAP_PHAT);
        workOrderRepository.save(workOrder);
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

        if (workOrder.getMaterialStatus() == MaterialStatus.DA_CAP_PHAT) {
            throw new RuntimeException("❌ Vật tư của phiếu sửa chữa này đã được xuất kho thực tế, không thể xuất lại!");
        }


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
        workOrder.setMaterialStatus(MaterialStatus.DA_CAP_PHAT);
        workOrderRepository.save(workOrder);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void approveSpecificMaterials(Long workOrderId, Long staffId, List<Long> approvedConsumableIds, List<Long> approvedReplacementIds) {

        User staff = userRepository.findById(staffId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy thông tin Thủ kho ID: " + staffId));

        WorkOrder workOrder = workOrderRepository.findById(workOrderId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy phiếu sửa chữa ID: " + workOrderId));

        if (workOrder.getMaterialStatus() == MaterialStatus.DA_CAP_PHAT) {
            throw new RuntimeException(" Vật tư của phiếu sửa chữa này đã được xuất kho thực tế!");
        }
        List<WorkOrderConsumable> requestedConsumables = workOrderConsumableRepository.findByWorkOrder(workOrder);
        for (WorkOrderConsumable req : requestedConsumables) {
            Long materialId = req.getMaterial().getId();

            if (approvedConsumableIds != null && approvedConsumableIds.contains(materialId)) {
                ConsumableMaterial material = req.getMaterial();
                int qtyToExport = req.getQuantity();

                int currentStock = consumableTxRepository.getStockQuantity(material.getId());

                if (currentStock < qtyToExport) {
                    throw new RuntimeException(" Kho không đủ số lượng cho vật tư tiêu hao: " + material.getName()
                            + " (Hiện tồn: " + currentStock + ", Yêu cầu: " + qtyToExport + ")");
                }
                //

                // Ghi nhận Transaction Xuất kho thực tế
                ConsumableTransaction tx = new ConsumableTransaction();
                tx.setMaterial(material);
                tx.setType(TransactionType.EXPORT);
                tx.setQuantity(qtyToExport);
                tx.setCreatedBy(staff);
                tx.setCreatedAt(LocalDateTime.now());
                consumableTxRepository.save(tx);
            }
        }

        // 4. Duyệt danh sách Phụ tùng thay thế (Chỉ xử lý những ID được Thủ kho tick)
        List<WorkOrderReplacement> requestedReplacements = workOrderReplacementRepository.findByWorkOrder(workOrder);
        for (WorkOrderReplacement req : requestedReplacements) {
            Long materialId = req.getMaterial().getId();

            // Nếu nằm trong danh sách được tick chọn
            if (approvedReplacementIds != null && approvedReplacementIds.contains(materialId)) {
                ReplacementMaterial material = req.getMaterial();
                int qtyToExport = req.getQuantity();

                int currentStock = replacementTxRepository.getStockQuantity(material.getId());

                if (currentStock < qtyToExport) {
                    throw new RuntimeException("❌ Kho không đủ số lượng cho phụ tùng thay thế: " + material.getName()
                            + " (Hiện tồn: " + currentStock + ", Yêu cầu: " + qtyToExport + ")");
                }

                // Ghi nhận Transaction Xuất kho thực tế
                ReplacementTransaction tx = new ReplacementTransaction();
                tx.setMaterial(material);
                tx.setType(TransactionType.EXPORT);
                tx.setQuantity(qtyToExport);
                tx.setCreatedBy(staff);
                tx.setCreatedAt(LocalDateTime.now());
                replacementTxRepository.save(tx);
            }
        }

        // 5. Cập nhật trạng thái tổng thể của phiếu sửa chữa sang ĐÃ CẤP PHAT
        workOrder.setMaterialStatus(MaterialStatus.DA_CAP_PHAT);
        workOrderRepository.save(workOrder);
    }
}