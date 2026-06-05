package com.example.project_backend_thermoelectric.controller.personnel_manager;

import com.example.project_backend_thermoelectric.dto.personnel_manager.WorkPositionDto;
import com.example.project_backend_thermoelectric.service.personnel_manager.IWorkPositionService;
import com.example.project_backend_thermoelectric.service.personnel_manager.WorkPositionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/work_positions")
@CrossOrigin("*")
public class WorkPositionController {
    @Autowired
    private WorkPositionService service;

    @GetMapping
    public Page<WorkPositionDto> search(@RequestParam(defaultValue="") String keyword,
                                        @RequestParam(defaultValue="0") int page,
                                        @RequestParam(defaultValue="5") int size) {
        return service.search(keyword, page, size);
    }

    @GetMapping("/all")
    public List<WorkPositionDto> getAll() {
        return service.getAll();
    }

    @PostMapping
    public WorkPositionDto create(@RequestBody WorkPositionDto dto) {
        return service.create(dto.getName());
    }

    @PutMapping("/{id}")
    public WorkPositionDto update(@PathVariable Long id, @RequestBody WorkPositionDto dto) {
        return service.update(id, dto.getName());
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id) {
        service.delete(id);
        return "Xóa vị trí thành công";
    }
}
