package com.assignment.creator_settlement_api.domain.refund.dto;

import java.time.LocalDateTime;

public record RefundRecordResponse(     // 환불 내역 조회 DTO
        String refundRecordId,      // 환불 건 식별
        String saleRecordId,        // 원본 판매
        String courseId,            // 어떤 강의에 대한 환불인지
        String courseTitle,
        String studentId,           // 어떤 구매자(수강생) 환불인지
        Long refundAmount,          // 환불 금액
        LocalDateTime refundedAt    // 취소 시각
) {
}
