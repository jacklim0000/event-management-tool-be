package com.unitask.dto.assessment;

import com.unitask.dto.FileResponse;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class AssessmentSubmissionResponse {

    private Long id;
    private String name;
    private AssessmentResponse assessment;
    private List<AssessmentSubmissionMemberResponse> groupMember;
    private List<FileResponse> submittedDocuments;
    private LocalDateTime submissionDate;
}
