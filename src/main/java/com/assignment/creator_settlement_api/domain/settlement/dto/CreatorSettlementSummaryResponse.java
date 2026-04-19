package com.assignment.creator_settlement_api.domain.settlement.dto;

// 운영자 집계 API 응답 DTO
// 1) 크리에이터별 집계 항목 DTO
public record CreatorSettlementSummaryResponse(
        String creatorId,
        String creatorName,
        Long totalSalesAmount,
        Long refundAmount,
        Long netSalesAmount,
        Long feeAmount,
        Long payoutAmount,
        Long salesCount,
        Long refundCount

) {
}
