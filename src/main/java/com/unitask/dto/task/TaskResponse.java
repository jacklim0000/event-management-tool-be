package com.unitask.dto.task;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TaskResponse {

    private Long id;
    private String name;
    private String userId;
    private String userName;
    private String assessmentName;
    private String assessmentId;
    private Boolean checked;

}
