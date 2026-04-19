package com.assignment.creator_settlement_api.domain.refund.entity;

import com.assignment.creator_settlement_api.domain.sale.entity.SaleRecord;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)

public class RefundRecord {

    @Id
    @Column(nullable = false, length = 50)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "sale_record_id", nullable = false)
    private SaleRecord saleRecord;

    @Column(nullable = false)
    private Long refundAmount;

    @Column(nullable = false)
    private LocalDateTime refundedAt;

    public RefundRecord(String id, SaleRecord saleRecord, Long refundAmount, LocalDateTime refundedAt) {
        this.id = id;
        this.saleRecord = saleRecord;
        this.refundAmount = refundAmount;
        this.refundedAt = refundedAt;
    }
}
