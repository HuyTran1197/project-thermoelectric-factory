package com.example.project_backend_thermoelectric.controller;

import com.example.project_backend_thermoelectric.entity.ToolBorrowing;
import com.example.project_backend_thermoelectric.service.ToolBorrowingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tool-borrowings")
@CrossOrigin("*")
public class ToolBorrowingController {

    @Autowired
    private ToolBorrowingService borrowingService;

    @GetMapping
    public List<ToolBorrowing> getAllBorrowings() {
        return borrowingService.getAllBorrowings();
    }

    @PostMapping("/borrow")
    public ResponseEntity<?> borrowTool(@RequestBody ToolBorrowing borrowing) {
        try {
            return ResponseEntity.ok(borrowingService.borrowTool(borrowing));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/return/{id}")
    public ResponseEntity<?> returnTool(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(borrowingService.returnTool(id));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
