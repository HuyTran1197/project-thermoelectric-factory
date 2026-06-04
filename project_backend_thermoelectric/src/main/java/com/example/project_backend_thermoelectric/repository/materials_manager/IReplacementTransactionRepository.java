package com.example.project_backend_thermoelectric.repository.materials_manager;

import com.example.project_backend_thermoelectric.dto.materials_manager.ReplacementInventoryDto;
import com.example.project_backend_thermoelectric.dto.materials_manager.ReplacementTransactionDto;
import com.example.project_backend_thermoelectric.entity.ReplacementTransaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface IReplacementTransactionRepository extends JpaRepository<ReplacementTransaction,Long> {
    @Query(value = """
    SELECT
        rt.id AS id,
        rt.type AS type,
        rt.quantity AS quantity,
        rt.created_at AS createdAt,
        rm.code AS materialCode,
        rm.name AS materialName,
        u.username AS username
    FROM replacement_transactions rt
    JOIN replacement_materials rm
        ON rm.id = rt.material_id
    JOIN users u
        ON u.id = rt.created_by
    WHERE
        (
            :keyword IS NULL
            OR LOWER(rm.code) LIKE LOWER(CONCAT('%', :keyword, '%'))
            OR LOWER(rm.name) LIKE LOWER(CONCAT('%', :keyword, '%'))
            OR LOWER(u.username) LIKE LOWER(CONCAT('%', :keyword, '%'))
        )
        AND (
            :type IS NULL
            OR rt.type = :type
        )
    
        AND (
            COALESCE(:fromDate, NULL) IS NULL\s
            OR rt.created_at >= :fromDate
        )
        AND (
            COALESCE(:toDate, NULL) IS NULL\s
            OR rt.created_at <= :toDate
        )
    ORDER BY rt.created_at DESC
    """,
            countQuery = """
    SELECT COUNT(*)
    FROM replacement_transactions rt
    JOIN replacement_materials rm
        ON rm.id = rt.material_id
    JOIN users u
        ON u.id = rt.created_by
    WHERE
        (
            :keyword IS NULL
            OR LOWER(rm.code) LIKE LOWER(CONCAT('%', :keyword, '%'))
            OR LOWER(rm.name) LIKE LOWER(CONCAT('%', :keyword, '%'))
            OR LOWER(u.username) LIKE LOWER(CONCAT('%', :keyword, '%'))
        )
        AND (
            :type IS NULL
            OR rt.type = :type
        )
        
        AND (
            COALESCE(:fromDate, NULL) IS NULL\s
            OR rt.created_at >= :fromDate
        )
        AND (
            COALESCE(:toDate, NULL) IS NULL\s
            OR rt.created_at <= :toDate
        )
    """,
            nativeQuery = true)
    Page<ReplacementTransactionDto> searchReplacementTransactions(
            @Param("keyword") String keyword,
            @Param("type") String type,
            @Param("fromDate") LocalDateTime fromDate,
            @Param("toDate") LocalDateTime toDate,
            Pageable pageable
    );
    //KHo Vật tư
    @Query(value = """
    SELECT
        rm.id AS id,
        rm.code AS code,
        rm.name AS name,

        COALESCE(
            SUM(
                CASE
                    WHEN rt.type = 'IMPORT'
                    THEN rt.quantity

                    WHEN rt.type = 'EXPORT'
                    THEN -rt.quantity

                    ELSE 0
                END
            ),
            0
        ) AS quantity

    FROM replacement_materials rm

    LEFT JOIN replacement_transactions rt
        ON rt.material_id = rm.id

    WHERE
        (
            :code IS NULL
            OR LOWER(rm.code) LIKE LOWER(CONCAT('%', :code, '%'))
        )

        AND
        (
            :name IS NULL
            OR LOWER(rm.name) LIKE LOWER(CONCAT('%', :name, '%'))
        )

    GROUP BY
        rm.id,
        rm.code,
        rm.name

    ORDER BY rm.name ASC
    """,

            countQuery = """
    SELECT COUNT(*)

    FROM replacement_materials rm

    WHERE
        (
            :code IS NULL
            OR LOWER(rm.code) LIKE LOWER(CONCAT('%', :code, '%'))
        )

        AND
        (
            :name IS NULL
            OR LOWER(rm.name) LIKE LOWER(CONCAT('%', :name, '%'))
        )
    """,

            nativeQuery = true)
    Page<ReplacementInventoryDto> getInventory(

            @Param("code") String code,

            @Param("name") String name,

            Pageable pageable
    );
    @Query(value = "SELECT " +
            "COALESCE(SUM(CASE WHEN type = 'IMPORT' THEN quantity ELSE 0 END), 0) - " +
            "COALESCE(SUM(CASE WHEN type = 'EXPORT' THEN quantity ELSE 0 END), 0) " +
            "FROM replacement_transactions WHERE material_id = :materialId", nativeQuery = true)
    int getStockQuantity(@Param("materialId") Long materialId);
}
