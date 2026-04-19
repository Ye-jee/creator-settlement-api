package com.assignment.creator_settlement_api.domain.sale.repository;

import com.assignment.creator_settlement_api.domain.sale.entity.SaleRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface SaleRecordRepository extends JpaRepository<SaleRecord, String> {

    // 판매 금액 합계
    @Query("""
        select coalesce(sum(s.amount), 0)
        from SaleRecord s
        where s.course.creator.id = :creatorId
          and s.paidAt between :start and :end
    """)
    Long sumSalesAmountByCreatorAndPeriod(
            @Param("creatorId") String creatorId,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end
    );

    // 판매 건수
    @Query("""
        select count(s)
        from SaleRecord s
        where s.course.creator.id = :creatorId
          and s.paidAt between :start and :end
    """)
    Long countSalesByCreatorAndPeriod(
            @Param("creatorId") String creatorId,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end
    );

    // 판매 목록
    @Query("""
        select s
        from SaleRecord s
        where s.course.creator.id = :creatorId
          and s.paidAt between :start and :end
        order by s.paidAt desc
    """)
    List<SaleRecord> findSalesByCreatorAndPeriod(
            @Param("creatorId") String creatorId,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end
    );


}
