package com.assignment.creator_settlement_api.domain.sale.service;

import com.assignment.creator_settlement_api.domain.sale.dto.SaleRecordResponse;
import com.assignment.creator_settlement_api.domain.sale.entity.SaleRecord;
import com.assignment.creator_settlement_api.domain.sale.repository.SaleRecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SaleQueryService {

    private final SaleRecordRepository saleRecordRepository;

    public List<SaleRecordResponse> getSalesByCreatorAndPeriod(
            String creatorId,
            LocalDate startDate,
            LocalDate endDate
    ) {
        // 기간을 날짜 범위로 변경
        LocalDateTime start = startDate.atStartOfDay();
        LocalDateTime end = endDate.atTime(23, 59, 59);

        // 리포지터리에서 판매 목록 조회
        List<SaleRecord> saleRecords =
                saleRecordRepository.findSalesByCreatorAndPeriod(creatorId, start, end);

        // 엔티티를 응답 DTO로 변환
        return saleRecords.stream()
                .map(saleRecord -> new SaleRecordResponse(
                        saleRecord.getId(),
                        saleRecord.getCourse().getId(),
                        saleRecord.getCourse().getTitle(),
                        saleRecord.getStudentId(),
                        saleRecord.getAmount(),
                        saleRecord.getPaidAt()
                ))
                .toList();
    }


}
