package com.example.project_backend_thermoelectric.repositiory.operations_manager;

import com.example.project_backend_thermoelectric.entity.SystemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ISystemEntityRepo extends JpaRepository<SystemEntity,Long> {
    @Query("""
    SELECT COUNT(s) > 0
    FROM SystemEntity s
    WHERE LOWER(TRIM(s.name)) = LOWER(TRIM(:name))
""")
    boolean existsNameAndDescription(
            @Param("name") String name
    );

    @Query("SELECT COUNT(s) > 0 FROM SystemEntity s " +
            "WHERE s.id = :id")
    boolean existsSystem(@Param("id") Long id);

    @Query("SELECT COUNT(s) > 0 FROM SystemEntity s " +
            "join Equipment e on e.system.id = s.id " +
            "WHERE s.id = :id")
    boolean existsSystemOnEquipment(@Param("id") Long id);
}
