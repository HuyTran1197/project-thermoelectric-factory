package com.example.project_backend_thermoelectric.service.impl;

import com.example.project_backend_thermoelectric.entity.ReplacementMaterial;

import java.util.List;

public interface IReplacementMaterialService {
    ReplacementMaterial findById(long id);
    List<ReplacementMaterial> findAll();
    ReplacementMaterial add(ReplacementMaterial replacementMaterial);
    boolean delete(long id);
    ReplacementMaterial update(ReplacementMaterial replacementMaterial);
    List<ReplacementMaterial> findByNameOrCode(String keyword);
}
