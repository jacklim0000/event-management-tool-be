package com.unitask.repository;

import com.unitask.dto.assessment.AssessmentTuple;
import com.unitask.entity.assessment.Assessment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AssessmentRepository extends JpaRepository<Assessment, Long> {


    @Query("select a from Assessment a where a.subject.id in ?1")
    List<Assessment> findBySubject_Id_List(List<Long> Id);

    @Query("select a.id as id," +
            "a.name as name," +
            "a.dueDate as dueDate," +
            "a.maxMember as maxNumber," +
            "a.subject.name as subjectName," +
            "a.subject.code as subjectCode," +
            "a.subject.color as color " +
            "from Assessment a " +
            "where (a.subject.owner.email = :email) " +
            "AND (a.name LIKE %:search%) " +
            "ORDER BY a.subject.id DESC")
    Page<AssessmentTuple> findBySubject_Owner_EmailAndNameLike(Pageable pageable, String email, String search);


}
