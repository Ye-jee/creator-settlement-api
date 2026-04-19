package com.assignment.creator_settlement_api.domain.settlement.controller;

// 월별 정산 조회, 운영자 집계 (원본 데이터 기반 계산 결과)

import com.assignment.creator_settlement_api.domain.settlement.dto.AdminSettlementResponse;
import com.assignment.creator_settlement_api.domain.settlement.dto.MonthlySettlementResponse;
import com.assignment.creator_settlement_api.domain.settlement.service.AdminSettlementService;
import com.assignment.creator_settlement_api.domain.settlement.service.SettlementService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
@RequestMapping
@Tag(name = "Settlement", description = "정산 관련 API")
public class SettlementController {

    private final SettlementService settlementService;
    private final AdminSettlementService adminSettlementService;

    // 월별 정산 API
    @Operation(
            summary = "크리에이터 월별 정산 조회",
            description = "creatorId와 yearMonth를 기준으로 월별 정산 결과를 조회한다."
    )
    @GetMapping("/settlements/monthly")
    public MonthlySettlementResponse getMonthlySettlement(
            @Parameter(description = "크리에이터 ID") @RequestParam String creatorId,
            @Parameter(description = "조회 연월, 예: 2025-03") @RequestParam String yearMonth
    ) {
        return settlementService.getMonthlySettlement(creatorId, yearMonth);
    }

    // 운영자 집계 API
    @Operation(
            summary = "운영자 기간 정산 집계",
            description = "기간 내 전체 크리에이터의 정산 현황과 전체 합계를 조회한다."
    )
    @GetMapping("/admin/settlements")
    public AdminSettlementResponse getAdminSettlementSummary(
            @Parameter(description = "조회 시작일") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @Parameter(description = "조회 종료일") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
    ) {
        return adminSettlementService.getAdminSettlementSummary(startDate, endDate);
    }


}
