package com.assignment.creator_settlement_api.domain.settlement.dto;

// 크리에이터 월별 정산 API 응답 DTO
public record MonthlySettlementResponse(
        String creatorId,
        String creatorName,
        String yearMonth,
        Long totalSalesAmount,
        Long refundAmount,
        Long netSalesAmount,
        Integer feeRatePercent,
        Long feeAmount,
        Long payoutAmount,
        Long salesCount,
        Long refundCount
) {
    public static MonthlySettlementResponse of(
            String creatorId,
            String creatorName,
            String yearMonth,
            Long totalSalesAmount,
            Long refundAmount,
            Long netSalesAmount,
            Integer feeRatePercent,
            Long feeAmount,
            Long payoutAmount,
            Long salesCount,
            Long refundCount
    ) {
        return new MonthlySettlementResponse(
                creatorId,
                creatorName,
                yearMonth,
                totalSalesAmount,
                refundAmount,
                netSalesAmount,
                feeRatePercent,
                feeAmount,
                payoutAmount,
                salesCount,
                refundCount
        );
    }

    // 빈 월 조회
    public static MonthlySettlementResponse empty(
            String creatorId,
            String creatorName,
            String yearMonth
    ) {
        return new MonthlySettlementResponse(
                creatorId,
                creatorName,
                yearMonth,
                0L,
                0L,
                0L,
                20,
                0L,
                0L,
                0L,
                0L
        );
    }


}

