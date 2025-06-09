package com.unitask.service;

import com.unitask.dto.PageRequest;
import com.unitask.dto.group.DropdownResponse;
import com.unitask.dto.group.GroupRequest;
import com.unitask.dto.group.GroupResponse;
import com.unitask.dto.group.GroupTuple;
import org.springframework.data.domain.Page;

import java.util.List;

public interface GroupService {

    void createGroup(GroupRequest groupRequest);

    void updateGroup(Long id, GroupRequest groupRequest);

    GroupResponse getGroup(Long id);

    Page<GroupTuple> getList(PageRequest pageRequest);

    List<DropdownResponse> getStudentListing();

    List<DropdownResponse> getStudentAssignmentDropdown(Long id);

}
