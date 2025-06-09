package com.unitask.dto.group;

import com.unitask.dto.GroupMemberListDto;
import com.unitask.dto.StudentAssessmentResponse;
import com.unitask.dto.assessment.AssessmentResponse;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class GroupResponse {

    private Long id;
    private String name;
    private String description;
    private AssessmentResponse assessment;
    private List<GroupMemberListDto> groupMembers;
    private Boolean locked;
    private Boolean openForPublic;
}
