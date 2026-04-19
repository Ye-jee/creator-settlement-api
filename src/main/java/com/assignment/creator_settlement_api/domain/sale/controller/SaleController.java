package com.assignment.creator_settlement_api.domain.sale.controller;

// 판매 목록 조회 (원본 데이터 조회)

import com.assignment.creator_settlement_api.domain.sale.dto.SaleRecordResponse;
import com.assignment.creator_settlement_api.domain.sale.service.SaleQueryService;
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
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/sales")
@Tag(name = "Sale", description = "판매 내역 조회 API")
public class SaleController {

    private final SaleQueryService saleQueryService;

    @Operation(summary = "크리에이터별 판매 내역 조회")
    @GetMapping
    public List<SaleRecordResponse> getSales(
            @Parameter(description = "크리에이터 ID") @RequestParam String creatorId,
            @Parameter(description = "조회 시작일") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @Parameter(description = "조회 종료일") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
    ) {
        return saleQueryService.getSalesByCreatorAndPeriod(creatorId, startDate, endDate);
    }

}
