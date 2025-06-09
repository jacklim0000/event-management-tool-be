package com.unitask.service.impl;

import com.unitask.dao.*;
import com.unitask.dto.task.TaskRequest;
import com.unitask.dto.task.TaskResponse;
import com.unitask.entity.Group;
import com.unitask.entity.StudentAssessment;
import com.unitask.entity.Task;
import com.unitask.entity.User.AppUser;
import com.unitask.entity.assessment.Assessment;
import com.unitask.exception.ServiceAppException;
import com.unitask.mapper.TaskMapper;
import com.unitask.repository.TaskRepository;
import com.unitask.service.ContextService;
import com.unitask.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TaskServiceImpl extends ContextService implements TaskService {

    @Autowired
    private TaskDao taskDao;
    @Autowired
    private AssessmentDao assessmentDao;
    @Autowired
    private AppUserDAO appUserDAO;
    @Autowired
    private GroupDao groupDao;
    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private StudentAssessmentDao studentAssessmentDao;

    @Override
    public TaskResponse createTask(TaskRequest taskRequest) {
        AppUser appUser = appUserDAO.findByEmail(getCurrentAuthUsername());
        Assessment assessment = null;
        Optional<StudentAssessment> studentAssessment = Optional.empty();
        if (taskRequest.getAssignmentId() != null) {
            assessment = assessmentDao.findById(taskRequest.getAssignmentId());
            if (assessment == null) {
                throw new ServiceAppException(HttpStatus.BAD_REQUEST, "Assessment does not Exists");
            }
            studentAssessment = studentAssessmentDao.findByAppUserAndAssessment(appUser.getId(), assessment.getId());
        }

        Task task = new Task();
        task.setAssessment(assessment);
        task.setName(taskRequest.getName());
        task.setChecked(Boolean.FALSE);
        if (studentAssessment.isPresent()) {
            task.setGroup(studentAssessment.get().getGroup());
        }
        if (taskRequest.getAssignedId() != null) {
            AppUser assignedUser = appUserDAO.findById(taskRequest.getAssignedId());
            task.setUser(assignedUser);
        } else {
            task.setUser(appUser);
        }
        taskDao.save(task);
        return TaskMapper.INSTANCE.entityToResponse(task);
    }

    @Override
    public void updateTask(Long id, TaskRequest taskRequest) {
        Task task = taskDao.findById(id);
        if (task == null) {
            throw new ServiceAppException(HttpStatus.BAD_REQUEST, "Task does not Exists");
        }
        Assessment assessment = null;
        if (taskRequest.getAssignmentId() != null) {
            assessment = assessmentDao.findById(taskRequest.getAssignmentId());
            if (assessment == null) {
                throw new ServiceAppException(HttpStatus.BAD_REQUEST, "Assessment does not Exists");
            }
        }
        task.setName(taskRequest.getName());
        task.setAssessment(assessment);
        task.setChecked(taskRequest.getChecked());
        taskDao.save(task);
    }

    @Override
    public void checkTask(Long id) {
        Task task = taskDao.findById(id);
        if (task == null) {
            throw new ServiceAppException(HttpStatus.BAD_REQUEST, "Task does not Exists");
        }
        task.setChecked(Boolean.TRUE);
        taskRepository.save(task);
    }

    @Override
    public void uncheckTask(Long id) {
        Task task = taskDao.findById(id);
        if (task == null) {
            throw new ServiceAppException(HttpStatus.BAD_REQUEST, "Task does not Exists");
        }
        task.setChecked(Boolean.FALSE);
        taskRepository.save(task);
    }

    @Override
    public TaskResponse getTask(Long id) {
        Task task = taskDao.findById(id);
        if (task == null) {
            throw new ServiceAppException(HttpStatus.BAD_REQUEST, "Task does not Exists");
        }
        return TaskMapper.INSTANCE.entityToResponse(task);
    }

    @Override
    public List<TaskResponse> getTaskList(Boolean checked) {
        AppUser appUser = appUserDAO.findByEmail(getCurrentAuthUsername());
        List<Task> taskList = taskDao.findByUserId(appUser.getId(), checked);
        return TaskMapper.INSTANCE.entityListToResponseList(taskList);
    }

    @Override
    public List<TaskResponse> getGroupTask(Boolean checked) {
        AppUser appUser = appUserDAO.findByEmail(getCurrentAuthUsername());
        List<Group> groupList = groupDao.findByUserId(appUser.getId());
        List<Long> groupId = groupList.stream().map(Group::getId).distinct().toList();
        List<Task> taskList = taskDao.findByGroup(groupId, checked);
        return TaskMapper.INSTANCE.entityListToResponseList(taskList);
    }
}
