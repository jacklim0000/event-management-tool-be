package com.unitask.dto.subject;

import com.unitask.constant.Enum.GeneralStatus;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SubjectRequest {

    private String code;
    private String name;
    private String course;
    private Integer creditHour;
    private String description;
    private String learningOutcome;
    private String lecturerName;
    private String lecturerContact;
    private String lecturerEmail;
    private String lecturerOffice;
    private List<AssessmentDto> assessment;
    private GeneralStatus status;
    private String color;

}
