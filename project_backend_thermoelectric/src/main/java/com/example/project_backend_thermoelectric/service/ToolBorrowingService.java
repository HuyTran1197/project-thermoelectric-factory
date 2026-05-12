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
        return borrowingRepository.findAll();
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
    public ToolBorrowing returnTool(Long borrowingId) {
        ToolBorrowing borrowing = borrowingRepository.findById(borrowingId)
                .orElseThrow(() -> new RuntimeException("Borrowing record not found"));

        if ("RETURNED".equals(borrowing.getStatus())) {
            throw new RuntimeException("This tool has already been returned");
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

        return borrowingRepository.save(existingBorrowing);
    }
}
