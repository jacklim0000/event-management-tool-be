package com.unitask.dto.annoucement;

import java.time.LocalDate;
import java.time.LocalDateTime;

public interface AnnouncementTuple {

    Long getId();

    String getTitle();

    String getColor();

    String getSubjectCode();

    String getSubjectName();

    String getLecturerName();

    String getDescription();

    LocalDateTime getPostedDate();
}
