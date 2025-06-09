package com.unitask.dto.assessment;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Data
public class AssessmentRequest {

    private String name;
    private String weightage;
    private String assignmentMode;
    private LocalDate dueDate;
    private String lecturerInstruction;
    private Set<AssessmentMarkingRubricDto> assessmentMarkingRubrics;
    private LocalDate startDate;
    private Long maxMember;

}
