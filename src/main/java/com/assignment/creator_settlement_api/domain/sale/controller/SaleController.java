package com.assignment.creator_settlement_api.domain.sale.controller;

// 판매 목록 조회 (원본 데이터 조회)

import com.assignment.creator_settlement_api.domain.sale.dto.SaleRecordResponse;
import com.assignment.creator_settlement_api.domain.sale.service.SaleQueryService;
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
public class SaleController {

    private final SaleQueryService saleQueryService;

    @GetMapping
    public List<SaleRecordResponse> getSales(
            @RequestParam String creatorId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
    ) {
        return saleQueryService.getSalesByCreatorAndPeriod(creatorId, startDate, endDate);
    }

}
