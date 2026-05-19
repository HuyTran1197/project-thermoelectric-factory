package com.example.project_backend_thermoelectric.service.materials_manager;

import com.example.project_backend_thermoelectric.entity.ReplacementMaterial;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


import java.util.List;

public interface IReplacementMaterialService {
    ReplacementMaterial findById(long id);
    List<ReplacementMaterial> findAll();
    ReplacementMaterial add(ReplacementMaterial replacementMaterial);
    boolean delete(long id);
    ReplacementMaterial update(ReplacementMaterial replacementMaterial);
    Page<ReplacementMaterial> findByNameOrCode(String code, String name, Pageable pageable);
}
