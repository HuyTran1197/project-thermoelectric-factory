package com.example.project_backend_thermoelectric.controller.operations_manager.equipment;


import com.example.project_backend_thermoelectric.dto.operations_manager.request.EquipmentRequestDto;
import com.example.project_backend_thermoelectric.dto.operations_manager.response.EquipmentDto;
import com.example.project_backend_thermoelectric.entity.Equipment;
import com.example.project_backend_thermoelectric.service.operations_manager.equipment.IEquipmentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("*")
@RestController
@RequestMapping("/equipments")
public class EquipmentController {
    @Autowired
    private IEquipmentService equipmentService;

    @GetMapping
    public ResponseEntity<Page<EquipmentDto>> showList(@RequestParam(defaultValue = "0") int page,
                                                       @RequestParam(defaultValue = "") String name,
                                                       @RequestParam(defaultValue = "") String code,
                                                       @RequestParam(defaultValue = "") String status) {
        Pageable pageable = PageRequest.of(page, 5);
        Page<EquipmentDto> equipmentTypeDtos = equipmentService.searchEquipmentDto(name, code, status, pageable);
        return ResponseEntity.ok(equipmentTypeDtos);
    }

    @PostMapping
    public ResponseEntity<?> save(@Valid @RequestBody EquipmentRequestDto dto) {

        Equipment saved = equipmentService.add(dto);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id){
        return new ResponseEntity<>(equipmentService.findById(id),HttpStatus.OK);
    }
    @PutMapping("/{id}")
    public ResponseEntity<?> editEquipment(@PathVariable Long id,
                                           @Valid @RequestBody EquipmentRequestDto dto) {

        Equipment updated = equipmentService.edit(id, dto);
        return new ResponseEntity<>(updated, HttpStatus.OK);

    }

}
