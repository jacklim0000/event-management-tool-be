package com.unitask.dto.annoucement;

import lombok.Data;

@Data
public class AnnouncementRequest {

    private String title;

    private String description;

    private Long subjectId;
}
