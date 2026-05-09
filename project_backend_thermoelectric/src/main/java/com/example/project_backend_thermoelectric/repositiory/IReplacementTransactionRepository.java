package com.example.project_backend_thermoelectric.repositiory;

import com.example.project_backend_thermoelectric.entity.ReplacementTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IReplacementTransactionRepository extends JpaRepository<ReplacementTransaction,Long> {
}
