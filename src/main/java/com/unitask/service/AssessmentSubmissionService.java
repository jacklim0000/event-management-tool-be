package com.unitask.service;

import com.unitask.dto.AssessmentSubmissionPageRequest;
import com.unitask.dto.PageRequest;
import com.unitask.dto.assessment.AssessmentGradeRequest;
import com.unitask.dto.assessment.AssessmentSubmissionResponse;
import com.unitask.dto.assessment.AssessmentSubmissionTuple;
import org.springframework.data.domain.Page;

public interface AssessmentSubmissionService {

    Page<AssessmentSubmissionTuple> getListing(AssessmentSubmissionPageRequest pageRequest);

    AssessmentSubmissionResponse read(Long id);

    String resubmit(Long id);

    String grade(Long id, AssessmentGradeRequest request);
}
