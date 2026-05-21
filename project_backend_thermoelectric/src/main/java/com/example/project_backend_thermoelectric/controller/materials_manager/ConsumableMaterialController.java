package com.example.project_backend_thermoelectric.controller.materials_manager;

import com.example.project_backend_thermoelectric.entity.ConsumableMaterial;
import com.example.project_backend_thermoelectric.service.materials_manager.IConsumableMaterialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/consumable-materials")
@CrossOrigin("*")
public class ConsumableMaterialController {
    @Autowired
    private IConsumableMaterialService consumableMaterialService;
    @GetMapping
    public ResponseEntity<Page<ConsumableMaterial>> getAllOrSearch(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(required = false) String code,
            @RequestParam(required = false) String name) {

        Pageable pageable = PageRequest.of(page, 5);
        Page<ConsumableMaterial> consumableMaterials = consumableMaterialService.findByNameOrCode(code,name, pageable);
        return ResponseEntity.ok(consumableMaterials);
    }
    @GetMapping("/list")
    public ResponseEntity<List<ConsumableMaterial>> getAllWithoutPage() {
        List<ConsumableMaterial> list = consumableMaterialService.findAll();
        return ResponseEntity.ok(list);
    }

    @PostMapping
    public ResponseEntity<ConsumableMaterial> save(@RequestBody ConsumableMaterial consumableMaterial) {
        ConsumableMaterial saved = consumableMaterialService.add(consumableMaterial);
        return ResponseEntity.status(201).body(saved);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        consumableMaterialService.delete(id);
        return ResponseEntity.noContent().build();
    }
    @GetMapping("/{id}")
    public ResponseEntity<ConsumableMaterial> getById(@PathVariable Long id) {
        ConsumableMaterial consumableMaterial = consumableMaterialService.findById(id);
        return ResponseEntity.ok(consumableMaterial);
    }
    @PutMapping("/{id}")
    public ResponseEntity<ConsumableMaterial> update(@PathVariable Long id, @RequestBody ConsumableMaterial consumableMaterial) {
        consumableMaterial.setId(id);
        ConsumableMaterial saved = consumableMaterialService.update(consumableMaterial);
        return ResponseEntity.ok(saved);
    }
}
