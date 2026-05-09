package com.example.project_backend_thermoelectric.controller;

import com.example.project_backend_thermoelectric.entity.ReplacementMaterial;
import com.example.project_backend_thermoelectric.service.impl.IReplacementMaterialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/replacement-materials")
@CrossOrigin("*")
public class ReplacementMaterialController {
    @Autowired
    IReplacementMaterialService replacementMaterialService;
    @GetMapping
    public ResponseEntity<List<ReplacementMaterial>> getAllOrSearch(
            @RequestParam(required = false) String keyword) {

        List<ReplacementMaterial> materials;
        if (keyword != null && !keyword.isBlank()) {
            materials = replacementMaterialService.findByNameOrCode(keyword);
        } else {
            materials = replacementMaterialService.findAll();
        }
        return ResponseEntity.ok(materials);
    }

    @PostMapping
    public ResponseEntity<ReplacementMaterial> save(@RequestBody ReplacementMaterial replacementMaterial) {
        ReplacementMaterial saved = replacementMaterialService.add(replacementMaterial);
        return ResponseEntity.status(201).body(saved);
    }
    @GetMapping("/{id}")
    public ResponseEntity<ReplacementMaterial> getById(@PathVariable Long id) {
        ReplacementMaterial replacementMaterial = replacementMaterialService.findById(id);
        return ResponseEntity.ok(replacementMaterial);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        replacementMaterialService.delete(id);
        return ResponseEntity.noContent().build();
    }
    @PutMapping("/{id}")
    public ResponseEntity<ReplacementMaterial> update(@PathVariable Long id, @RequestBody ReplacementMaterial replacementMaterial) {
        replacementMaterial.setId(id);
        ReplacementMaterial saved = replacementMaterialService.update(replacementMaterial);
        return ResponseEntity.ok(saved);
    }
}
