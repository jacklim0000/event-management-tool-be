package com.unitask.dto.studentSubject;

import com.unitask.constant.Enum.GeneralStatus;
import com.unitask.dto.subject.SubjectResponse;
import lombok.Data;

import java.time.LocalDate;

@Data
public class StudentSubjectResponse {

    private Long id;

    private LocalDate enrollmentDate;

    private GeneralStatus status;

    private SubjectResponse subject;
}
