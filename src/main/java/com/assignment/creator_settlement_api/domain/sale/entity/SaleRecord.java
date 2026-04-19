package com.assignment.creator_settlement_api.domain.sale.entity;

import com.assignment.creator_settlement_api.domain.course.entity.Course;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SaleRecord {

    @Id
    @Column(nullable = false, length = 50)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @Column(nullable = false, length = 50)
    private String studentId;

    @Column(nullable = false)
    private Long amount;

    @Column(nullable = false)
    private LocalDateTime paidAt;

    public SaleRecord(String id, Course course, String studentId, Long amount, LocalDateTime paidAt) {
        this.id = id;
        this.course = course;
        this.studentId = studentId;
        this.amount = amount;
        this.paidAt = paidAt;
    }
}
