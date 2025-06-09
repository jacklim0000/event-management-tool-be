package com.unitask.dto.assessment;

import lombok.Data;

@Data
public class AssessmentMarkingRubricDto {

    private Long id;
    private String criteria;
    private String weightage;
}
