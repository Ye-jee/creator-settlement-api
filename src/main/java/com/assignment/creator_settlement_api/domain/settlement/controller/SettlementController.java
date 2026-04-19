package com.assignment.creator_settlement_api.domain.settlement.controller;

// 월별 정산 조회, 운영자 집계 (원본 데이터 기반 계산 결과)

import com.assignment.creator_settlement_api.domain.settlement.dto.AdminSettlementResponse;
import com.assignment.creator_settlement_api.domain.settlement.dto.MonthlySettlementResponse;
import com.assignment.creator_settlement_api.domain.settlement.service.AdminSettlementService;
import com.assignment.creator_settlement_api.domain.settlement.service.SettlementService;
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
public class SettlementController {

    private final SettlementService settlementService;
    private final AdminSettlementService adminSettlementService;

    // 월별 정산 API
    @GetMapping("/settlements/monthly")
    public MonthlySettlementResponse getMonthlySettlement(
            @RequestParam String creatorId,
            @RequestParam String yearMonth
    ) {
        return settlementService.getMonthlySettlement(creatorId, yearMonth);
    }

    // 운영자 집계 API
    @GetMapping("/admin/settlements")
    public AdminSettlementResponse getAdminSettlementSummary(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
    ) {
        return adminSettlementService.getAdminSettlementSummary(startDate, endDate);
    }


}
