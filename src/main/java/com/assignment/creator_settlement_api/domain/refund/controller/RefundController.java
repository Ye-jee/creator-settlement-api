package com.assignment.creator_settlement_api.domain.refund.controller;

// 환불 목록 조회 (원본 데이터 조회)

import com.assignment.creator_settlement_api.domain.refund.dto.RefundRecordResponse;
import com.assignment.creator_settlement_api.domain.refund.service.RefundQueryService;
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
@RequestMapping("/refunds")
public class RefundController {

    private final RefundQueryService refundQueryService;

    @GetMapping
    public List<RefundRecordResponse> getRefunds(
            @RequestParam String creatorId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
    ) {
        return refundQueryService.getRefundsByCreatorAndPeriod(creatorId, startDate, endDate);
    }

}
