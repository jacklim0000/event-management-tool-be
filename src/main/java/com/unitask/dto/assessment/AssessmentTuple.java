package com.unitask.dto.assessment;

import java.time.LocalDate;

public interface AssessmentTuple {

    Long getId();

    String getName();

    LocalDate getDueDate();

    String getStatus();

    String getSubjectName();

    String getSubjectCode();

    Long getMaxNumber();

    String getColor();

}
