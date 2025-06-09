package com.unitask.service.impl;


import com.unitask.constant.Enum.GeneralStatus;
import com.unitask.dao.AssessmentDao;
import com.unitask.dto.PageRequest;
import com.unitask.dto.assessment.AssessmentRequest;
import com.unitask.dto.assessment.AssessmentResponse;
import com.unitask.dto.assessment.AssessmentTuple;
import com.unitask.dto.subject.AssessmentDto;
import com.unitask.entity.Subject;
import com.unitask.entity.assessment.Assessment;
import com.unitask.entity.assessment.AssessmentFile;
import com.unitask.mapper.AssessmentMapper;
import com.unitask.service.AssessmentService;
import com.unitask.service.ContextService;
import com.unitask.util.OssUtil;
import com.unitask.util.PageUtil;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AssessmentServiceImpl extends ContextService implements AssessmentService {

    private final AssessmentDao assessmentDao;
    private final OssUtil ossUtil;

    @Override
    public AssessmentResponse read(Long id) {
        Assessment assessment = assessmentDao.findById(id);
        AssessmentResponse response = AssessmentMapper.INSTANCE.toResponse(assessment);
        response.setAttachedFile(ossUtil.toResponse(assessment.getAttachedFile()));
        return response;
    }

    public Page<AssessmentTuple> getListing(PageRequest pageRequest) {
        Pageable pageable = PageUtil.pageable(pageRequest);
        return assessmentDao.findListing(pageable, getCurrentAuthUsername(), pageRequest.getSearch());
    }

    public void update(Subject subject, List<AssessmentDto> dtos) {
        Map<Long, Assessment> map;
        if (CollectionUtils.isEmpty(subject.getAssessment())) {
            map = new HashMap<>();
        } else {
            map = subject.getAssessment()
                    .stream()
                    .filter(x -> Objects.nonNull(x.getId()))
                    .collect(Collectors.toMap(x -> x.getId(), x -> x));
        }

        List<Assessment> saveThis = new ArrayList<>();
        for (AssessmentDto dto : dtos) {
            Assessment assessment;
            if (Objects.nonNull(dto.getId()) && Objects.nonNull(map.get(dto.getId()))) {
                //update
                assessment = map.get(dto.getId());
                map.remove(dto.getId());
            } else {
                //create
                assessment = new Assessment();
            }
            assessment.setName(dto.getName());
            assessment.setAssignmentMode(dto.getAssignmentMode());
            assessment.setWeightage(dto.getWeightage());
            assessment.setSubject(subject);
            assessment.setGeneralStatus(GeneralStatus.ACTIVE);
            saveThis.add(assessment);
        }
        //delete
        assessmentDao.deleteAll(map.values());
        assessmentDao.saveAll(saveThis);
    }

    @Override
    public void update(Long id, AssessmentRequest assessmentRequest) {
        Assessment assessment = assessmentDao.findById(id);
        AssessmentMapper.INSTANCE.update(assessment, assessmentRequest);
        assessmentDao.save(assessment);
    }

    @Override
    @SneakyThrows
    public void uploadFile(Long id, MultipartFile multipartFile) {
        Assessment assessment = assessmentDao.findById(id);
        AssessmentFile assessmentFile = OssUtil.toBaseOssFile(AssessmentFile.class,
                "assessment/" + id, multipartFile);
        assessmentFile.setAssessment(assessment);
        assessmentDao.saveFile(assessmentFile);
        ossUtil.putObject(assessmentFile);
    }

    @Override
    public void deleteFile(Long fileId) {
        AssessmentFile assessmentFile = assessmentDao.findFileByFileId(fileId);
        ossUtil.removeObject(assessmentFile);
        assessmentDao.deleteFile(assessmentFile);
    }

}
