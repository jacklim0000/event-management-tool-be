package com.unitask.dto.assessment;

import com.unitask.constant.Enum.AssignmentMode;
import com.unitask.dto.FileResponse;
import com.unitask.dto.subject.SubjectResponse;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class AssessmentResponse {

    private Long id;
    private String name;
    private String weightage;
    private SubjectResponse subject;
    private AssignmentMode assignmentMode;
    private LocalDate dueDate;
    private String lecturerInstruction;
    private List<AssessmentMarkingRubricDto> assessmentMarkingRubrics;
    private List<FileResponse> attachedFile;
    private Long maxMember;
    private LocalDate startDate;

    private FileResponse submissionFile;
    private Boolean submitted;
}
