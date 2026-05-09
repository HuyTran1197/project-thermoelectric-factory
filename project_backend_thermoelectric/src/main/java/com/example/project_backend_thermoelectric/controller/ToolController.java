package com.example.project_backend_thermoelectric.controller;

import com.example.project_backend_thermoelectric.entity.Tool;
import com.example.project_backend_thermoelectric.service.ToolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/tools")
@CrossOrigin("*")
public class ToolController {

    @Autowired
    private ToolService toolService;

    @GetMapping
    public List<Tool> getAllTools() {
        return toolService.getAllTools();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Tool> getToolById(@PathVariable Long id) {
        return toolService.getToolById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Tool createTool(@RequestBody Tool tool) {
        return toolService.saveTool(tool);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Tool> updateTool(@PathVariable Long id, @RequestBody Tool toolDetails) {
        return toolService.getToolById(id)
                .map(tool -> {
                    tool.setName(toolDetails.getName());
                    tool.setCode(toolDetails.getCode());
                    tool.setType(toolDetails.getType());
                    tool.setTotalQuantity(toolDetails.getTotalQuantity());
                    tool.setAvailableQuantity(toolDetails.getAvailableQuantity());
                    tool.setLocation(toolDetails.getLocation());
                    tool.setDescription(toolDetails.getDescription());
                    return ResponseEntity.ok(toolService.saveTool(tool));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTool(@PathVariable Long id) {
        toolService.deleteTool(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/import")
    public ResponseEntity<Tool> importTool(@RequestBody Map<String, Object> payload) {
        String code = (String) payload.get("code");
        Integer quantity = (Integer) payload.get("quantity");
        return ResponseEntity.ok(toolService.importTool(code, quantity));
    }
}
