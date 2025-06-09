package com.unitask.mapper;

import com.unitask.dto.studentSubject.StudentSubjectResponse;
import com.unitask.entity.StudentSubject;
import com.unitask.entity.Subject;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface StudentSubjectMapper {

    StudentSubjectMapper INSTANCE = Mappers.getMapper(StudentSubjectMapper.class);

    StudentSubjectResponse toResponse(StudentSubject studentSubject);

    @Mapping(target = "subject", source = "subject")
    @Mapping(target = "enrollmentDate", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", ignore = true)
    StudentSubjectResponse toResponse(Subject subject);

}
