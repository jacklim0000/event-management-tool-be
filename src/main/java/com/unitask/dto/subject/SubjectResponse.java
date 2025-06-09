package com.unitask.dto.subject;


import com.unitask.constant.Enum.GeneralStatus;
import lombok.*;

import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class SubjectResponse {
    private Long id;
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
    private GeneralStatus status;
    private String color;
    private List<AssessmentDto> assessment;

}
