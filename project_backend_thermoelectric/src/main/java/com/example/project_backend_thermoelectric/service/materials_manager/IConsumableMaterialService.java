package com.example.project_backend_thermoelectric.service.materials_manager;

import com.example.project_backend_thermoelectric.entity.ConsumableMaterial;

import java.util.List;

public interface IConsumableMaterialService {
    ConsumableMaterial findById(long id);
    List<ConsumableMaterial> findAll();
    ConsumableMaterial add(ConsumableMaterial consumableMaterial);
    boolean delete(long id);
    ConsumableMaterial update(ConsumableMaterial consumableMaterial);
    List<ConsumableMaterial> findByNameOrCode(String keyword);
}
