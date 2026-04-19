package com.assignment.creator_settlement_api.infra;

import com.assignment.creator_settlement_api.domain.course.entity.Course;
import com.assignment.creator_settlement_api.domain.course.repository.CourseRepository;
import com.assignment.creator_settlement_api.domain.creator.entity.Creator;
import com.assignment.creator_settlement_api.domain.creator.repository.CreatorRepository;
import com.assignment.creator_settlement_api.domain.refund.entity.RefundRecord;
import com.assignment.creator_settlement_api.domain.refund.repository.RefundRecordRepository;
import com.assignment.creator_settlement_api.domain.sale.entity.SaleRecord;
import com.assignment.creator_settlement_api.domain.sale.repository.SaleRecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Transactional
@ConditionalOnProperty(prefix = "app.seed", name = "enabled", havingValue = "true")
public class SampleDataInitializer implements CommandLineRunner {

    private final ObjectMapper objectMapper;
    private final CreatorRepository creatorRepository;
    private final CourseRepository courseRepository;
    private final SaleRecordRepository saleRecordRepository;
    private final RefundRecordRepository refundRecordRepository;

    @Override
    public void run(String... args) throws Exception {
        if (creatorRepository.count() > 0) {        // 중복 삽입 방지
            return;
        }

        SampleDataDto sampleData = loadSampleData();

        Map<String, Creator> creatorMap = seedCreators(sampleData.creators());
        Map<String, Course> courseMap = seedCourses(sampleData.courses(), creatorMap);
        Map<String, SaleRecord> saleRecordMap = seedSaleRecords(sampleData.saleRecords(), courseMap);
        seedRefundRecords(sampleData.refundRecords(), saleRecordMap);
    }


    // JSON 파일 -> 자바 객체
    private SampleDataDto loadSampleData() throws Exception {
        ClassPathResource resource = new ClassPathResource("sample-data/sample-data.json");
        try (InputStream inputStream = resource.getInputStream()) {
            return objectMapper.readValue(inputStream, SampleDataDto.class);
        }
    }

    // 크리에이터부터 저장
    private Map<String, Creator> seedCreators(List<SampleDataDto.CreatorSeed> creators) {

        Map<String, Creator> creatorMap = new HashMap<>();

        for (SampleDataDto.CreatorSeed seed : creators) {
            Creator creator = new Creator(seed.id(), seed.name());
            creatorRepository.save(creator);
            creatorMap.put(seed.id(), creator);
        }

        return creatorMap;
    }

    // 크리에이터를 참조해서 강의를 저장
    private Map<String, Course> seedCourses(
            List<SampleDataDto.CourseSeed> courses, Map<String, Creator> creatorMap) {

            Map<String, Course> courseMap = new HashMap<>();

            for (SampleDataDto.CourseSeed seed : courses) {
                Creator creator = creatorMap.get(seed.creatorId());
                Course course = new Course(seed.id(), creator, seed.title());
                courseRepository.save(course);
                courseMap.put(seed.id(), course);
            }

            return courseMap;
    }

    // 강의를 참조해 판매 내역 저장
    private Map<String, SaleRecord> seedSaleRecords(
            List<SampleDataDto.SaleRecordSeed> saleRecords, Map<String, Course> courseMap) {
        Map<String, SaleRecord> saleRecordMap = new HashMap<>();

        for (SampleDataDto.SaleRecordSeed seed : saleRecords) {
            Course course = courseMap.get(seed.courseId());
            LocalDateTime paidAt = seed.paidAt().toLocalDateTime();

            SaleRecord saleRecord = new SaleRecord(
                    seed.id(),
                    course,
                    seed.studentId(),
                    seed.amount(),
                    paidAt
            );

            saleRecordRepository.save(saleRecord);
            saleRecordMap.put(seed.id(), saleRecord);
        }

        return saleRecordMap;
    }

    // 원본 판매 내역을 포함해 환불 내역 저장
    private void seedRefundRecords(
            List<SampleDataDto.RefundRecordSeed> refundRecords, Map<String, SaleRecord> saleRecordMap) {

        if (refundRecords == null) {
            return;
        }

        for (SampleDataDto.RefundRecordSeed seed : refundRecords) {
            SaleRecord saleRecord = saleRecordMap.get(seed.saleRecordId());
            LocalDateTime refundedAt = seed.refundedAt().toLocalDateTime();

            RefundRecord refundRecord = new RefundRecord(
                    seed.id(),
                    saleRecord,
                    seed.refundAmount(),
                    refundedAt
            );

            refundRecordRepository.save(refundRecord);
        }
    }
}

