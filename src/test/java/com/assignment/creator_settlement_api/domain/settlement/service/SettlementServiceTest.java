package com.assignment.creator_settlement_api.domain.settlement.service;

import com.assignment.creator_settlement_api.domain.course.entity.Course;
import com.assignment.creator_settlement_api.domain.course.repository.CourseRepository;
import com.assignment.creator_settlement_api.domain.creator.entity.Creator;
import com.assignment.creator_settlement_api.domain.creator.repository.CreatorRepository;
import com.assignment.creator_settlement_api.domain.refund.entity.RefundRecord;
import com.assignment.creator_settlement_api.domain.refund.repository.RefundRecordRepository;
import com.assignment.creator_settlement_api.domain.sale.entity.SaleRecord;
import com.assignment.creator_settlement_api.domain.sale.repository.SaleRecordRepository;
import com.assignment.creator_settlement_api.domain.settlement.dto.MonthlySettlementResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class SettlementServiceTest {

    private final SettlementService settlementService;
    private final CreatorRepository creatorRepository;
    private final CourseRepository courseRepository;
    private final SaleRecordRepository saleRecordRepository;
    private final RefundRecordRepository refundRecordRepository;

    @Autowired
    public SettlementServiceTest(SettlementService settlementService,
                                 CreatorRepository creatorRepository,
                                 CourseRepository courseRepository,
                                 SaleRecordRepository saleRecordRepository,
                                 RefundRecordRepository refundRecordRepository) {
        this.settlementService = settlementService;
        this.creatorRepository = creatorRepository;
        this.courseRepository = courseRepository;
        this.saleRecordRepository = saleRecordRepository;
        this.refundRecordRepository = refundRecordRepository;
    }

    @BeforeEach
    void setUp() {
        Creator creator1 = creatorRepository.save(new Creator("creator-1", "김강사"));
        Creator creator2 = creatorRepository.save(new Creator("creator-2", "이강사"));
        Creator creator3 = creatorRepository.save(new Creator("creator-3", "박강사"));

        Course course1 = courseRepository.save(new Course("course-1", creator1, "Spring Boot 입문"));
        Course course2 = courseRepository.save(new Course("course-2", creator1, "JPA 실전"));
        Course course3 = courseRepository.save(new Course("course-3", creator2, "Kotlin 기초"));
        Course course4 = courseRepository.save(new Course("course-4", creator3, "MSA 설계"));

        SaleRecord sale1 = saleRecordRepository.save(
                new SaleRecord("sale-1", course1, "student-1", 50_000L, LocalDateTime.parse("2025-03-05T10:00:00"))
        );
        SaleRecord sale2 = saleRecordRepository.save(
                new SaleRecord("sale-2", course1, "student-2", 50_000L, LocalDateTime.parse("2025-03-15T14:30:00"))
        );
        SaleRecord sale3 = saleRecordRepository.save(
                new SaleRecord("sale-3", course2, "student-3", 80_000L, LocalDateTime.parse("2025-03-20T09:00:00"))
        );
        SaleRecord sale4 = saleRecordRepository.save(
                new SaleRecord("sale-4", course2, "student-4", 80_000L, LocalDateTime.parse("2025-03-22T11:00:00"))
        );
        SaleRecord sale5 = saleRecordRepository.save(
                new SaleRecord("sale-5", course3, "student-5", 60_000L, LocalDateTime.parse("2025-01-31T23:30:00"))
        );
        SaleRecord sale6 = saleRecordRepository.save(
                new SaleRecord("sale-6", course3, "student-6", 60_000L, LocalDateTime.parse("2025-03-10T16:00:00"))
        );
        saleRecordRepository.save(
                new SaleRecord("sale-7", course4, "student-7", 120_000L, LocalDateTime.parse("2025-02-14T10:00:00"))
        );

        refundRecordRepository.save(
                new RefundRecord("cancel-1", sale3, 80_000L, LocalDateTime.parse("2025-03-25T10:00:00"))
        );
        refundRecordRepository.save(
                new RefundRecord("cancel-2", sale4, 30_000L, LocalDateTime.parse("2025-03-28T15:00:00"))
        );
        refundRecordRepository.save(
                new RefundRecord("cancel-3", sale5, 60_000L, LocalDateTime.parse("2025-02-02T09:30:00"))
        );
    }

    // 테스트1 검증 시나리오: creator-1의 2025-03 정산
    @Test
    @DisplayName("creator-1의 2025-03 월별 정산이 올바르게 계산된다")
    void getMonthlySettlement_creator1_march() {
        // when
        MonthlySettlementResponse response =
                settlementService.getMonthlySettlement("creator-1", "2025-03");

        // then
        assertThat(response.creatorId()).isEqualTo("creator-1");
        assertThat(response.creatorName()).isEqualTo("김강사");
        assertThat(response.yearMonth()).isEqualTo("2025-03");
        assertThat(response.totalSalesAmount()).isEqualTo(260_000L);
        assertThat(response.refundAmount()).isEqualTo(110_000L);
        assertThat(response.netSalesAmount()).isEqualTo(150_000L);
        assertThat(response.feeRatePercent()).isEqualTo(20);
        assertThat(response.feeAmount()).isEqualTo(30_000L);
        assertThat(response.payoutAmount()).isEqualTo(120_000L);
        assertThat(response.salesCount()).isEqualTo(4L);
        assertThat(response.refundCount()).isEqualTo(2L);
    }

    // 테스트2 검증 시나리오: 부분 환불 처리
    @Test
    @DisplayName("부분 환불이 원결제보다 작으면 그 차액만 순 판매에 반영된다")
    void partialRefund_isAppliedToNetSales() {
        // given
        Creator creator = creatorRepository.save(new Creator("creator-pf", "부분환불강사"));
        Course course = courseRepository.save(new Course("course-pf", creator, "부분 환불 테스트 강의"));

        SaleRecord sale = saleRecordRepository.save(
                new SaleRecord(
                        "sale-pf-1",
                        course,
                        "student-pf-1",
                        80_000L,
                        LocalDateTime.parse("2025-03-22T11:00:00")
                )
        );

        refundRecordRepository.save(
                new RefundRecord(
                        "cancel-pf-1",
                        sale,
                        30_000L,
                        LocalDateTime.parse("2025-03-28T15:00:00")
                )
        );

        // when
        MonthlySettlementResponse response =
                settlementService.getMonthlySettlement("creator-pf", "2025-03");

        // then
        assertThat(response.creatorId()).isEqualTo("creator-pf");
        assertThat(response.creatorName()).isEqualTo("부분환불강사");
        assertThat(response.totalSalesAmount()).isEqualTo(80_000L);
        assertThat(response.refundAmount()).isEqualTo(30_000L);
        assertThat(response.netSalesAmount()).isEqualTo(50_000L);
        assertThat(response.feeRatePercent()).isEqualTo(20);
        assertThat(response.feeAmount()).isEqualTo(10_000L);
        assertThat(response.payoutAmount()).isEqualTo(40_000L);
        assertThat(response.salesCount()).isEqualTo(1L);
        assertThat(response.refundCount()).isEqualTo(1L);

    }

    // 테스트3,4 검증 시나리오: creator-2의 월 경계 취소 (1월에는 판매로, 2월에는 환불로 반영)
    @Test
    @DisplayName("creator-2의 2025-01 월별 정산은 1월 결제가 판매로 반영된다")
    void getMonthlySettlement_creator2_january() {
        // when
        MonthlySettlementResponse response =
                settlementService.getMonthlySettlement("creator-2", "2025-01");

        // then
        assertThat(response.creatorId()).isEqualTo("creator-2");
        assertThat(response.creatorName()).isEqualTo("이강사");
        assertThat(response.totalSalesAmount()).isEqualTo(60_000L);
        assertThat(response.refundAmount()).isEqualTo(0L);
        assertThat(response.netSalesAmount()).isEqualTo(60_000L);
        assertThat(response.feeRatePercent()).isEqualTo(20);
        assertThat(response.feeAmount()).isEqualTo(12_000L);
        assertThat(response.payoutAmount()).isEqualTo(48_000L);
        assertThat(response.salesCount()).isEqualTo(1L);
        assertThat(response.refundCount()).isEqualTo(0L);
    }

    @Test
    @DisplayName("creator-2의 2025-02 월별 정산은 2월 취소가 환불로 반영된다")
    void getMonthlySettlement_creator2_february() {
        // when
        MonthlySettlementResponse response =
                settlementService.getMonthlySettlement("creator-2", "2025-02");

        // then
        assertThat(response.creatorId()).isEqualTo("creator-2");
        assertThat(response.creatorName()).isEqualTo("이강사");
        assertThat(response.totalSalesAmount()).isEqualTo(0L);
        assertThat(response.refundAmount()).isEqualTo(60_000L);
        assertThat(response.netSalesAmount()).isEqualTo(-60_000L);
        assertThat(response.feeRatePercent()).isEqualTo(20);
        assertThat(response.feeAmount()).isEqualTo(-12_000L);
        assertThat(response.payoutAmount()).isEqualTo(-48_000L);
        assertThat(response.salesCount()).isEqualTo(0L);
        assertThat(response.refundCount()).isEqualTo(1L);
    }


   // 테스트5 검증 시나리오: creator-3의 빈 월 조회
    @Test
    @DisplayName("creator-3의 2025-03 월별 정산은 빈 월이라 0원으로 반환된다")
    void getMonthlySettlement_creator3_emptyMonth() {
        // when
        MonthlySettlementResponse response =
                settlementService.getMonthlySettlement("creator-3", "2025-03");

        // then
        assertThat(response.totalSalesAmount()).isEqualTo(0L);
        assertThat(response.refundAmount()).isEqualTo(0L);
        assertThat(response.netSalesAmount()).isEqualTo(0L);
        assertThat(response.feeAmount()).isEqualTo(0L);
        assertThat(response.payoutAmount()).isEqualTo(0L);
        assertThat(response.salesCount()).isEqualTo(0L);
        assertThat(response.refundCount()).isEqualTo(0L);
    }
}


