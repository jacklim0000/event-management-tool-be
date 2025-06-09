package com.unitask.mapper;

import com.unitask.constant.Enum.AssignmentMode;
import com.unitask.dto.assessment.AssessmentRequest;
import com.unitask.dto.assessment.AssessmentResponse;
import com.unitask.dto.assessment.AssessmentSubmissionMemberResponse;
import com.unitask.dto.assessment.AssessmentSubmissionResponse;
import com.unitask.entity.StudentAssessment;
import com.unitask.entity.assessment.Assessment;
import com.unitask.entity.assessment.AssessmentMarkingRubric;
import com.unitask.entity.assessment.AssessmentSubmission;
import com.unitask.util.OssUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.ArrayList;
import java.util.List;

@Mapper(uses = OssUtil.class)
public interface AssessmentMapper {
    AssessmentMapper INSTANCE = Mappers.getMapper(AssessmentMapper.class);

    @Named("toAssessmentResponse")
    @Mapping(target = "attachedFile", ignore = true)
    @Mapping(target = "subject.assessment", ignore = true)
    AssessmentResponse toResponse(Assessment assessment);

    @BeanMapping(qualifiedByName = "toRubrics")
    @Mapping(target = "studentAssessments", ignore = true)
    @Mapping(target = "generalStatus", ignore = true)
    @Mapping(target = "attachedFile", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "subject", ignore = true)
    void update(@MappingTarget Assessment assessment, AssessmentRequest assessmentRequest);

    @Named("toRubrics")
    @AfterMapping()
    default void update(@MappingTarget Assessment assessment) {
        if (CollectionUtils.isNotEmpty(assessment.getAssessmentMarkingRubrics())) {
            for (AssessmentMarkingRubric assessmentMarkingRubric : assessment.getAssessmentMarkingRubrics()) {
                assessmentMarkingRubric.setAssessment(assessment);
            }
        }
    }

    @Mapping(target = "name", ignore = true)
    @Mapping(target = "submittedDocuments", ignore = true)
    @Mapping(target = "groupMember", ignore = true)
    @Mapping(target = "assessment", source = "assessmentSubmission.assessment", qualifiedByName = "toAssessmentResponse")
    @BeanMapping(qualifiedByName = "afterResponse")
    AssessmentSubmissionResponse toResponse(AssessmentSubmission assessmentSubmission);

    @Named("afterResponse")
    @AfterMapping
    default void update(@MappingTarget AssessmentSubmissionResponse response, AssessmentSubmission assessmentSubmission) {
        List<AssessmentSubmissionMemberResponse> list = new ArrayList<>();
        if (AssignmentMode.INDIVIDUAL.equals(assessmentSubmission.getAssessment().getAssignmentMode())) {
            response.setName(assessmentSubmission.getStudentAssessment().getUser().getName());
            AssessmentSubmissionMemberResponse member = new AssessmentSubmissionMemberResponse();
            member.setId(assessmentSubmission.getStudentAssessment().getId());
            member.setName(assessmentSubmission.getStudentAssessment().getUser().getName());
            member.setScore(assessmentSubmission.getStudentAssessment().getScore());
            list.add(member);

        } else if (AssignmentMode.GROUP.equals(assessmentSubmission.getAssessment().getAssignmentMode())) {
            response.setName(assessmentSubmission.getGroup().getName());
            for (StudentAssessment studentAssessment : assessmentSubmission.getGroup().getStudentAssessment()) {
                AssessmentSubmissionMemberResponse member = new AssessmentSubmissionMemberResponse();
                member.setId(studentAssessment.getId());
                member.setName(studentAssessment.getUser().getName());
                member.setScore(studentAssessment.getScore());
                list.add(member);
            }
        }

        response.setGroupMember(list);
    }
}
