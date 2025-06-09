package com.unitask.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class DocumentPageRequest extends PageRequest {
    private String assessmentName;
    private String subjectName;
    private LocalDateTime beforeSubmissionDate;
    private LocalDateTime afterSubmissionDate;
}
