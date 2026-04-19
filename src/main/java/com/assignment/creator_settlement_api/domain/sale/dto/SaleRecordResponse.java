package com.assignment.creator_settlement_api.domain.sale.dto;

import java.time.LocalDateTime;

public record SaleRecordResponse (  // 판매 내역 조회 DTO
        String saleRecordId,    // 판매 건 식별
        String courseId,        // 강의 식별
        String courseTitle,     // 강의명
        String studentId,       // 구매자 식별
        Long amount,            // 결제 금액
        LocalDateTime paidAt    // 결제 시각
) {
}
