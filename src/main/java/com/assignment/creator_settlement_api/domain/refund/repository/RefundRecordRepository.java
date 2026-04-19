package com.assignment.creator_settlement_api.domain.refund.repository;

import com.assignment.creator_settlement_api.domain.refund.entity.RefundRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface RefundRecordRepository extends JpaRepository<RefundRecord, String> {

    // 환불 금액 합계
    @Query("""
        select coalesce(sum(r.refundAmount), 0)
        from RefundRecord r
        where r.saleRecord.course.creator.id = :creatorId
          and r.refundedAt between :start and :end
    """)
    Long sumRefundAmountByCreatorAndPeriod(
            @Param("creatorId") String creatorId,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end
    );

    // 환불 건수
    @Query("""
        select count(r)
        from RefundRecord r
        where r.saleRecord.course.creator.id = :creatorId
          and r.refundedAt between :start and :end
    """)
    Long countRefundsByCreatorAndPeriod(
            @Param("creatorId") String creatorId,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end
    );

    // 환불 목록
    @Query("""
        select r
        from RefundRecord r
        where r.saleRecord.course.creator.id = :creatorId
          and r.refundedAt between :start and :end
        order by r.refundedAt desc
    """)
    List<RefundRecord> findRefundsByCreatorAndPeriod(
            @Param("creatorId") String creatorId,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end
    );

}
