package com.unitask.service.impl;

import com.unitask.dao.AppUserDAO;
import com.unitask.dao.SubjectDAO;
import com.unitask.dto.PageRequest;
import com.unitask.dto.subject.SubjectRequest;
import com.unitask.dto.subject.SubjectResponse;
import com.unitask.dto.subject.SubjectTuple;
import com.unitask.entity.Subject;
import com.unitask.entity.User.AppUser;
import com.unitask.exception.ServiceAppException;
import com.unitask.mapper.SubjectMapper;
import com.unitask.service.AssessmentService;
import com.unitask.service.ContextService;
import com.unitask.service.SubjectService;
import com.unitask.util.PageUtil;
import com.unitask.util.PageWrapperVO;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class SubjectServiceImpl extends ContextService implements SubjectService {

    private final AssessmentService assessmentService;
    private final SubjectDAO subjectDAO;
    private final AppUserDAO appUserDAO;

    @Override
    public void create(SubjectRequest subjectRequest) {
        AppUser appUser = appUserDAO.findByEmail(getCurrentAuthUsername());
        Subject subject = SubjectMapper.INSTANCE.toEntity(subjectRequest, appUser);
        subjectDAO.save(subject);
        assessmentService.update(subject, subjectRequest.getAssessment());
    }

    @Override
    public void updateSubject(Long id, SubjectRequest subjectRequest) {
        Subject subject = subjectDAO.findById(id);
        if (subject == null) {
            throw new ServiceAppException(HttpStatus.BAD_REQUEST, "Subject not Found");
        }
        SubjectMapper.INSTANCE.toEntity(subject, subjectRequest);


        subjectDAO.save(subject);
        assessmentService.update(subject, subjectRequest.getAssessment());
    }

    @Override
    public SubjectResponse getSubject(Long subjectId) {
        Subject subject = subjectDAO.findById(subjectId);
        if (subject == null) {
            throw new ServiceAppException(HttpStatus.BAD_REQUEST, "Subject not Found");
        }
        return SubjectMapper.INSTANCE.toResponse(subject);
    }

    @Override
    public PageWrapperVO<SubjectTuple> getListing(PageRequest pageRequest, Long requestId) {
        String email = getCurrentAuthUsername();
        Pageable pageable = PageUtil.pageable(pageRequest);
        Page<SubjectTuple> subjectList = subjectDAO.findListing(pageable, email, pageRequest.getSearch(),requestId);
        return new PageWrapperVO<SubjectTuple>(subjectList, subjectList.getContent());
    }
}
