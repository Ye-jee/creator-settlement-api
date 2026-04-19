package com.assignment.creator_settlement_api.domain.course.repository;

import com.assignment.creator_settlement_api.domain.course.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CourseRepository extends JpaRepository<Course, String> {

    // 크리에이터별
    List<Course> findByCreatorId(String creatorId);
}
