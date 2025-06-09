package com.unitask.dao;

import com.unitask.entity.Task;
import com.unitask.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
public class TaskDao {

    @Autowired
    private TaskRepository taskRepository;

    public Task save(Task task) {
        return taskRepository.save(task);
    }

    public Task findById(Long id) {
        return taskRepository.findById(id).orElse(null);
    }

    public List<Task> findByUserId(Long id, Boolean check) {
        return taskRepository.findByUser_Id(id, check);
    }

    public List<Task> findByGroup(Collection<Long> ids, Boolean check) {
        return taskRepository.findByGroup_IdIn(ids, check);
    }

}
