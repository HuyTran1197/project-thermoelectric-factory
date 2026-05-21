package com.example.project_backend_thermoelectric.service.materials_manager;

import com.example.project_backend_thermoelectric.entity.ConsumableMaterial;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IConsumableMaterialService {
    ConsumableMaterial findById(long id);
    List<ConsumableMaterial> findAll();
    ConsumableMaterial add(ConsumableMaterial consumableMaterial);
    boolean delete(long id);
    ConsumableMaterial update(ConsumableMaterial consumableMaterial);
    Page<ConsumableMaterial> findByNameOrCode(String code, String name, Pageable pageable);
}
