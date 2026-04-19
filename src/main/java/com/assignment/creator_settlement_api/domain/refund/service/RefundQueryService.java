package com.assignment.creator_settlement_api.domain.refund.service;

import com.assignment.creator_settlement_api.domain.refund.dto.RefundRecordResponse;
import com.assignment.creator_settlement_api.domain.refund.entity.RefundRecord;
import com.assignment.creator_settlement_api.domain.refund.repository.RefundRecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RefundQueryService {

    private final RefundRecordRepository refundRecordRepository;

    public List<RefundRecordResponse> getRefundsByCreatorAndPeriod(
            String creatorId,
            LocalDate startDate,
            LocalDate endDate
    ) {
        LocalDateTime start = startDate.atStartOfDay();
        LocalDateTime end = endDate.atTime(23, 59, 59);

        List<RefundRecord> refundRecords =
                refundRecordRepository.findRefundsByCreatorAndPeriod(creatorId, start, end);

        return refundRecords.stream()
                .map(refundRecord -> new RefundRecordResponse(
                        refundRecord.getId(),
                        refundRecord.getSaleRecord().getId(),
                        refundRecord.getSaleRecord().getCourse().getId(),
                        refundRecord.getSaleRecord().getCourse().getTitle(),
                        refundRecord.getSaleRecord().getStudentId(),
                        refundRecord.getRefundAmount(),
                        refundRecord.getRefundedAt()
                ))
                .toList();
    }

}
