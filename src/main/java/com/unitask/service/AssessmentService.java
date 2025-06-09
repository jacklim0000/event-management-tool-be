package com.unitask.service;

import com.unitask.dto.PageRequest;
import com.unitask.dto.assessment.AssessmentRequest;
import com.unitask.dto.assessment.AssessmentResponse;
import com.unitask.dto.assessment.AssessmentTuple;
import com.unitask.dto.subject.AssessmentDto;
import com.unitask.entity.Subject;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface AssessmentService {

    AssessmentResponse read(Long id);

    Page<AssessmentTuple> getListing(PageRequest pageRequest);

    void update(Subject subject, List<AssessmentDto> dtos);

    void update(Long id, AssessmentRequest assessmentRequest);

    void uploadFile(Long id, MultipartFile multipartFile);

    void deleteFile(Long fileId);
}
