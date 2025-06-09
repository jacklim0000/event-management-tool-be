package com.unitask.dto.task;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TaskRequest {

    private String name;
    private Long assignmentId;
    private Long assignedId;
    private Boolean checked;

}
