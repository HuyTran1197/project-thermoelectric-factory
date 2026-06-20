package com.example.project_backend_thermoelectric.repository.repair_order;

import com.example.project_backend_thermoelectric.dto.work_orders.RepairOrderForWorkOrderDto;
import com.example.project_backend_thermoelectric.entity.RepairOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface IRepairOrderRepository
        extends JpaRepository<RepairOrder, Long> {

    @Query("""
        select r
        from RepairOrder r
        where
            :keyword = ''
            or lower(r.title) like lower(concat('%',:keyword,'%'))
            or lower(r.description) like lower(concat('%',:keyword,'%'))
        order by r.createdAt desc
    """)
    Page<RepairOrder> search(
            @Param("keyword") String keyword,
            Pageable pageable
    );

    @Query(
            value = "select " +
                    "ro.id as id, " +
                    "ro.title as title, " +
                    "ro.description as description, " +
                    "e.name as equipment, " +
                    "emp.full_name as createdBy, " +
                    "ro.created_at as createdAt, " +
                    "ro.status as status, " +
                    "case " +
                    "when wo.id is null then false " +
                    "else true " +
                    "end as hasWorkOrder " +
                    "from repair_order ro " +
                    "join equipments e on e.id = ro.equipment_id " +
                    "join users u on u.id = ro.created_by " +
                    "join employees emp on emp.id = u.employee_id " +
                    "left join work_orders wo on wo.request_id = ro.id " +
                    "where " +
                    "(:searchTitle is null or :searchTitle = '' or lower(ro.title) like lower(concat('%',:searchTitle,'%'))) " +
                    "and (:searchCreatedBy is null or :searchCreatedBy = '' or lower(emp.full_name) like lower(concat('%',:searchCreatedBy,'%'))) " +
                    "and (:equipmentId is null or e.id = :equipmentId) " +
                    "and (:repairStatus is null or :repairStatus = '' or ro.status = :repairStatus) " +
                    "and (" +
                    ":hasWorkOrder is null " +
                    "or (:hasWorkOrder = true and wo.id is not null) " +
                    "or (:hasWorkOrder = false and wo.id is null)" +
                    ") " +
                    "order by ro.created_at desc",
            countQuery =
                    "select count(*) " +
                            "from repair_order ro " +
                            "join equipments e on e.id = ro.equipment_id " +
                            "join users u on u.id = ro.created_by " +
                            "join employees emp on emp.id = u.employee_id " +
                            "left join work_orders wo on wo.request_id = ro.id " +
                            "where " +
                            "(:searchTitle is null or :searchTitle = '' or lower(ro.title) like lower(concat('%',:searchTitle,'%'))) " +
                            "and (:searchCreatedBy is null or :searchCreatedBy = '' or lower(emp.full_name) like lower(concat('%',:searchCreatedBy,'%'))) " +
                            "and (:equipmentId is null or e.id = :equipmentId) " +
                            "and (:repairStatus is null or :repairStatus = '' or ro.status = :repairStatus) " +
                            "and (" +
                            ":hasWorkOrder is null " +
                            "or (:hasWorkOrder = true and wo.id is not null) " +
                            "or (:hasWorkOrder = false and wo.id is null)" +
                            ")", nativeQuery = true)
    Page<RepairOrderForWorkOrderDto> searchForWorkOrder(@Param("searchTitle") String title,
                                                        @Param("searchCreatedBy") String createdBy,
                                                        @Param("equipmentId") Long equipmentId,
                                                        @Param("repairStatus") String repairStatus,
                                                        @Param("hasWorkOrder") Boolean hasWorkOrder,
                                                        Pageable pageable);

    @Query(
            value =
                    "select count(*) " +
                            "from repair_order ro " +
                            "left join work_orders wo " +
                            "on wo.request_id = ro.id " +
                            "where wo.id is null",
            nativeQuery = true
    )
    Long countRepairOrderWithoutWorkOrder();
}