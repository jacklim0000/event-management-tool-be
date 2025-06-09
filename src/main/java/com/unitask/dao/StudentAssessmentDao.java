package com.unitask.dao;

import com.unitask.constant.Enum.AssignmentMode;
import com.unitask.dto.assessment.AssessmentTuple;
import com.unitask.entity.StudentAssessment;
import com.unitask.exception.ServiceAppException;
import com.unitask.repository.StudentAssessmentRepository;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
public class StudentAssessmentDao {

    @Autowired
    private StudentAssessmentRepository studentAssessmentRepository;

    public List<StudentAssessment> saveAll(Collection<StudentAssessment> studentAssessmentList) {
        if (CollectionUtils.isEmpty(studentAssessmentList)) {
            return null;
        }
        return studentAssessmentRepository.saveAll(studentAssessmentList);
    }

    public Page<AssessmentTuple> getAssessmentListing(String search, Pageable pageable, Long userId) {
        return studentAssessmentRepository.findByAssessment_NameOrderByStatusDesc(search, pageable, userId);
    }

    public StudentAssessment findById(Long id) {
        return studentAssessmentRepository.findById(id).orElseThrow(() -> new ServiceAppException(HttpStatus.BAD_REQUEST, "Not found"));
    }

    public StudentAssessment save(StudentAssessment assessment) {
        return studentAssessmentRepository.save(assessment);
    }

    public List<StudentAssessment> findByAssignment(Long id) {
        return studentAssessmentRepository.findByAssessment_Id(id);
    }

    public Optional<StudentAssessment> findByAppUserAndAssessment(Long appUserId, Long assessmentId) {
        return studentAssessmentRepository.findByUser_IdAndAssessment_Id(appUserId, assessmentId);
    }


    public List<StudentAssessment> findByIds(Collection<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return new ArrayList<>();
        }
        return studentAssessmentRepository.findAllByIds(ids);
    }

    public List<StudentAssessment> findByAppUserAndGroupNull(Long id) {
        return studentAssessmentRepository.findByUser_IdAndGroupNull(id);
    }

    public List<StudentAssessment> findBySubject(Long id) {
        return studentAssessmentRepository.findByAssessment_Subject_Id(id);
    }

    public List<AssessmentTuple> findByGroupAssessment(Long appUser) {
        return studentAssessmentRepository.findByGroupAssessment(AssignmentMode.GROUP, appUser);
    }
}
