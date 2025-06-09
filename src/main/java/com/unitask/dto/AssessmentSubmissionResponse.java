package com.unitask.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AssessmentSubmissionResponse {


    private StudentAssessmentResponse studentAssessmentResponse;
    private String fileName;
    private String uuid;
    private String filePath;

}
