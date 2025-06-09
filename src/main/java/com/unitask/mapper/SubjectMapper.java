package com.unitask.mapper;

import com.unitask.dto.subject.SubjectRequest;
import com.unitask.dto.subject.SubjectResponse;
import com.unitask.entity.Subject;
import com.unitask.entity.User.AppUser;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface SubjectMapper {

    SubjectMapper INSTANCE = Mappers.getMapper(SubjectMapper.class);

    @Mapping(target = "owner", source = "appUser")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "name", source = "subjectRequest.name")
    Subject toEntity(SubjectRequest subjectRequest, AppUser appUser);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "owner", ignore = true)
    @Mapping(target = "name", source = "subjectRequest.name")
    void toEntity(@MappingTarget Subject subject, SubjectRequest subjectRequest);

    @Named("toResponse")
    SubjectResponse toResponse(Subject subject);

    @IterableMapping(qualifiedByName = "toResponse")
    List<SubjectResponse> toResponse(List<Subject> subject);

}
