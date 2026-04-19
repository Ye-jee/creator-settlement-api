package com.assignment.creator_settlement_api.domain.creator.repository;

import com.assignment.creator_settlement_api.domain.creator.entity.Creator;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CreatorRepository extends JpaRepository<Creator, String> {

    // 기본 CRUD
}
