package com.example.project_backend_thermoelectric.repository.materials_manager;

import com.example.project_backend_thermoelectric.dto.materials_manager.ConsumableInventoryDto;
import com.example.project_backend_thermoelectric.dto.materials_manager.ConsumableTransactionDto;
import com.example.project_backend_thermoelectric.entity.ConsumableTransaction;
import com.example.project_backend_thermoelectric.enums.TransactionType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Repository
public interface IConsumableTransactionRepository extends JpaRepository<ConsumableTransaction,Long> {
    @Query(value = """
    SELECT
        ct.id AS id,
        ct.type AS type,
        ct.quantity AS quantity,
        ct.created_at AS createdAt,
        cm.code AS materialCode,
        cm.name AS materialName,
        u.username AS username
    FROM consumable_transactions ct
    JOIN consumable_materials cm
        ON cm.id = ct.material_id
    JOIN users u
        ON u.id = ct.created_by
    WHERE
        (
            :keyword IS NULL
            OR LOWER(cm.code) LIKE LOWER(CONCAT('%', :keyword, '%'))
            OR LOWER(cm.name) LIKE LOWER(CONCAT('%', :keyword, '%'))
            OR LOWER(u.username) LIKE LOWER(CONCAT('%', :keyword, '%'))
        )
        AND (
            :type IS NULL
            OR ct.type = :type
        )
    
        AND (
            COALESCE(:fromDate, NULL) IS NULL\s
            OR ct.created_at >= :fromDate
        )
        AND (
            COALESCE(:toDate, NULL) IS NULL\s
            OR ct.created_at <= :toDate
        )
    ORDER BY ct.created_at DESC
    """,
            countQuery = """
    SELECT COUNT(*)
    FROM consumable_transactions ct
    JOIN consumable_materials cm
        ON cm.id = ct.material_id
    JOIN users u
        ON u.id = ct.created_by
    WHERE
        (
            :keyword IS NULL
            OR LOWER(cm.code) LIKE LOWER(CONCAT('%', :keyword, '%'))
            OR LOWER(cm.name) LIKE LOWER(CONCAT('%', :keyword, '%'))
            OR LOWER(u.username) LIKE LOWER(CONCAT('%', :keyword, '%'))
        )
        AND (
            :type IS NULL
            OR ct.type = :type
        )
        
        AND (
            COALESCE(:fromDate, NULL) IS NULL\s
            OR ct.created_at >= :fromDate
        )
        AND (
            COALESCE(:toDate, NULL) IS NULL\s
            OR ct.created_at <= :toDate
        )
    """,
            nativeQuery = true)
    Page<ConsumableTransactionDto> searchConsumableTransactions(
            @Param("keyword") String keyword,
            @Param("type") String type,
            @Param("fromDate") LocalDateTime fromDate,
            @Param("toDate") LocalDateTime toDate,
            Pageable pageable
    );
    //KHo Vật tư
    @Query(value = """
    SELECT
        cm.id AS id,
        cm.code AS code,
        cm.name AS name,

        COALESCE(
            SUM(
                CASE
                    WHEN ct.type = 'IMPORT'
                    THEN ct.quantity

                    WHEN ct.type = 'EXPORT'
                    THEN -ct.quantity

                    ELSE 0
                END
            ),
            0
        ) AS quantity

    FROM consumable_materials cm

    LEFT JOIN consumable_transactions ct
        ON ct.material_id = cm.id

    WHERE
        (
            :code IS NULL
            OR LOWER(cm.code) LIKE LOWER(CONCAT('%', :code, '%'))
        )

        AND
        (
            :name IS NULL
            OR LOWER(cm.name) LIKE LOWER(CONCAT('%', :name, '%'))
        )

    GROUP BY
        cm.id,
        cm.code,
        cm.name

    ORDER BY cm.name ASC
    """,

            countQuery = """
    SELECT COUNT(*)

    FROM consumable_materials cm

    WHERE
        (
            :code IS NULL
            OR LOWER(cm.code) LIKE LOWER(CONCAT('%', :code, '%'))
        )

        AND
        (
            :name IS NULL
            OR LOWER(cm.name) LIKE LOWER(CONCAT('%', :name, '%'))
        )
    """,

            nativeQuery = true)
    Page<ConsumableInventoryDto> getInventory(

            @Param("code") String code,

            @Param("name") String name,

            Pageable pageable
    );
    @Query(value = "SELECT " +
            "COALESCE(SUM(CASE WHEN type = 'IMPORT' THEN quantity ELSE 0 END), 0) - " +
            "COALESCE(SUM(CASE WHEN type = 'EXPORT' THEN quantity ELSE 0 END), 0) " +
            "FROM consumable_transactions WHERE material_id = :materialId", nativeQuery = true)
    int getStockQuantity(@Param("materialId") Long materialId);
}
