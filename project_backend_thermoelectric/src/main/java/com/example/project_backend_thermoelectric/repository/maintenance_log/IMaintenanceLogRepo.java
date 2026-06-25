package com.example.project_backend_thermoelectric.repository.maintenance_log;

import com.example.project_backend_thermoelectric.entity.MaintenanceLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IMaintenanceLogRepo extends JpaRepository<MaintenanceLog,Long> {
    @Query("""
        SELECT m FROM MaintenanceLog m
        JOIN m.equipment e
        WHERE (:equipmentName IS NULL OR :equipmentName = ''
               OR LOWER(e.name) LIKE LOWER(CONCAT('%', :equipmentName, '%')))
    """)
    Page<MaintenanceLog> search(
            @Param("equipmentName") String equipmentName,
            Pageable pageable
    );
}
