package com.unitask.mapper;

import com.unitask.dto.GroupMemberListDto;
import com.unitask.dto.StudentAssessmentResponse;
import com.unitask.dto.group.GroupResponse;
import com.unitask.entity.Group;
import com.unitask.entity.StudentAssessment;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.Collection;
import java.util.List;

@Mapper
public interface GroupMapper {

    GroupMapper INSTANCE = Mappers.getMapper(GroupMapper.class);

    @Mapping(target = "groupMembers", source = "group.studentAssessment", qualifiedByName = "toMembers")
    GroupResponse toResponse(Group group);

    @Named("toMembers")
    @IterableMapping(qualifiedByName = "toMember")
    List<GroupMemberListDto> toMembers(Collection<StudentAssessment> studentAssessments);

    @Named("toMember")
    @Mapping(target = "name", source = "user.name")
    GroupMemberListDto toMember(StudentAssessment studentAssessment);

    @Mapping(target = "name", source = "user.name")
    StudentAssessmentResponse toResponse(StudentAssessment studentAssessment);
}

