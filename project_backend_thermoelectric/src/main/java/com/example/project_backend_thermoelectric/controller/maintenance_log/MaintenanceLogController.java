package com.example.project_backend_thermoelectric.controller.maintenance_log;

import com.example.project_backend_thermoelectric.dto.maintenance_log.CreateMaintenanceLogDto;
import com.example.project_backend_thermoelectric.dto.maintenance_log.MaintenanceLogDto;
import com.example.project_backend_thermoelectric.service.maintenance_log.IMaintenanceLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/maintenance-logs")
@RequiredArgsConstructor
@CrossOrigin("*")
public class MaintenanceLogController {
    private final IMaintenanceLogService service;

    // CREATE
    @PostMapping
    public ResponseEntity<MaintenanceLogDto> create(
            @RequestBody CreateMaintenanceLogDto dto
    ) {
        return ResponseEntity.ok(service.create(dto));
    }

    // SEARCH + PAGINATION
    @GetMapping
    public ResponseEntity<Page<MaintenanceLogDto>> search(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "") String equipmentName
    ) {

        Pageable pageable = PageRequest.of(page, size);

        return ResponseEntity.ok(
                service.search(equipmentName, pageable)
        );
    }
}
