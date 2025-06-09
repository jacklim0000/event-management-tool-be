package com.unitask.repository;

import com.unitask.constant.Enum.AssignmentMode;
import com.unitask.dto.assessment.AssessmentTuple;
import com.unitask.entity.StudentAssessment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface StudentAssessmentRepository extends JpaRepository<StudentAssessment, Long> {

    @Query("select a.id as id," +
            "a.name as name," +
            "a.dueDate as dueDate," +
            "a.subject.name as subjectName," +
            "a.subject.color as color " +
            "from StudentAssessment s " +
            "left join s.assessment a   " +
            "left join a.subject sub  " +
            "where (:search is null or a.name like %:search%) " +
            "AND s.user.id = :userId " +
            "ORDER by s.status DESC")
    Page<AssessmentTuple> findByAssessment_NameOrderByStatusDesc(String search, Pageable pageable, Long userId);

    @Query("select s from StudentAssessment s where s.user.id = :userId and s.assessment.id = :assessmentId")
    Optional<StudentAssessment> findByUser_IdAndAssessment_Id(Long userId, Long assessmentId);

    @Query("select s from StudentAssessment s where s.user.id in ?1 and s.assessment.id = ?2")
    List<StudentAssessment> findByUser_IdInAndAssessment_Id(Collection<Long> ids, Long id);

    @Query("select s from StudentAssessment s where s.assessment.id = ?1")
    List<StudentAssessment> findByAssessment_Id(Long id);

    @Query("SELECT user FROM StudentAssessment user " +
            "WHERE user.id IN :ids ")
    List<StudentAssessment> findAllByIds(@Param("ids") Collection<Long> ids);

    @Query("select s from StudentAssessment s where s.user.id = ?1 and s.group is null")
    List<StudentAssessment> findByUser_IdAndGroupNull(Long id);

    @Query("select s from StudentAssessment s where s.assessment.subject.id = ?1")
    List<StudentAssessment> findByAssessment_Subject_Id(Long id);

    @Query("select a.id as id," +
            "a.name as name," +
            "a.dueDate as dueDate," +
            "a.maxMember as maxNumber," +
            "a.subject.name as subjectCode," +
            "a.subject.name as subjectName," +
            "a.subject.color as color " +
            "from StudentAssessment s " +
            "left join s.assessment a   " +
            "left join a.subject sub  " +
            "where a.assignmentMode = :assignmentMode " +
            "AND s.user.id = :userId " +
            "ORDER by s.status DESC")
    List<AssessmentTuple> findByGroupAssessment(AssignmentMode assignmentMode, Long userId);
}
