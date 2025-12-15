package com.cgi.parizek.matej.repository;

import com.cgi.parizek.matej.entity.Mission;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;


@Repository
public interface IMissionRepository extends JpaRepository<Mission, Long>, JpaSpecificationExecutor<Mission> {

    @EntityGraph(attributePaths = {"rewards", "tags"})
    Page<Mission> findByPlayerId(Long playerId, Pageable pageable);
}

