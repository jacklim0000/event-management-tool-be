package com.unitask.mapper;

import com.unitask.dto.task.TaskResponse;
import com.unitask.entity.Task;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface TaskMapper {

    TaskMapper INSTANCE = Mappers.getMapper(TaskMapper.class);

    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "userName", source = "user.name")
    @Mapping(target = "assessmentId", source = "assessment.id")
    @Mapping(target = "assessmentName", source = "assessment.name")
    TaskResponse entityToResponse(Task task);

    @IterableMapping(elementTargetType = TaskResponse.class)
    List<TaskResponse> entityListToResponseList(List<Task> tasks);

}
