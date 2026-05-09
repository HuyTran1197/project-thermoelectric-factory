package com.example.project_backend_thermoelectric.controller.operations_manager.equipment;

import com.example.project_backend_thermoelectric.dto.operations_manager.request.EquipmentTypeRequestDto;
import com.example.project_backend_thermoelectric.dto.operations_manager.response.EquipmentTypeDto;
import com.example.project_backend_thermoelectric.entity.EquipmentType;
import com.example.project_backend_thermoelectric.service.operations_manager.equipment.IEquipmentService;
import com.example.project_backend_thermoelectric.service.operations_manager.equipment.IEquipmentTypeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/equipment-types")
public class EquipmentTypeController {
    @Autowired
    private IEquipmentTypeService equipmentTypeService;
    @Autowired
    private IEquipmentService equipmentService;

    @GetMapping
    public ResponseEntity<Page<EquipmentTypeDto>> search(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "") String name,
            @RequestParam(defaultValue = "") String domain
    ){
        Pageable pageable = PageRequest.of(page, 3);
        Page<EquipmentTypeDto> result = equipmentTypeService.searchEquipmentTypeDto(name, domain, pageable);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/list")
    public ResponseEntity<List<EquipmentType>> getList(){
        return new ResponseEntity<>(equipmentTypeService.getAll(),HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> save (@Valid @RequestBody EquipmentTypeRequestDto dto){
        EquipmentType saved = equipmentTypeService.add(dto);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    @GetMapping("/{typeId}/equipments")
    public ResponseEntity<?> getEquipmentsByType(
            @PathVariable Long typeId
    ){
        return new ResponseEntity<>(equipmentService.getEquipmentsByType(typeId), HttpStatus.OK);
    }

    @GetMapping("{typeId}/equipments/{equipmentId}/detail")
    public ResponseEntity<?> detail(
            @PathVariable Long typeId,
            @PathVariable Long equipmentId
    ){
        return ResponseEntity.ok(
                equipmentTypeService.detail(typeId, equipmentId)
        );
    }

}
