package com.unitask.service.impl;

import com.unitask.dao.AppUserDAO;
import com.unitask.dao.AssessmentDao;
import com.unitask.dao.AssessmentSubmissionDAO;
import com.unitask.dao.StudentAssessmentDao;
import com.unitask.dto.PageRequest;
import com.unitask.dto.assessment.AssessmentResponse;
import com.unitask.dto.assessment.AssessmentTuple;
import com.unitask.entity.StudentAssessment;
import com.unitask.entity.User.AppUser;
import com.unitask.entity.assessment.Assessment;
import com.unitask.entity.assessment.AssessmentSubmission;
import com.unitask.exception.ServiceAppException;
import com.unitask.mapper.AssessmentMapper;
import com.unitask.service.ContextService;
import com.unitask.service.StudentAssessmentService;
import com.unitask.util.OssUtil;
import com.unitask.util.PageUtil;
import com.unitask.util.PageWrapperVO;
import lombok.AllArgsConstructor;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class StudentAssessmentServiceImpl extends ContextService implements StudentAssessmentService {

    @Autowired
    private final StudentAssessmentDao studentAssessmentDao;
    private final AssessmentDao assessmentDao;
    @Autowired
    private final AppUserDAO appUserDAO;
    @Autowired
    private final AssessmentSubmissionDAO assessmentSubmissionDAO;
    @Autowired
    private final OssUtil ossUtil;

    @Override
    public PageWrapperVO getAssessmentListing(PageRequest pageRequest) {
        Pageable pageable = PageUtil.pageable(pageRequest);
        AppUser appUser = appUserDAO.findByEmail(getCurrentAuthUsername());
        Page<AssessmentTuple> studentAssessmentTuplePage = studentAssessmentDao.getAssessmentListing(pageRequest.getSearch(), pageable, appUser.getId());
        return new PageWrapperVO(studentAssessmentTuplePage, studentAssessmentTuplePage.getContent());
    }

    @Override
    public void submit(Long id, MultipartFile file) {
        AppUser appUser = appUserDAO.findByEmail(getCurrentAuthUsername());
        Optional<StudentAssessment> studentAssessmentOptional = studentAssessmentDao.findByAppUserAndAssessment(appUser.getId(), id);
        if (studentAssessmentOptional.isEmpty()) {
            throw new ServiceAppException(HttpStatus.BAD_REQUEST, "Student Assessment Not Found");
        }
        StudentAssessment studentAssessment = studentAssessmentOptional.get();
        AssessmentSubmission assessmentSubmission = new AssessmentSubmission();
        assessmentSubmission.setAssessment(studentAssessment.getAssessment());
        assessmentSubmission.setStudentAssessment(studentAssessment);
        assessmentSubmission.setSubmissionDate(LocalDateTime.now());
        if (studentAssessment.getGroup() != null) {
            assessmentSubmission.setGroup(studentAssessment.getGroup());
        }
        String extension = FilenameUtils.getExtension(file.getOriginalFilename());
        String uuid = UUID.randomUUID().toString();
        String path = "submission" + "/" + uuid + "." + extension;
        ossUtil.putObject(path, file);
        assessmentSubmission.setName(file.getOriginalFilename());
        assessmentSubmission.setPath(path);
        assessmentSubmission.setUuid(uuid);

        studentAssessment.setSubmissionDate(LocalDate.now());
        assessmentSubmissionDAO.save(assessmentSubmission);
        studentAssessmentDao.save(studentAssessment);
    }

    @Override
    public AssessmentResponse read(Long id) {
        AppUser appUser = appUserDAO.findByEmail(getCurrentAuthUsername());
        Assessment assessment = assessmentDao.findById(id);
        AssessmentResponse response = AssessmentMapper.INSTANCE.toResponse(assessment);
        response.setAttachedFile(ossUtil.toResponse(assessment.getAttachedFile()));

        Optional<StudentAssessment> studentAssessment = studentAssessmentDao.findByAppUserAndAssessment(appUser.getId(), assessment.getId());

        if (studentAssessment.isPresent()) {
            Optional<AssessmentSubmission> assessmentSubmission;
            if (studentAssessment.get().getGroup() != null) {
                assessmentSubmission = assessmentSubmissionDAO.findByGroupId(studentAssessment.get().getGroup().getId());
            } else {
                assessmentSubmission = assessmentSubmissionDAO.findByStudentAssessment(studentAssessment.get().getAssessment().getId());
            }

            if (assessmentSubmission.isPresent() && StringUtils.isNotBlank(assessmentSubmission.get().getPath())) {
                response.setSubmitted(true);
                response.setSubmissionFile(ossUtil.toResponse(assessmentSubmission.get().getPath(),
                        assessmentSubmission.get().getName(), assessmentSubmission.get().getCreatedDate()));
            }
        }

        return response;
    }
}
