package com.unitask.dto;

import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
public class AssessmentSubmissionPageRequest {

    private String assignmentName;
    private String groupName;
    private String subjectCode;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime beforeDate;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime afterDate;
    @Min(value = 1, message = "Cannot be less than {value}.")
    private int page = 1;

    @Min(value = 1, message = "Cannot be less than {value}.")
    private int pageSize = 10;

    private String search;

    private List<String> sort;
}
