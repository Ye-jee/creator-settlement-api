package com.assignment.creator_settlement_api.domain.settlement.service;

import com.assignment.creator_settlement_api.domain.creator.entity.Creator;
import com.assignment.creator_settlement_api.domain.creator.repository.CreatorRepository;
import com.assignment.creator_settlement_api.domain.refund.repository.RefundRecordRepository;
import com.assignment.creator_settlement_api.domain.sale.repository.SaleRecordRepository;
import com.assignment.creator_settlement_api.domain.settlement.dto.AdminSettlementResponse;
import com.assignment.creator_settlement_api.domain.settlement.dto.CreatorSettlementSummaryResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminSettlementService {

    // 수수료
    private static final int FEE_RATE_PERCENT = 20;

    private final CreatorRepository creatorRepository;
    private final SaleRecordRepository saleRecordRepository;
    private final RefundRecordRepository refundRecordRepository;

    // 운영자 집계 서비스 (여러 크리에이터, 임의 기간, 목록+전체 합계 반환)
    public AdminSettlementResponse getAdminSettlementSummary(LocalDate startDate, LocalDate endDate) {
        // 1. 기간 검증
        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("시작일은 종료일보다 늦을 수 없습니다.");
        }

        // 2. 날짜 범위로 변환
        LocalDateTime start = startDate.atStartOfDay();
        LocalDateTime end = endDate.atTime(23, 59, 59);

        // 3. 크리에이터 전체 순회
        List<Creator> creators = creatorRepository.findAll();

        // 4. 크리에이터별 정산 계산
        List<CreatorSettlementSummaryResponse> items = creators.stream()
                .map(creator -> {
                    long totalSalesAmount = saleRecordRepository.sumSalesAmountByCreatorAndPeriod(
                            creator.getId(), start, end
                    );
                    long salesCount = saleRecordRepository.countSalesByCreatorAndPeriod(
                            creator.getId(), start, end
                    );
                    long refundAmount = refundRecordRepository.sumRefundAmountByCreatorAndPeriod(
                            creator.getId(), start, end
                    );
                    long refundCount = refundRecordRepository.countRefundsByCreatorAndPeriod(
                            creator.getId(), start, end
                    );

                    long netSalesAmount = totalSalesAmount - refundAmount;
                    long feeAmount = Math.round(netSalesAmount * (FEE_RATE_PERCENT / 100.0));
                    long payoutAmount = netSalesAmount - feeAmount;

                    return new CreatorSettlementSummaryResponse(
                            creator.getId(),
                            creator.getName(),
                            totalSalesAmount,
                            refundAmount,
                            netSalesAmount,
                            feeAmount,
                            payoutAmount,
                            salesCount,
                            refundCount
                    );
                })
                .toList();

        // 5. 전체 합계 계산
        long totalPayoutAmount = items.stream()
                .mapToLong(CreatorSettlementSummaryResponse::payoutAmount)
                .sum();


        // 6. 최종 응답 반환
        return new AdminSettlementResponse(
                startDate,
                endDate,
                items,
                totalPayoutAmount
        );



        //-----------------------------------
        // 2. 기간 내 판매/환불 데이터 조회
        // 3. creator별로 그룹핑
        // 4. creator별 정산 계산
        // 5. 전체 합계 계산
        // 6. DTO 반환
    }
}
