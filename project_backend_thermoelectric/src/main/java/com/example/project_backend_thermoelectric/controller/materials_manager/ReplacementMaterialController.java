package com.example.project_backend_thermoelectric.controller.materials_manager;

import com.example.project_backend_thermoelectric.entity.ReplacementMaterial;
import com.example.project_backend_thermoelectric.service.materials_manager.IReplacementMaterialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/replacement-materials")
@CrossOrigin("*")
public class ReplacementMaterialController {
    @Autowired
    IReplacementMaterialService replacementMaterialService;
    @GetMapping
    public ResponseEntity<Page<ReplacementMaterial>> getAllOrSearch(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(required = false) String code,
            @RequestParam(required = false) String name) {

        Pageable pageable = PageRequest.of(page, 5);
        Page<ReplacementMaterial> materials = replacementMaterialService.findByNameOrCode(code,name, pageable);
        return ResponseEntity.ok(materials);
    }
    @GetMapping("/list")
    public ResponseEntity<List<ReplacementMaterial>> getAllWithoutPage() {
        List<ReplacementMaterial> list = replacementMaterialService.findAll();
        return ResponseEntity.ok(list);
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
