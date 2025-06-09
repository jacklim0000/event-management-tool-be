package com.unitask.dto;

import com.unitask.constant.Enum.GeneralStatus;
import com.unitask.dto.assessment.AssessmentResponse;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class StudentAssessmentResponse {

    private Long id;
    private String name;
    private AssessmentResponse assessment;
    private LocalDate enrollmentDate;
    private GeneralStatus status;
    private LocalDate submissionDate;
    private String score;

}
