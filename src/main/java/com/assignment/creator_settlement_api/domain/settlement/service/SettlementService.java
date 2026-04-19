package com.assignment.creator_settlement_api.domain.settlement.service;

import com.assignment.creator_settlement_api.domain.creator.entity.Creator;
import com.assignment.creator_settlement_api.domain.creator.repository.CreatorRepository;
import com.assignment.creator_settlement_api.domain.refund.repository.RefundRecordRepository;
import com.assignment.creator_settlement_api.domain.sale.repository.SaleRecordRepository;
import com.assignment.creator_settlement_api.domain.settlement.dto.AdminSettlementResponse;
import com.assignment.creator_settlement_api.domain.settlement.dto.CreatorSettlementSummaryResponse;
import com.assignment.creator_settlement_api.domain.settlement.dto.MonthlySettlementResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SettlementService {

    // 수수료
    private static final int FEE_RATE_PERCENT = 20;

    private final CreatorRepository creatorRepository;
    private final SaleRecordRepository saleRecordRepository;
    private final RefundRecordRepository refundRecordRepository;


    // 월별 정산 서비스 (한 크리에이터, 한달, 정산 상세 수치 반환)
    public MonthlySettlementResponse getMonthlySettlement(String creatorId, String yearMonth) {
        // 1. creator 조회 (크리에이터 존재 확인)
        Creator creator = creatorRepository.findById(creatorId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 크리에이터입니다."));


        // 2. YearMonth 파싱 + KST 월 범위 계산 (월별 정산 범위) >> 연월을 날짜 범위로 변환
        YearMonth targetMonth = YearMonth.parse(yearMonth);
        LocalDateTime start = targetMonth.atDay(1).atStartOfDay();
        LocalDateTime end = targetMonth.atEndOfMonth().atTime(23, 59, 59);

        // 3. 판매 합계 및 건수 조회
        Long totalSalesAmount = saleRecordRepository.sumSalesAmountByCreatorAndPeriod(creatorId, start, end);
        Long salesCount = saleRecordRepository.countSalesByCreatorAndPeriod(creatorId, start, end);

        // 4. 환불 합계와 환불 건수 조회
        Long refundAmount = refundRecordRepository.sumRefundAmountByCreatorAndPeriod(creatorId, start, end);
        Long refundCount = refundRecordRepository.countRefundsByCreatorAndPeriod(creatorId, start, end);

        // 5. 순 판매, 수수료, 정산 예정 금액 계산
        long netSalesAmount = totalSalesAmount - refundAmount;
        long feeAmount = Math.round(netSalesAmount * (FEE_RATE_PERCENT / 100.0));
        long payoutAmount = netSalesAmount - feeAmount;

        // 6. 계산 후 DTO 반환
        return MonthlySettlementResponse.of(
                creator.getId(),
                creator.getName(),
                yearMonth,
                totalSalesAmount,
                refundAmount,
                netSalesAmount,
                FEE_RATE_PERCENT,
                feeAmount,
                payoutAmount,
                salesCount,
                refundCount
        );
    }




}
