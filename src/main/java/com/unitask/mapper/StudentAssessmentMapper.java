package com.unitask.mapper;

import com.unitask.dto.StudentAssessmentResponse;
import com.unitask.entity.StudentAssessment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface StudentAssessmentMapper {

    StudentAssessmentMapper INSTANCE = Mappers.getMapper(StudentAssessmentMapper.class);

    @Mapping(target = "name", source = "user.name")
    StudentAssessmentResponse getResponse(StudentAssessment studentAssessment);

}
