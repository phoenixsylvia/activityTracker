package com.phoenix.activitytracker.services.impl;

import com.phoenix.activitytracker.dto.TaskDTO;
import com.phoenix.activitytracker.entities.Student;
import com.phoenix.activitytracker.entities.Task;
import com.phoenix.activitytracker.enums.Status;
import com.phoenix.activitytracker.exception.BadRequestException;
import com.phoenix.activitytracker.exception.NotFoundException;
import com.phoenix.activitytracker.repository.StudentRepository;
import com.phoenix.activitytracker.repository.TaskRepository;
import com.phoenix.activitytracker.services.TaskService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final StudentRepository studentRepository;
    @Override
    public void createTask(Long studentId, TaskDTO taskDto) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new NotFoundException("User not found"));

        Task newTask = new Task();
        newTask.setTitle(taskDto.getTitle());
        newTask.setDescription(taskDto.getDescription());
        newTask.setStudent(student);

        taskRepository.save(newTask);
    }

    @Override
    public List<TaskDTO> getTasks(Long studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new NotFoundException("User not found"));

        List<Task> tasks = taskRepository.findTasksByStudent(student);
        List<TaskDTO> taskDtoList = new ArrayList<>();

        tasks.forEach(task -> {
            System.out.println("------>" + task);
            TaskDTO taskDto = new TaskDTO();
            BeanUtils.copyProperties(task, taskDto);
            taskDtoList.add(taskDto);
        });

        return taskDtoList;
    }

    @Override
    public TaskDTO getTask(Long studentId, Long taskId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new NotFoundException("User not found"));

        Task task = taskRepository.findTaskByIdAndStudent(taskId, student)
                .orElseThrow(() -> new NotFoundException("Task Not Found"));

        TaskDTO taskDto = new TaskDTO();
        BeanUtils.copyProperties(task, taskDto);

        return taskDto;
    }

    @Override
    public List<TaskDTO> getTasksByStatus(Long studentId, String status) {
        Status statusCheck = checkStatus(status);

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new NotFoundException("User not found"));

        List<Task> tasks = taskRepository.findTaskByStatusAndStudent(statusCheck, student);

        List<TaskDTO> taskDtoList = new ArrayList<>();

        tasks.forEach(task -> {
            TaskDTO taskDto = new TaskDTO();
            BeanUtils.copyProperties(task, taskDto);
            taskDtoList.add(taskDto);
        });

        return taskDtoList;
    }

    @Override
    @Transactional
    public TaskDTO updateTaskStatus(Long studentId, Long taskId, String status) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new NotFoundException("User not found"));

        Task task = taskRepository.findTaskByIdAndStudent(taskId, student)
                .orElseThrow(() -> new NotFoundException("Task Not Found"));

        Status statusCheck = checkStatus(status);

        if(!task.getStatus().equals(statusCheck)) {
            task.setStatus(statusCheck);
            if(statusCheck.equals(Status.DONE)){
                task.setCompletedAt(LocalDateTime.now());
                task.setUpdatedAt(LocalDateTime.now());
            } else {
                task.setUpdatedAt(LocalDateTime.now());
                task.setCompletedAt(null);
            }
        }

        TaskDTO taskDto = new TaskDTO();
        BeanUtils.copyProperties(task, taskDto);

        return taskDto;
    }

    @Override
    @Transactional
    public TaskDTO updateTask(Long studentId, Long taskId, String title, String description) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new NotFoundException("User not found"));

        Task task = taskRepository.findTaskByIdAndStudent(taskId, student)
                .orElseThrow(() -> new NotFoundException("Task Not Found"));

        if(title != null && title.length() > 0 && !title.equals(task.getTitle())){
            task.setTitle(title);
            task.setUpdatedAt(LocalDateTime.now());
        }

        if(description != null && description.length() > 0 && !description.equals(task.getDescription())){
            task.setDescription(description);
            task.setUpdatedAt(LocalDateTime.now());
        }

        TaskDTO taskDto = new TaskDTO();
        BeanUtils.copyProperties(task, taskDto);

        return taskDto;
    }

    @Override
    public void deleteTask(Long studentId, Long taskId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new NotFoundException("User not found"));

        taskRepository.findTaskByIdAndStudent(taskId, student)
                .ifPresentOrElse(
                        taskRepository::delete,
                        () -> {
                            throw new NotFoundException("Task Not Found");
                        });
    }

    private Status checkStatus(String status) {
        Status statusCheck;

        if(status.equalsIgnoreCase("pending")){
            statusCheck = Status.PENDING;
        } else if (status.equalsIgnoreCase("in_progress")) {
            statusCheck = Status.IN_PROGRESS;
        } else if (status.equalsIgnoreCase("done")){
            statusCheck = Status.DONE;
        } else {
            throw new BadRequestException("Invalid Status");
        }

        return statusCheck;
    }
}
