package com.example.project_backend_thermoelectric.service;

import com.example.project_backend_thermoelectric.entity.Tool;
import com.example.project_backend_thermoelectric.entity.ToolBorrowing;
import com.example.project_backend_thermoelectric.repository.tool.ToolBorrowingRepository;
import com.example.project_backend_thermoelectric.repository.tool.ToolRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ToolBorrowingService {

    @Autowired
    private ToolBorrowingRepository borrowingRepository;

    @Autowired
    private ToolRepository toolRepository;

    public List<ToolBorrowing> getAllBorrowings() {
        return borrowingRepository.searchBorrowings(null, null, null);
    }

    public List<ToolBorrowing> searchBorrowings(String toolCode, String employeeSearch, String status) {
        String tc = (toolCode != null && !toolCode.trim().isEmpty()) ? toolCode.trim() : null;
        String es = (employeeSearch != null && !employeeSearch.trim().isEmpty()) ? employeeSearch.trim() : null;
        String st = (status != null && !status.trim().isEmpty()) ? status.trim() : null;
        
        // Nếu tất cả các tham số đều null, trả về danh sách tất cả (hoặc tùy theo logic mong muốn)
        // Ở đây repository đã xử lý IS NULL nên nếu tất cả null nó sẽ trả về tất cả.
        return borrowingRepository.searchBorrowings(tc, es, st);
    }

    @Transactional
    public List<ToolBorrowing> borrowToolsBatch(List<ToolBorrowing> borrowings) {
        if (borrowings == null || borrowings.isEmpty()) {
            throw new RuntimeException("Danh sách mượn trống");
        }

        LocalDateTime now = LocalDateTime.now();
        for (ToolBorrowing borrowing : borrowings) {
            if (borrowing.getTool() == null || borrowing.getTool().getId() == null) {
                throw new RuntimeException("Thông tin công cụ bị thiếu");
            }
            if (borrowing.getEmployee() == null || borrowing.getEmployee().getId() == null) {
                throw new RuntimeException("Thông tin nhân viên bị thiếu");
            }
            if (borrowing.getQuantity() == null || borrowing.getQuantity() <= 0) {
                throw new RuntimeException("Số lượng không hợp lệ cho công cụ: " + borrowing.getTool().getId());
            }

            Tool tool = toolRepository.findById(borrowing.getTool().getId())
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy công cụ ID: " + borrowing.getTool().getId()));

            if (tool.getAvailableQuantity() < borrowing.getQuantity()) {
                throw new RuntimeException("Không đủ số lượng trong kho cho: " + tool.getName() + ". Hiện có: " + tool.getAvailableQuantity());
            }

            tool.setAvailableQuantity(tool.getAvailableQuantity() - borrowing.getQuantity());
            toolRepository.save(tool);

            if (borrowing.getBorrowDate() == null) {
                borrowing.setBorrowDate(now);
            }
            borrowing.setStatus("WAITING"); // Chờ duyệt theo yêu cầu
        }
        return borrowingRepository.saveAll(borrowings);
    }

    @Transactional
    public List<ToolBorrowing> confirmReturns(List<Long> borrowingIds) {
        if (borrowingIds == null || borrowingIds.isEmpty()) {
            throw new RuntimeException("Danh sách ID trả đồ trống");
        }

        List<ToolBorrowing> borrowings = borrowingRepository.findAllById(borrowingIds);
        LocalDateTime now = LocalDateTime.now();

        for (ToolBorrowing borrowing : borrowings) {
            if ("RETURNED".equals(borrowing.getStatus())) {
                continue; // Bỏ qua nếu đã trả rồi
            }

            Tool tool = borrowing.getTool();
            tool.setAvailableQuantity(tool.getAvailableQuantity() + borrowing.getQuantity());
            toolRepository.save(tool);

            borrowing.setReturnDate(now);
            borrowing.setStatus("RETURNED");
        }
        return borrowingRepository.saveAll(borrowings);
    }

    @Transactional
    public ToolBorrowing borrowTool(ToolBorrowing borrowing) {
        if (borrowing.getTool() == null || borrowing.getTool().getId() == null) {
            throw new RuntimeException("Tool information is missing");
        }
        if (borrowing.getEmployee() == null || borrowing.getEmployee().getId() == null) {
            throw new RuntimeException("Employee information is missing");
        }
        if (borrowing.getQuantity() == null || borrowing.getQuantity() <= 0) {
            throw new RuntimeException("Invalid quantity");
        }

        Tool tool = toolRepository.findById(borrowing.getTool().getId())
                .orElseThrow(() -> new RuntimeException("Tool not found"));

        if (tool.getAvailableQuantity() < borrowing.getQuantity()) {
            throw new RuntimeException("Not enough tools available in stock. Current available: " + tool.getAvailableQuantity());
        }

        tool.setAvailableQuantity(tool.getAvailableQuantity() - borrowing.getQuantity());
        toolRepository.save(tool);

        if (borrowing.getBorrowDate() == null) {
            borrowing.setBorrowDate(LocalDateTime.now());
        }
        borrowing.setStatus("BORROWED");
        return borrowingRepository.save(borrowing);
    }

    @Transactional
    public ToolBorrowing confirmBorrowing(Long id) {
        ToolBorrowing borrowing = borrowingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy bản ghi mượn đồ"));

        if (!"WAITING".equals(borrowing.getStatus())) {
            throw new RuntimeException("Chỉ có thể xác nhận cho mượn khi đang ở trạng thái Chờ duyệt (WAITING)");
        }

        borrowing.setStatus("BORROWED");
        return borrowingRepository.save(borrowing);
    }

    @Transactional
    public ToolBorrowing returnTool(Long borrowingId) {
        ToolBorrowing borrowing = borrowingRepository.findById(borrowingId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy bản ghi mượn đồ"));

        if (!"BORROWED".equals(borrowing.getStatus())) {
            throw new RuntimeException("Chỉ có thể trả đồ khi đang ở trạng thái Đang mượn (BORROWED)");
        }

        Tool tool = borrowing.getTool();
        tool.setAvailableQuantity(tool.getAvailableQuantity() + borrowing.getQuantity());
        toolRepository.save(tool);

        borrowing.setReturnDate(LocalDateTime.now());
        borrowing.setStatus("RETURNED");
        return borrowingRepository.save(borrowing);
    }

    @Transactional
    public ToolBorrowing updateBorrowing(Long id, ToolBorrowing updatedBorrowing) {
        ToolBorrowing existingBorrowing = borrowingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Borrowing record not found"));

        if ("RETURNED".equals(existingBorrowing.getStatus())) {
            throw new RuntimeException("Cannot edit a returned borrowing record");
        }

        // Handle quantity change
        if (updatedBorrowing.getQuantity() != null && !updatedBorrowing.getQuantity().equals(existingBorrowing.getQuantity())) {
            if (updatedBorrowing.getQuantity() <= 0) {
                throw new RuntimeException("Invalid quantity");
            }

            Tool tool = existingBorrowing.getTool();
            int diff = updatedBorrowing.getQuantity() - existingBorrowing.getQuantity();

            if (tool.getAvailableQuantity() < diff) {
                throw new RuntimeException("Not enough tools available in stock to increase borrowing quantity");
            }

            tool.setAvailableQuantity(tool.getAvailableQuantity() - diff);
            toolRepository.save(tool);
            existingBorrowing.setQuantity(updatedBorrowing.getQuantity());
        }

        if (updatedBorrowing.getNote() != null) {
            existingBorrowing.setNote(updatedBorrowing.getNote());
        }
        
        if (updatedBorrowing.getDueDate() != null) {
            existingBorrowing.setDueDate(updatedBorrowing.getDueDate());
        }

        if (updatedBorrowing.getStatus() != null) {
            existingBorrowing.setStatus(updatedBorrowing.getStatus());
        }

        return borrowingRepository.save(existingBorrowing);
    }
}
