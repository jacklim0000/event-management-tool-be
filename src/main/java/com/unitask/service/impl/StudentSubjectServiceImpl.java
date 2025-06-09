package com.unitask.service.impl;

import com.unitask.constant.Enum.GeneralStatus;
import com.unitask.dao.AppUserDAO;
import com.unitask.dao.StudentAssessmentDao;
import com.unitask.dao.StudentSubjectDAO;
import com.unitask.dao.SubjectDAO;
import com.unitask.dto.PageRequest;
import com.unitask.dto.assessment.AssessmentTuple;
import com.unitask.dto.studentSubject.StudentSubjectResponse;
import com.unitask.dto.studentSubject.StudentSubjectTuple;
import com.unitask.entity.assessment.Assessment;
import com.unitask.entity.StudentAssessment;
import com.unitask.entity.StudentSubject;
import com.unitask.entity.Subject;
import com.unitask.entity.User.AppUser;
import com.unitask.exception.ServiceAppException;
import com.unitask.mapper.StudentSubjectMapper;
import com.unitask.service.ContextService;
import com.unitask.service.StudentSubjectService;
import com.unitask.util.PageUtil;
import com.unitask.util.PageWrapperVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class StudentSubjectServiceImpl extends ContextService implements StudentSubjectService {

    @Autowired
    private StudentSubjectDAO studentSubjectDAO;
    @Autowired
    private SubjectDAO subjectDAO;
    @Autowired
    private AppUserDAO appUserDAO;
    @Autowired
    private StudentAssessmentDao studentAssessmentDao;

    public Page<StudentSubjectTuple> getListing(PageRequest pageRequest) {
        Pageable pageable = PageUtil.pageable(pageRequest);
        return studentSubjectDAO.findByStudentEmail(pageable, getCurrentAuthUsername(), pageRequest.getSearch());
    }

    @Override
    public StudentSubjectResponse get(Long subjectId) {
        Subject subject = subjectDAO.findById(subjectId);
        Optional<StudentSubject> studentSubject = studentSubjectDAO.findByStudentEmailAndSubjectId(getCurrentAuthUsername(), subject.getId());
        if (studentSubject.isPresent()) {
            return StudentSubjectMapper.INSTANCE.toResponse(studentSubject.get());
        }else{
            return StudentSubjectMapper.INSTANCE.toResponse(subject);
        }
    }

    @Override
    public String enroll(Long subjectId) {
        AppUser appUser = appUserDAO.findByEmail(getCurrentAuthUsername());
        if (appUser == null) {
            throw new ServiceAppException(HttpStatus.BAD_REQUEST, "User not Found");
        }
        Subject subject = subjectDAO.findById(subjectId);
        if (subject == null) {
            throw new ServiceAppException(HttpStatus.BAD_REQUEST, "Subject Not Found");
        }
        StudentSubject studentSubject = new StudentSubject();
        studentSubject.setUser(appUser);
        studentSubject.setSubject(subject);
        studentSubject.setEnrollmentDate(LocalDate.now());
        studentSubject.setStatus(GeneralStatus.ACTIVE);
        studentSubjectDAO.save(studentSubject);
        List<Assessment> assessmentList = subject.getAssessment();
        if (!CollectionUtils.isEmpty(assessmentList)) {
            studentAssessmentDao.saveAll(
                    assessmentList.stream().map(ass -> {
                        StudentAssessment assessment = new StudentAssessment();
                        assessment.setUser(appUser);
                        assessment.setAssessment(ass);
                        assessment.setEnrollmentDate(LocalDate.now());
                        assessment.setStatus(GeneralStatus.ACTIVE);
                        return assessment;
                    }).toList());
        }
        return "OK";
    }
}
