package com.example.project_backend_thermoelectric.service.materials_manager;

import com.example.project_backend_thermoelectric.entity.ConsumableMaterial;
import com.example.project_backend_thermoelectric.repository.materials_manager.IConsumableMaterialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConsumableMaterialService implements IConsumableMaterialService {
    @Autowired
    private IConsumableMaterialRepository consumableMaterialRepository;
    @Override
    public ConsumableMaterial findById(long id) {
        return consumableMaterialRepository.findById(id).orElse(null);
    }

    @Override
    public List<ConsumableMaterial> findAll() {
        return consumableMaterialRepository.findAll();
    }

    @Override
    public ConsumableMaterial add(ConsumableMaterial consumableMaterial) {
        if (consumableMaterialRepository.existsByCode(consumableMaterial.getCode())) {
            throw new IllegalArgumentException("Mã vật tư đã tồn tại!");
        }
       return consumableMaterialRepository.save(consumableMaterial);
    }

    @Override
    public boolean delete(long id) {
        try {
            consumableMaterialRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public ConsumableMaterial update(ConsumableMaterial consumableMaterial) {
        return consumableMaterialRepository.save(consumableMaterial);
    }

    @Override
    public Page<ConsumableMaterial> findByNameOrCode(String code, String name, Pageable pageable) {
        return consumableMaterialRepository.searchConsumableMaterial(code, name, pageable);
    }


}
