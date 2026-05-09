package com.example.project_backend_thermoelectric.service;

import com.example.project_backend_thermoelectric.entity.Tool;
import com.example.project_backend_thermoelectric.repository.ToolRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ToolService {

    @Autowired
    private ToolRepository toolRepository;

    public List<Tool> getAllTools() {
        return toolRepository.findAll();
    }

    public Optional<Tool> getToolById(Long id) {
        return toolRepository.findById(id);
    }

    @Transactional
    public Tool saveTool(Tool tool) {
        if (tool.getId() == null) {
            // New tool
            if (tool.getAvailableQuantity() == null) {
                tool.setAvailableQuantity(tool.getTotalQuantity());
            }
        } else {
            // Updating tool
            Tool existingTool = toolRepository.findById(tool.getId())
                    .orElseThrow(() -> new RuntimeException("Tool not found"));
            
            // Re-calculate available quantity: (New Total - Old Total) + Old Available
            int totalDiff = tool.getTotalQuantity() - existingTool.getTotalQuantity();
            tool.setAvailableQuantity(existingTool.getAvailableQuantity() + totalDiff);
            
            if (tool.getAvailableQuantity() < 0) {
                throw new RuntimeException("Available quantity cannot be negative after update");
            }
        }
        return toolRepository.save(tool);
    }

    @Transactional
    public void deleteTool(Long id) {
        toolRepository.deleteById(id);
    }

    @Transactional
    public Tool importTool(String code, Integer quantity) {
        Optional<Tool> toolOpt = toolRepository.findByCode(code);
        if (toolOpt.isPresent()) {
            Tool tool = toolOpt.get();
            tool.setTotalQuantity(tool.getTotalQuantity() + quantity);
            tool.setAvailableQuantity(tool.getAvailableQuantity() + quantity);
            return toolRepository.save(tool);
        } else {
            throw new RuntimeException("Tool not found with code: " + code);
        }
    }
}
