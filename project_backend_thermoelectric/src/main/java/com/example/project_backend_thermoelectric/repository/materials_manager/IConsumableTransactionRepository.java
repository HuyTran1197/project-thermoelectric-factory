package com.example.project_backend_thermoelectric.repository.materials_manager;

import com.example.project_backend_thermoelectric.entity.ConsumableTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IConsumableTransactionRepository extends JpaRepository<ConsumableTransaction,Long> {
}
