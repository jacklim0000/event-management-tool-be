package com.unitask.dto.assessment;

import java.time.LocalDateTime;

public interface AssessmentSubmissionTuple {

    Long getId();

    String getSubjectCode();

    String getAssignmentName();

    String getGroupName();

    LocalDateTime getSubmissionDate();
}
