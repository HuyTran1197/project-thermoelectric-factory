package com.example.project_backend_thermoelectric.repository.work_orders;


import com.example.project_backend_thermoelectric.dto.work_orders.WorkOrderResponseDto;
import com.example.project_backend_thermoelectric.entity.WorkOrder;
import com.example.project_backend_thermoelectric.enums.MaterialStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface IWorkOrderRepository
        extends JpaRepository<WorkOrder, Long> {
    List<WorkOrder> getWorkOrdersByMaterialStatus(MaterialStatus materialStatus);

    long countByMaterialStatus(MaterialStatus materialStatus);

    boolean existsByRequestId(Long repairOrderId);

    boolean existsByCode(String code);
    Optional<WorkOrder> findByRequestId(Long repairOrderId);


    @Query(value = "select " +
            "wo.id as id, " +
            "wo.code as code, " +
            "e.name as equipment, " +
            "ro.description as description, " +
            "wo.status as status, " +
            "case " +
            "when wo.status = 'DA_PHAN_CONG' then 'Đã phân công' " +
            "when wo.status = 'DANG_THUC_HIEN' then 'Đang thực hiện' " +
            "when wo.status = 'CHO_VAT_TU' then 'Chờ vật tư' " +
            "when wo.status = 'HOAN_THANH' then 'Hoàn thành' " +
            "end as statusDisplay " +
            "from work_orders wo " +
            "join repair_order ro on ro.id = wo.request_id " +
            "join equipments e on e.id = ro.equipment_id " +
            "where (:searchCode is null or :searchCode = '' or wo.code like concat('%',:searchCode,'%')) " +
            "and (:searchEquipment is null or :searchEquipment = '' or e.name like concat('%',:searchEquipment,'%')) " +
            "and (:searchStatus is null or :searchStatus = '' or wo.status = :searchStatus)",
            countQuery =
                    "select count(*) " +
                            "from work_orders wo " +
                            "join repair_order ro on ro.id = wo.request_id " +
                            "join equipments e on e.id = ro.equipment_id " +
                            "where (:searchCode is null or :searchCode = '' or wo.code like concat('%',:searchCode,'%')) " +
                            "and (:searchEquipment is null or :searchEquipment = '' or e.name like concat('%',:searchEquipment,'%')) " +
                            "and (:searchStatus is null or :searchStatus = '' or wo.status = :searchStatus)",
            nativeQuery = true)
    Page<WorkOrderResponseDto> search(
            @Param("searchCode") String code,
            @Param("searchEquipment") String equipment,
            @Param("searchStatus") String status,
            Pageable pageable);

    @Query("""
    SELECT COUNT(w)
    FROM WorkOrder w
    WHERE YEAR(w.startDate) = :year
""")
    long countByYear(
            @Param("year")
            int year
    );

    Optional<WorkOrder> findByCode(String code);
    List<WorkOrder> findAllByOrderByCodeAsc();
}