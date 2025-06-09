package com.unitask.repository;

import com.unitask.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;
import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {

    @Query("select t from Task t where t.user.id = :id AND t.checked = :checked ")
    List<Task> findByUser_Id(Long id, Boolean checked);

    @Query("select t from Task t where t.assessment.id in ?1")
    List<Task> findByAssessment_IdIn(Collection<Long> ids);

    @Query("select t from Task t where t.group.id in :ids AND t.checked = :check ORDER BY t.assessment.id")
    List<Task> findByGroup_IdIn(Collection<Long> ids, Boolean check);
}
