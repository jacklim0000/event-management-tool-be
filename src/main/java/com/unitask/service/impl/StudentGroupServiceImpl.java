package com.unitask.service.impl;

import com.unitask.dao.AppUserDAO;
import com.unitask.dao.GroupDao;
import com.unitask.dao.StudentAssessmentDao;
import com.unitask.dto.PageRequest;
import com.unitask.dto.assessment.AssessmentTuple;
import com.unitask.dto.group.GroupMemberTuple;
import com.unitask.dto.group.GroupResponse;
import com.unitask.dto.group.GroupTuple;
import com.unitask.entity.Group;
import com.unitask.entity.StudentAssessment;
import com.unitask.entity.User.AppUser;
import com.unitask.exception.ServiceAppException;
import com.unitask.mapper.GroupMapper;
import com.unitask.service.ContextService;
import com.unitask.service.StudentGroupService;
import com.unitask.util.OssUtil;
import com.unitask.util.PageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class StudentGroupServiceImpl extends ContextService implements StudentGroupService {

    @Autowired
    private GroupDao groupDao;
    @Autowired
    private AppUserDAO appUserDAO;
    @Autowired
    private StudentAssessmentDao studentAssessmentDao;
    @Autowired
    private OssUtil ossUtil;

    @Override
    public void joinGroup(Long id) {
        AppUser appUser = appUserDAO.findByEmail(getCurrentAuthUsername());
        Group group = groupDao.findById(id);
        if (group == null) {
            throw new ServiceAppException(HttpStatus.BAD_REQUEST, "Group does not Exists");
        }
        Optional<StudentAssessment> studentAssessment = studentAssessmentDao.findByAppUserAndAssessment(appUser.getId(), group.getAssessment().getId());
        if (studentAssessment.isPresent()) {
            studentAssessment.get().setGroup(group);
            studentAssessmentDao.save(studentAssessment.get());
        }
    }

//    @Override
//    public void submitGroup(Long id, MultipartFile multipartFile) {
//        Group group = groupDao.findById(id);
//        if (group == null) {
//            throw new ServiceAppException(HttpStatus.BAD_REQUEST, "Group does not Exists");
//        }
//        if (StringUtils.isNotBlank(group.getFilePath())) {
//            ossUtil.removeObject(group.getFilePath());
//        }
//        String extension = FilenameUtils.getExtension(multipartFile.getOriginalFilename());
//        String uuid = UUID.randomUUID().toString();
//        String path = "group" + "/" + uuid + "." + extension;
//        ossUtil.putObject(path, multipartFile);
//        group.setFileName(multipartFile.getOriginalFilename());
//        group.setFilePath(path);
//        group.setUuid(uuid);
//        group.setFileCreatedDate(LocalDateTime.now());
//        groupDao.save(group);
//    }

    @Override
    public void leaveGroup(Long id) {
        AppUser appUser = appUserDAO.findByEmail(getCurrentAuthUsername());
        Group group = groupDao.findById(id);
        if (group == null) {
            throw new ServiceAppException(HttpStatus.BAD_REQUEST, "Group does not Exists");
        }
        StudentAssessment studentAssessment = group.getStudentAssessment().stream().filter(ass -> ass.getUser().getId().equals(appUser.getId())).findFirst().orElse(null);
        if (studentAssessment == null) {
            throw new ServiceAppException(HttpStatus.BAD_REQUEST, "Student Assessment does not Exists");
        }
        studentAssessment.setGroup(null);
        studentAssessmentDao.save(studentAssessment);
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
    public List<GroupMemberTuple> getGroupList(Long assessmentId) {
        AppUser appUser = appUserDAO.findByEmail(getCurrentAuthUsername());
        List<Group> group = groupDao.findByAssessmentIdAndUserId(assessmentId, appUser.getId());
        if (!group.isEmpty()) {
            return groupDao.findMember(group.get(0).getId());
        }
        return new ArrayList<>();
    }

    @Override
    public Page<GroupTuple> getList(PageRequest pageRequest) {
        Pageable pageable = PageUtil.pageable(pageRequest);
        AppUser appUser = appUserDAO.findByEmail(getCurrentAuthUsername());
        Page<GroupTuple> groupAssessmentTuple = groupDao.findMyGroup(pageRequest.getSearch(), pageable, appUser.getId());
        return groupAssessmentTuple;
    }

    @Override
    public Page<GroupTuple> getGroupList(PageRequest pageRequest) {
        AppUser appUser = appUserDAO.findByEmail(getCurrentAuthUsername());
        Pageable pageable = PageUtil.pageable(pageRequest);
        List<StudentAssessment> studentAssessmentList = studentAssessmentDao.findByAppUserAndGroupNull(appUser.getId());
        List<Long> assessmentId = studentAssessmentList.stream().map(x -> x.getAssessment().getId()).distinct().toList();
        return groupDao.findByAssessmentId(pageable, assessmentId, true, false);
    }

    @Override
    public List<AssessmentTuple> getAssessment() {
        AppUser appUser = appUserDAO.findByEmail(getCurrentAuthUsername());
        return studentAssessmentDao.findByGroupAssessment(appUser.getId());
    }
}
