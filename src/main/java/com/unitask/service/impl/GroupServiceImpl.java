package com.unitask.service.impl;

import com.unitask.dao.AppUserDAO;
import com.unitask.dao.AssessmentDao;
import com.unitask.dao.GroupDao;
import com.unitask.dao.StudentAssessmentDao;
import com.unitask.dto.PageRequest;
import com.unitask.dto.group.DropdownResponse;
import com.unitask.dto.group.GroupRequest;
import com.unitask.dto.group.GroupResponse;
import com.unitask.dto.group.GroupTuple;
import com.unitask.entity.Group;
import com.unitask.entity.StudentAssessment;
import com.unitask.entity.User.AppUser;
import com.unitask.entity.assessment.Assessment;
import com.unitask.exception.ServiceAppException;
import com.unitask.mapper.GroupMapper;
import com.unitask.service.ContextService;
import com.unitask.service.GroupService;
import com.unitask.util.PageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class GroupServiceImpl extends ContextService implements GroupService {

    @Autowired
    private GroupDao groupDao;

    @Autowired
    private AppUserDAO appUserDAO;
    @Autowired
    private AssessmentDao assessmentDao;
    @Autowired
    private StudentAssessmentDao studentAssessmentDao;

    @Override
    public void createGroup(GroupRequest groupRequest) {
        Assessment assessment = assessmentDao.findById(groupRequest.getAssessmentId());
        Group group = new Group();
        group.setName(groupRequest.getName());
        group.setDescription(groupRequest.getDescription());
        group.setAssessment(assessment);
        Group savedGroup = groupDao.save(group);

        List<StudentAssessment> studentAssessmentList = studentAssessmentDao.findByIds(groupRequest.getMembers());
        if (!CollectionUtils.isEmpty(studentAssessmentList)) {
            studentAssessmentDao.saveAll(studentAssessmentList.stream().peek(studentAss -> studentAss.setGroup(savedGroup)).toList());
        }
    }

    @Override
    public void updateGroup(Long id, GroupRequest groupRequest) {
        Map<Long, StudentAssessment> request = studentAssessmentDao.findByIds(groupRequest.getMembers()).stream().collect(Collectors.toMap(StudentAssessment::getId, x -> x));
        if (CollectionUtils.isEmpty(request.keySet())) {
            throw new ServiceAppException(HttpStatus.BAD_REQUEST, "Group must have at least one member");
        }
        Group group = groupDao.findById(id);
        if (group == null) {
            throw new ServiceAppException(HttpStatus.BAD_REQUEST, "Group does not Exists");
        }
        group.setName(groupRequest.getName());
        group.setDescription(groupRequest.getDescription());
        group.setOpenForPublic(groupRequest.getOpenForPublic());
        group.setLocked(groupRequest.getLocked());
        groupDao.save(group);

        Set<StudentAssessment> studentAssessmentSet = group.getStudentAssessment();
        Map<Long, StudentAssessment> existingValue = studentAssessmentSet.stream().collect(Collectors.toMap(StudentAssessment::getId, x -> x));

        List<StudentAssessment> studentAssessmentList = new ArrayList<>();
        groupRequest.getMembers().stream()
                .filter(Objects::nonNull)
                .forEach(memberId -> {
                    StudentAssessment studentAssessment;
                    if (existingValue.containsKey(memberId)) {
                        //old member
                        existingValue.remove(memberId);
                    } else {
                        //new member
                        studentAssessment = request.get(memberId);
                        studentAssessment.setGroup(group);
                        studentAssessmentList.add(studentAssessment);
                    }
                });
        studentAssessmentList.addAll(existingValue.values().stream().peek(studentAss -> studentAss.setGroup(null)).toList());
        studentAssessmentDao.saveAll(studentAssessmentList);
    }

    @Override
    public GroupResponse getGroup(Long id) {
        Group group = groupDao.findById(id);
        if (group == null) {
            throw new ServiceAppException(HttpStatus.BAD_REQUEST, "Group does not Exists");
        }
        return GroupMapper.INSTANCE.toResponse(group);
    }

    @Override
    public Page<GroupTuple> getList(PageRequest pageRequest) {
        Pageable pageable = PageUtil.pageable(pageRequest);
        AppUser appUser = appUserDAO.findByEmail(getCurrentAuthUsername());
        Page<GroupTuple> groupAssessmentTuple = groupDao.getList(pageRequest.getSearch(), pageable, appUser.getId());
        return groupAssessmentTuple;
    }

    @Override
    public List<DropdownResponse> getStudentListing() {
        return appUserDAO.findStudents().stream().map(user -> {
            DropdownResponse dropdownResponse = new DropdownResponse();
            dropdownResponse.setId(user.getId());
            dropdownResponse.setName(user.getName());
            return dropdownResponse;
        }).toList();
    }

    @Override
    public List<DropdownResponse> getStudentAssignmentDropdown(Long id) {
        Assessment ass = assessmentDao.findById(id);
        List<StudentAssessment> studentAssessmentList = studentAssessmentDao.findByAssignment(ass.getId());
        if (CollectionUtils.isEmpty(studentAssessmentList)) {
            return new ArrayList<>();
        }
        return studentAssessmentList.stream().map(studentAss -> {
            DropdownResponse dropdownResponse = new DropdownResponse();
            dropdownResponse.setId(studentAss.getId());
            dropdownResponse.setName(studentAss.getUser().getName());
            return dropdownResponse;
        }).toList();
    }
}
