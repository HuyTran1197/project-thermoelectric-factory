package com.example.project_backend_thermoelectric.repository.operations_manager.equipment;

import com.example.project_backend_thermoelectric.entity.EquipmentParameter;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IEquipmentParameterRepo extends JpaRepository<EquipmentParameter,Long> {
}
