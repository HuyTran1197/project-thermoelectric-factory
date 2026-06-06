package com.example.project_backend_thermoelectric.controller.personnel_manager;

import com.example.project_backend_thermoelectric.dto.personnel_manager.PositionDto;
import com.example.project_backend_thermoelectric.entity.Position;
import com.example.project_backend_thermoelectric.service.personnel_manager.IPositionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/positions")
@CrossOrigin("*")
public class PositionController {
    @Autowired
    private IPositionService positionService;

    @PostMapping
    public Position createPosition(@RequestBody PositionDto request) {
        Position position = new Position();
        position.setName(request.getName());

        return positionService.createPosition(position);
    }

    @GetMapping
    public List<Position> getAllPositions() {
        return positionService.getAllPositions();
    }

    @GetMapping("/search")
    public Page<Position> searchPositions(
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        return positionService.searchPositions(keyword, page, size);
    }

    @GetMapping("/{id}")
    public Position getPositionById(@PathVariable Long id) {
        return positionService.getPositionById(id);
    }

    @PutMapping("/{id}")
    public Position updatePosition(
            @PathVariable Long id,
            @RequestBody PositionDto request
    ) {
        Position position = new Position();
        position.setName(request.getName());

        return positionService.updatePosition(id, position);
    }

    @DeleteMapping("/{id}")
    public String deletePosition(@PathVariable Long id) {
        positionService.deletePosition(id);
        return "Delete position successfully";
    }
}
