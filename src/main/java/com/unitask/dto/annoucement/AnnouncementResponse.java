package com.unitask.dto.annoucement;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AnnouncementResponse {
    private Long id;
    private String title;
    private String description;
    private String subjectName;
    private String subjectCode;
    private String lecturerName;
    private LocalDateTime postedDate;
    private String color;
    private Long subjectId;
}
