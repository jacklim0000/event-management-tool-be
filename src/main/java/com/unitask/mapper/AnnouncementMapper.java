package com.unitask.mapper;

import com.unitask.dto.annoucement.AnnouncementRequest;
import com.unitask.dto.annoucement.AnnouncementResponse;
import com.unitask.entity.Announcement;
import com.unitask.entity.Subject;
import com.unitask.entity.User.AppUser;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper
public interface AnnouncementMapper {

    AnnouncementMapper INSTANCE = Mappers.getMapper(AnnouncementMapper.class);

    @Mapping(target = "postedDate", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "owner", source = "appUser")
    @Mapping(target = "subject", source = "subject")
    @Mapping(target = "description", source = "announcementRequest.description")
    Announcement toEntity(AnnouncementRequest announcementRequest, Subject subject, AppUser appUser);

    @Mapping(target = "postedDate", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "subject", source = "subject")
    @Mapping(target = "description", source = "announcementRequest.description")
    void toEntity(@MappingTarget Announcement announcement, AnnouncementRequest announcementRequest, Subject subject);

    @Mapping(target = "color", source = "subject.color")
    @Mapping(target = "subjectId", source = "subject.id")
    @Mapping(target = "subjectName", source = "subject.name")
    @Mapping(target = "subjectCode", source = "subject.code")
    @Mapping(target = "lecturerName", source = "owner.name")
    AnnouncementResponse toResponse(Announcement announcement);
}
