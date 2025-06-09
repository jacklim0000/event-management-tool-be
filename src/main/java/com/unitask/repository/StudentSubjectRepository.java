package com.unitask.repository;

import com.unitask.dto.studentSubject.StudentSubjectTuple;
import com.unitask.entity.StudentSubject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StudentSubjectRepository extends JpaRepository<StudentSubject, Long> {
    @Query("SELECT " +
            "s.id as id, " +
            "s.name as name, " +
            "s.code as code," +
            "s.lecturerName as lecturerName," +
            "s.color as color," +
            "ss.status as status " +
            "FROM Subject s " +
            "LEFT JOIN StudentSubject ss ON (ss.subject.id = s.id AND ss.user.email =:email)" +
            "WHERE (:search IS NULL OR s.name LIKE %:search%)" +
            "ORDER BY ss.status DESC "
    )
    Page<StudentSubjectTuple> findByStudentEmail(Pageable pageable, String email, String search);

    @Query("select s from StudentSubject s where s.user.email = ?1 and s.subject.id = ?2")
    Optional<StudentSubject> findByUser_EmailAndSubject_Id(String email, Long Id);
}
