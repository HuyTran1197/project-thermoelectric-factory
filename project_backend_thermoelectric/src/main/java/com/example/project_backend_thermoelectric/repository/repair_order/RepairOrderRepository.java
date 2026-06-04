package com.example.project_backend_thermoelectric.repository.repair_order;

import com.example.project_backend_thermoelectric.entity.RepairOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RepairOrderRepository
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
}