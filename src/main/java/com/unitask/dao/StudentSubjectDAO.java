package com.unitask.dao;

import com.unitask.dto.studentSubject.StudentSubjectTuple;
import com.unitask.entity.StudentSubject;
import com.unitask.repository.StudentSubjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StudentSubjectDAO {

    @Autowired
    private StudentSubjectRepository studentSubjectRepository;

    public List<StudentSubject> findAll() {
        return studentSubjectRepository.findAll();
    }

    public StudentSubject save(StudentSubject studentSubject) {
        if (studentSubject == null) {
            return null;
        }
        return studentSubjectRepository.save(studentSubject);
    }


    public Page<StudentSubjectTuple> findByStudentEmail(Pageable pageable, String email, String search) {
        return studentSubjectRepository.findByStudentEmail(pageable, email, search);
    }

    public Optional<StudentSubject> findByStudentEmailAndSubjectId(String email, Long subjectId) {
        return studentSubjectRepository.findByUser_EmailAndSubject_Id(email, subjectId);
    }
}
