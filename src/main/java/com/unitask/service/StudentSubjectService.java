package com.unitask.service;

import com.unitask.dto.PageRequest;
import com.unitask.dto.studentSubject.StudentSubjectResponse;
import com.unitask.dto.studentSubject.StudentSubjectTuple;
import com.unitask.util.PageWrapperVO;
import org.springframework.data.domain.Page;

import java.util.List;

public interface StudentSubjectService {

    Page<StudentSubjectTuple> getListing(PageRequest pageRequest);
    StudentSubjectResponse get(Long subjectId);

    String enroll(Long subjectId);
}
