package com.unitask.repository;

import com.unitask.dto.assessment.AssessmentSubmissionTuple;
import com.unitask.dto.document.DocumentListingTuple;
import com.unitask.entity.assessment.AssessmentSubmission;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface AssessmentSubmissionRepository extends JpaRepository<AssessmentSubmission, Long> {
    @Query("select asub.id as id," +
            "ass.name as assignmentName," +
            "asub.submissionDate as submissionDate," +
            "g.name as groupName," +
            "sub.name as subjectName, " +
            "sub.code as subjectCode " +
            "from AssessmentSubmission asub " +
            "left join asub.assessment ass " +
            "left join ass.subject sub " +
            "left join asub.group g " +
            "where (sub.owner.id = :ownerId) and " +
            "(:assignment is null or ass.name like :assignment) and " +
            "(:group is null or g.name like :group) and " +
            "(:subject is null or sub.code like :subject) and " +
            "(:beforeDate is null or asub.submissionDate >= :beforeDate) and " +
            "(:afterDate is null or asub.submissionDate <= :afterDate) " +
            "order by asub.submissionDate DESC")
    Page<AssessmentSubmissionTuple> getAssessmentSubmissionListing(Long ownerId, String assignment, String group, String subject, LocalDateTime beforeDate, LocalDateTime afterDate, Pageable pageable);

    @Query("select distinct assSubmit.name as name, " +
            "ass.name as assessmentName, " +
            "s.name as subjectName," +
            "assSubmit.path as path " +
            "from AssessmentSubmission assSubmit " +
            "left join Assessment ass on ass.id = assSubmit.assessment.id " +
            "left join Subject s ON ass.subject.id = s.id " +
            "left join StudentAssessment studentAss on studentAss.id = assSubmit.studentAssessment.id " +
            "left join AppUser userId on userId.id = studentAss.user.id " +
            "where userId.id = :id " +
            "and assSubmit.group is NULL " +
            "and (:docName is null or assSubmit.name like :docName) " +
            "and (:assName is null or ass.name like :assName) " +
            "and (:subName is null or s.name like :subName) " +
            "union " +
            "select distinct assSubmit.name as name, " +
            "ass.name as assessmentName, " +
            "s.name as subjectName," +
            "assSubmit.path as path " +
            "from AssessmentSubmission assSubmit " +
            "left join Assessment ass on ass.id = assSubmit.assessment.id " +
            "left join Subject s ON ass.subject.id = s.id " +
            "left join Group g on assSubmit.group.id = g.id " +
            "left join StudentAssessment studentAss on studentAss.assessment.id = g.assessment.id " +
            "left join AppUser userId on userId.id = studentAss.user.id " +
            "where userId.id = :id " +
            "and assSubmit.group IS NOT NULL " +
            "and (:docName is null or assSubmit.name like :docName) " +
            "and (:assName is null or ass.name like :assName) " +
            "and (:subName is null or s.name like :subName) ")
    Page<DocumentListingTuple> documentListingStudent(@Param("docName") String docName, @Param("assName") String assName,
                                                      @Param("subName") String subName, @Param("id")Long id, Pageable pageable);


    @Query("select a from AssessmentSubmission a where a.assessment.id = ?1 ORDER BY a.submissionDate DESC")
    List<AssessmentSubmission> findByAssessment_Id(Long id);

    @Query("select a from AssessmentSubmission a where a.group.id = ?1")
    Optional<AssessmentSubmission> findByGroup_Id(Long id);

    @Query("select a from AssessmentSubmission a where a.studentAssessment.id = ?1")
    Optional<AssessmentSubmission> findByStudentAssessment_Id(Long id);
}
