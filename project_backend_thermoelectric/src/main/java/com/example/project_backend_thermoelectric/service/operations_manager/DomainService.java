package com.example.project_backend_thermoelectric.service.operations_manager;

import com.example.project_backend_thermoelectric.entity.Domain;
import com.example.project_backend_thermoelectric.repository.operations_manager.IDomainRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class DomainService implements IDomainService{
    @Autowired
    private IDomainRepo domainRepo;

    @Override
    public List<Domain> getList() {
        return domainRepo.findAll();
    }
}
