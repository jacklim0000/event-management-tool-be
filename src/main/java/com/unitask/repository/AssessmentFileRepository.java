package com.unitask.repository;

import com.unitask.entity.assessment.AssessmentFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AssessmentFileRepository extends JpaRepository<AssessmentFile, Long> {
}
