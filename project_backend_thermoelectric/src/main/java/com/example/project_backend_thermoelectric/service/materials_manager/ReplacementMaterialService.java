package com.example.project_backend_thermoelectric.service.materials_manager;

import com.example.project_backend_thermoelectric.entity.ReplacementMaterial;
import com.example.project_backend_thermoelectric.repository.materials_manager.IReplacementMaterialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReplacementMaterialService implements IReplacementMaterialService {
    @Autowired
    private IReplacementMaterialRepository replacementMaterialRepository;
    @Override
    public ReplacementMaterial findById(long id) {
        return replacementMaterialRepository.findById(id).orElse(null);
    }

    @Override
    public List<ReplacementMaterial> findAll() {
        return replacementMaterialRepository.findAll();
    }

    @Override
    public ReplacementMaterial add(ReplacementMaterial replacementMaterial) {
        return replacementMaterialRepository.save(replacementMaterial);
    }

    @Override
    public boolean delete(long id) {
        try {
            replacementMaterialRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public ReplacementMaterial update(ReplacementMaterial replacementMaterial) {
        return  replacementMaterialRepository.save(replacementMaterial);
    }

    @Override
    public List<ReplacementMaterial> findByNameOrCode(String keyword) {
        return replacementMaterialRepository.searchReplacementMaterial(keyword);
    }
}
