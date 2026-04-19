package com.assignment.creator_settlement_api.domain.course.entity;

import com.assignment.creator_settlement_api.domain.creator.entity.Creator;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Course {

    @Id
    @Column(nullable = false, length = 50)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "creator_id", nullable = false)
    private Creator creator;

    @Column(nullable = false, length = 200)
    private String title;

    public Course(String id, Creator creator, String title) {
        this.id = id;
        this.creator = creator;
        this.title = title;
    }
}
