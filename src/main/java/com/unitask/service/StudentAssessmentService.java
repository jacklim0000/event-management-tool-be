package com.unitask.service;

import com.unitask.dto.AssessmentSubmissionResponse;
import com.unitask.dto.PageRequest;
import com.unitask.dto.assessment.AssessmentResponse;
import com.unitask.util.PageWrapperVO;
import org.springframework.web.multipart.MultipartFile;


public interface StudentAssessmentService {

    PageWrapperVO getAssessmentListing(PageRequest pageRequest);

    void submit(Long id, MultipartFile file);

    AssessmentResponse read(Long id);

}
