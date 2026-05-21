package com.example.project_backend_thermoelectric.repository.tool;

import com.example.project_backend_thermoelectric.entity.ToolBorrowing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ToolBorrowingRepository extends JpaRepository<ToolBorrowing, Long> {
    List<ToolBorrowing> findByStatus(String status);

    @Query("SELECT tb FROM ToolBorrowing tb " +
           "LEFT JOIN FETCH tb.tool t " +
           "LEFT JOIN FETCH tb.employee e " +
           "LEFT JOIN FETCH e.department d " +
           "LEFT JOIN FETCH e.position p " +
           "WHERE (:toolCode IS NULL OR LOWER(t.code) LIKE LOWER(CONCAT('%', :toolCode, '%')) OR LOWER(t.name) LIKE LOWER(CONCAT('%', :toolCode, '%'))) " +
           "AND (:employeeSearch IS NULL OR LOWER(e.fullName) LIKE LOWER(CONCAT('%', :employeeSearch, '%')) OR STR(e.id) LIKE CONCAT('%', :employeeSearch, '%')) " +
           "AND (:status IS NULL OR tb.status = :status)")
    List<ToolBorrowing> searchBorrowings(@Param("toolCode") String toolCode,
                                         @Param("employeeSearch") String employeeSearch,
                                         @Param("status") String status);
}
