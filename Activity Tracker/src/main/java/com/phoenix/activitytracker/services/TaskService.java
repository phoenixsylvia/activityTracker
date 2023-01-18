package com.phoenix.activitytracker.services;

import com.phoenix.activitytracker.dto.TaskDTO;


import java.util.List;

public interface TaskService {

    void createTask(Long studentId, TaskDTO taskDto);

    List<TaskDTO> getTasks(Long studentId);

    TaskDTO getTask(Long studentId, Long taskId);

    List<TaskDTO> getTasksByStatus(Long studentId, String status);

    TaskDTO updateTaskStatus(Long studentId, Long taskId, String status);

    TaskDTO updateTask(Long studentId, Long taskId, String title, String description);

    void deleteTask(Long studentId, Long taskId);
}
