package com.unitask.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class DocumentResponse {

    private Long id;
    private String name;
    private String assessmentName;
    private String subjectName;
    private LocalDateTime submissionDate;
    private String uuid;
    private String path;
    private String fileName;

}
