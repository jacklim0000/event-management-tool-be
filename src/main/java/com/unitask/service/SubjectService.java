package com.unitask.service;

import com.unitask.dto.PageRequest;
import com.unitask.dto.subject.SubjectRequest;
import com.unitask.dto.subject.SubjectResponse;
import com.unitask.dto.subject.SubjectTuple;
import com.unitask.util.PageWrapperVO;

public interface SubjectService {

    void create(SubjectRequest subjectRequest);

    void updateSubject(Long id, SubjectRequest subjectRequest);

    SubjectResponse getSubject(Long subjectId);

    PageWrapperVO<SubjectTuple> getListing(PageRequest pageRequest, Long subjectId);

}
