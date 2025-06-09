package com.unitask.dto.subject;

import com.unitask.constant.Enum.AssignmentMode;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class AssessmentDto {
    private Long id;
    private String name;
    private AssignmentMode assignmentMode;
    private String weightage;
}
