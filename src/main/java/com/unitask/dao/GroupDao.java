package com.unitask.dao;

import com.unitask.dto.group.GroupMemberTuple;
import com.unitask.dto.group.GroupTuple;
import com.unitask.entity.Group;
import com.unitask.repository.GroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GroupDao {

    @Autowired
    private GroupRepository groupRepository;

    public Group save(Group group) {
        if (group == null) {
            return null;
        }
        return groupRepository.save(group);
    }

    public Group findById(Long id) {
        if (id == null) {
            return null;
        }
        return groupRepository.findById(id).orElse(null);
    }

    public Page<GroupTuple> getList(String search, Pageable pageable, Long subjectOwner) {
        return groupRepository.findLecturerGroup(search, pageable, subjectOwner);
    }

    public Page<GroupTuple> findMyGroup(String search, Pageable pageable, Long userId) {
        return groupRepository.findMyGroup(search, pageable, userId);
    }

    public List<Group> findByUserId(Long id) {
        return groupRepository.findByStudentAssessment_User_Id(id);
    }

    public List<Group> findByAssessmentIdAndUserId(Long assessmentId, Long userId) {
        return groupRepository.findByAssessmentIdAndUserId(assessmentId, userId);
    }

    public List<GroupMemberTuple> findMember(Long groupId) {
        return groupRepository.findMember(groupId);
    }

    public Page<GroupTuple> findByAssessmentId(Pageable pageable, List<Long> ids, Boolean openForPublic ,Boolean locked) {
        return groupRepository.findByAssessment_IdIn(pageable, ids, openForPublic, locked);
    }

}
