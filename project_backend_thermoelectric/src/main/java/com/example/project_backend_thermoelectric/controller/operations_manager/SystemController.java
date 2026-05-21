package com.example.project_backend_thermoelectric.controller.operations_manager;


import com.example.project_backend_thermoelectric.dto.operations_manager.detail.EquipmentBySystemDto;
import com.example.project_backend_thermoelectric.dto.operations_manager.request.EquipmentRequestDto;
import com.example.project_backend_thermoelectric.dto.operations_manager.request.SystemRequestDto;
import com.example.project_backend_thermoelectric.entity.Equipment;
import com.example.project_backend_thermoelectric.entity.SystemEntity;
import com.example.project_backend_thermoelectric.service.operations_manager.ISystemEntityService;
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
@RequestMapping("/api/system-equipments")
public class SystemController {
    @Autowired
    private ISystemEntityService systemService;


    @GetMapping
    public ResponseEntity<List<SystemEntity>> showList() {
        List<SystemEntity> systemEntityList = systemService.findAll();
        if (systemEntityList.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(systemEntityList, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> save(@Valid @RequestBody SystemRequestDto dto) {

        SystemEntity saved = systemService.add(dto);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);

    }

    @GetMapping("{id}/equipments")
    public ResponseEntity<?> detail(@PathVariable Long id,
                                    @RequestParam(defaultValue = "0")int page,
                                    @RequestParam(defaultValue = "")String name,
                                    @RequestParam(defaultValue = "")String code,
                                    @RequestParam(defaultValue = "")String domain) {
        Pageable pageable = PageRequest.of(page,3);
        Page<EquipmentBySystemDto> equipmentList = systemService.getEquipmentsBySystem(id,name,code,domain,pageable);
        return new ResponseEntity<>(equipmentList, HttpStatus.OK);

    }

    @PostMapping("{id}/equipments")
    public ResponseEntity<?> saveEquipment(@PathVariable Long id,
                                           @Valid @RequestBody EquipmentRequestDto dto) {

        Equipment saved = systemService.addEquipmentBySystemId(id, dto);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

}
