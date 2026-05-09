package com.example.project_backend_thermoelectric.controller.operations_manager.equipment;

import com.example.project_backend_thermoelectric.dto.operations_manager.response.ParameterDefinitionDto;
import com.example.project_backend_thermoelectric.entity.ParameterDefinition;
import com.example.project_backend_thermoelectric.service.operations_manager.equipment.IParameterDefinitionService;
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
@RequestMapping("/api/param-definitions")
public class ParamDefinitionController {
    @Autowired
    private IParameterDefinitionService parameterDefinitionService;

    @GetMapping
    public ResponseEntity<Page<ParameterDefinitionDto>> showList(@RequestParam(defaultValue = "0")int page,
                                                                 @RequestParam(defaultValue = "")String name){
        Pageable pageable = PageRequest.of(page,5);
        Page<ParameterDefinitionDto> parameterDefinitionDtos =
                parameterDefinitionService.searchAllParameterDefinition(name,pageable);
        return ResponseEntity.ok(parameterDefinitionDtos);
    }


    @PostMapping
    public ResponseEntity<?> save(@Valid @RequestBody ParameterDefinition parameterDefinition){
        ParameterDefinition saved = parameterDefinitionService.add(parameterDefinition);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }
    @PutMapping("/{id}")
    public ResponseEntity<?> editParam(@PathVariable Long id,
                                       @Valid @RequestBody ParameterDefinition parameterDefinition){
        ParameterDefinition updated = parameterDefinitionService.edit(id,parameterDefinition);
        return new ResponseEntity<>(updated, HttpStatus.CREATED);
    }
}
