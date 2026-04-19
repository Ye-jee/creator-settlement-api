package com.assignment.creator_settlement_api.infra;

import com.assignment.creator_settlement_api.domain.refund.entity.RefundRecord;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.time.OffsetDateTime;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record SampleDataDto(
        List<CreatorSeed> creators,
        List<CourseSeed> courses,
        List<SaleRecordSeed> saleRecords,
        List<RefundRecordSeed> refundRecords

) {
    public record CreatorSeed(String id, String name) {}
    public record CourseSeed(String id, String creatorId, String title) {}
    public record SaleRecordSeed(
            String id,
            String courseId,
            String studentId,
            Long amount,
            OffsetDateTime paidAt
    ) {}

    public record RefundRecordSeed(
            String id,
            String saleRecordId,
            Long refundAmount,
            OffsetDateTime refundedAt
    ) {}
}
