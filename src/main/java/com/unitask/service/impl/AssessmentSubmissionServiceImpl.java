package com.unitask.service.impl;

import com.unitask.constant.Enum.AssignmentMode;
import com.unitask.constant.Enum.GeneralStatus;
import com.unitask.dao.AppUserDAO;
import com.unitask.dao.AssessmentSubmissionDAO;
import com.unitask.dao.StudentAssessmentDao;
import com.unitask.dto.AssessmentSubmissionPageRequest;
import com.unitask.dto.PageRequest;
import com.unitask.dto.assessment.AssessmentGradeRequest;
import com.unitask.dto.assessment.AssessmentSubmissionResponse;
import com.unitask.dto.assessment.AssessmentSubmissionTuple;
import com.unitask.entity.StudentAssessment;
import com.unitask.entity.User.AppUser;
import com.unitask.entity.assessment.AssessmentSubmission;
import com.unitask.mapper.AssessmentMapper;
import com.unitask.service.AssessmentSubmissionService;
import com.unitask.service.ContextService;
import com.unitask.util.OssUtil;
import com.unitask.util.PageUtil;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Set;

@Service
@AllArgsConstructor
public class AssessmentSubmissionServiceImpl extends ContextService implements AssessmentSubmissionService {

    private final AssessmentSubmissionDAO assessmentSubmissionDAO;
    private final StudentAssessmentDao studentAssessmentDao;
    private final AppUserDAO appUserDAO;
    private final OssUtil ossUtil;

    @Override
    public Page<AssessmentSubmissionTuple> getListing(AssessmentSubmissionPageRequest assessmentSubmissionPageRequest) {
        PageRequest pageRequest = new PageRequest(assessmentSubmissionPageRequest.getPage(), assessmentSubmissionPageRequest.getPageSize(), assessmentSubmissionPageRequest.getSearch(), assessmentSubmissionPageRequest.getSort());
        AppUser appUser = appUserDAO.findByEmail(getCurrentAuthUsername());
        Pageable pageable = PageUtil.pageable(pageRequest);
        return assessmentSubmissionDAO.getAssessmentSubmissionListing(appUser.getId(), assessmentSubmissionPageRequest.getAssignmentName(), assessmentSubmissionPageRequest.getGroupName(), assessmentSubmissionPageRequest.getSubjectCode(), assessmentSubmissionPageRequest.getBeforeDate(), assessmentSubmissionPageRequest.getAfterDate(), pageable);
    }

    @Override
    public AssessmentSubmissionResponse read(Long id) {
        AssessmentSubmission assessmentSubmission = assessmentSubmissionDAO.findById(id);
        AssessmentSubmissionResponse response = AssessmentMapper.INSTANCE.toResponse(assessmentSubmission);
        response.getAssessment().setAttachedFile(ossUtil.toResponse(assessmentSubmission.getAssessment().getAttachedFile()));
        response.setSubmittedDocuments(Arrays.asList(ossUtil.toResponse(assessmentSubmission.getPath(),
                assessmentSubmission.getName(), assessmentSubmission.getCreatedDate())));
        return response;
    }

    @Override
    public String resubmit(Long id) {
        AssessmentSubmission assessmentSubmission = assessmentSubmissionDAO.findById(id);
        if (AssignmentMode.GROUP.equals(assessmentSubmission.getAssessment().getAssignmentMode())) {
            Set<StudentAssessment> studentAssessmentSet = assessmentSubmission.getGroup().getStudentAssessment();
            studentAssessmentSet.forEach(x -> x.setStatus(GeneralStatus.ACTIVE));
            studentAssessmentDao.saveAll(studentAssessmentSet);
        } else if (AssignmentMode.INDIVIDUAL.equals(assessmentSubmission.getAssessment().getAssignmentMode())) {
            StudentAssessment studentAssessment = assessmentSubmission.getStudentAssessment();
            studentAssessment.setStatus(GeneralStatus.ACTIVE);
            studentAssessmentDao.save(studentAssessment);
        }
        ossUtil.removeObject(assessmentSubmission.getPath());
        assessmentSubmissionDAO.delete(assessmentSubmission);
        return HttpStatus.OK.getReasonPhrase();
    }

    @Override
    public String grade(Long id, AssessmentGradeRequest request) {
        StudentAssessment studentAssessment = studentAssessmentDao.findById(id);
        studentAssessment.setScore(request.getScore());
        studentAssessmentDao.save(studentAssessment);
        return HttpStatus.OK.getReasonPhrase();
    }
}
