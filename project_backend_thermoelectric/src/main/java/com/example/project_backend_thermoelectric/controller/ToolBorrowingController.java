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
    public List<ToolBorrowing> getAllBorrowings(

            @RequestParam(required = false)
            String toolCode,

            @RequestParam(required = false)
            String employeeSearch,

            @RequestParam(required = false)
            String status
    ) {

        return borrowingService.searchBorrowings(
                toolCode,
                employeeSearch,
                status
        );
    }

    @GetMapping("/search")
    public ResponseEntity<List<ToolBorrowing>> searchBorrowings(
            @RequestParam(required = false) String toolCode,
            @RequestParam(required = false) String employeeSearch,
            @RequestParam(required = false) String status) {
        return ResponseEntity.ok(borrowingService.searchBorrowings(toolCode, employeeSearch, status));
    }

    @PostMapping("/batch")
    public ResponseEntity<?> borrowToolsBatch(@RequestBody List<ToolBorrowing> borrowings) {
        try {
            return ResponseEntity.ok(borrowingService.borrowToolsBatch(borrowings));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/confirm-returns")
    public ResponseEntity<?> confirmReturns(@RequestBody List<Long> borrowingIds) {
        try {
            return ResponseEntity.ok(borrowingService.confirmReturns(borrowingIds));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/borrow")
    public ResponseEntity<?> borrowTool(@RequestBody ToolBorrowing borrowing) {
        try {
            return ResponseEntity.ok(borrowingService.borrowTool(borrowing));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/confirm-borrow/{id}")
    public ResponseEntity<?> confirmBorrowing(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(borrowingService.confirmBorrowing(id));
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

    @PutMapping("/{id}")
    public ResponseEntity<?> updateBorrowing(@PathVariable Long id, @RequestBody ToolBorrowing borrowing) {
        try {
            return ResponseEntity.ok(borrowingService.updateBorrowing(id, borrowing));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
