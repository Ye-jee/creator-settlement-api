package com.assignment.creator_settlement_api.domain.settlement.dto;

import java.time.LocalDate;
import java.util.List;

// 운영자 집계 API 응답 DTO
// 2) 운영자 집계 최상위 DTO
public record AdminSettlementResponse(
        LocalDate periodStart,
        LocalDate periodEnd,
        List<CreatorSettlementSummaryResponse> items,
        Long totalPayoutAmount
) {
}
