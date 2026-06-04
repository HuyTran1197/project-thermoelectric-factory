package com.example.project_backend_thermoelectric.repository.operations_manager.equipment;

import com.example.project_backend_thermoelectric.entity.EquipmentParameter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface IEquipmentParameterRepo extends JpaRepository<EquipmentParameter,Long> {

    List<EquipmentParameter> findByEquipmentId(Long equipmentId);

    @Transactional
    @Modifying
    @Query("delete from EquipmentParameter ep where ep.equipment.id = :equipmentId")
    void deleteByEquipmentId(Long equipmentId);
}
