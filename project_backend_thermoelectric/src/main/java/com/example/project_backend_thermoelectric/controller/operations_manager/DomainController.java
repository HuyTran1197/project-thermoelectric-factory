package com.example.project_backend_thermoelectric.controller.operations_manager;

import com.example.project_backend_thermoelectric.entity.Domain;
import com.example.project_backend_thermoelectric.service.operations_manager.IDomainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/domains")
public class DomainController {
    @Autowired
    private IDomainService domainService;

    @GetMapping
    public ResponseEntity<List<Domain>> showList(){
        return new ResponseEntity<>(domainService.getList(), HttpStatus.OK);
    }
}
