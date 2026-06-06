package com.example.project_backend_thermoelectric.service.personnel_manager;


import com.example.project_backend_thermoelectric.dto.personnel_manager.WorkPositionDto;
import com.example.project_backend_thermoelectric.entity.WorkPosition;
import com.example.project_backend_thermoelectric.repository.personnel_manager.IWorkPositionRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class WorkPositionService implements IWorkPositionService {
    @Autowired
    private IWorkPositionRepo repo;

    public Page<WorkPositionDto> search(String keyword, int page, int size) {
        return repo.findByNameContainingIgnoreCase(keyword != null ? keyword : "", PageRequest.of(page, size))
                .map(wp -> new WorkPositionDto(wp.getId(), wp.getName()));
    }
    public List<WorkPositionDto> getAll() {
        return repo.findAll().stream().map(wp -> new WorkPositionDto(wp.getId(), wp.getName())).collect(Collectors.toList());
    }

    @Transactional
    public WorkPositionDto create(String name) {
        WorkPosition wp = new WorkPosition();
        wp.setName(name);
        wp = repo.save(wp);
        return new WorkPositionDto(wp.getId(), wp.getName());
    }

    @Transactional
    public WorkPositionDto update(Long id, String name) {
        WorkPosition wp = repo.findById(id).orElseThrow(() -> new RuntimeException("WorkPosition không tồn tại"));
        wp.setName(name);
        wp = repo.save(wp);
        return new WorkPositionDto(wp.getId(), wp.getName());
    }

    @Transactional
    public void delete(Long id) {
        if(!repo.existsById(id)) throw new RuntimeException("WorkPosition không tồn tại");
        repo.deleteById(id);
    }
}
