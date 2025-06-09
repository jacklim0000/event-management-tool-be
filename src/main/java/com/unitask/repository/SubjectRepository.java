package com.unitask.repository;

import com.unitask.dto.document.DocumentListingTuple;
import com.unitask.dto.subject.SubjectTuple;
import com.unitask.entity.Subject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface SubjectRepository extends JpaRepository<Subject, Long> {
    @Query("SELECT " +
            "s.id as id, " +
            "s.name as name, " +
            "s.code as code," +
            "s.color as color," +
            "s.lecturerName as lecturerName " +
            "FROM Subject s " +
            "WHERE  (:search IS NULL OR s.name LIKE %:search%) " +
            "AND (:email IS NULL OR s.owner.email = :email) " +
            "AND (:subjectId IS NULL OR s.id = :subjectId) ")
    Page<SubjectTuple> findListing(Pageable pageable, String email, String search, Long subjectId);

    @Query("select distinct assFile.name as name, " +
            "ass.name as assessmentName, " +
            "s.name as subjectName, " +
            "assFile.path as path " +
            "from Subject s " +
            "left join AppUser user on s.owner.id = user.id " +
            "left join Assessment ass on s.id = ass.subject.id " +
            "left join AssessmentFile assFile on ass.id = assFile.assessment.id " +
            "left join AssessmentSubmission assSubmit on assSubmit.assessment.id = ass.id " +
            "where (:docName is null or assFile.name like :docName) " +
            "and (:assName is null or ass.name like :assName) " +
            "and (:subName is null or s.name like :subName) " +
            "and user.id = :id " +
            "UNION " +
            "select distinct assSubmit.name as name, " +
            "ass.name as assessmentName, " +
            "s.name as subjectName," +
            "assSubmit.path as path " +
            "from Subject s " +
            "left join AppUser user on s.owner.id = user.id " +
            "left join Assessment ass on s.id = ass.subject.id " +
            "left join AssessmentFile assFile on ass.id = assFile.assessment.id " +
            "left join AssessmentSubmission assSubmit on assSubmit.assessment.id = ass.id " +
            "where (:docName is null or assSubmit.name like :docName) " +
            "and (:assName is null or ass.name like :assName) " +
            "and (:subName is null or s.name like :subName) " +
            "and user.id = :id ")
    Page<DocumentListingTuple> documentListingLecturer(@Param("docName") String docName, @Param("assName") String assName,
                                                       @Param("subName") String subName, @Param("id")Long id, Pageable pageable);


}
