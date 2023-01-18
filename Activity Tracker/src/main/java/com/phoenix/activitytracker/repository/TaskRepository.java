package com.phoenix.activitytracker.repository;

import com.phoenix.activitytracker.entities.Student;
import com.phoenix.activitytracker.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.scheduling.config.Task;

import java.util.List;
import java.util.Optional;

public interface TaskRepository extends JpaRepository<Task, Long> {
    Optional<Task> findTaskByIdAndStudent(Long taskId, Student student);
    List<com.phoenix.activitytracker.entities.Task> findTasksByStudent(Student student);
    List<Task> findTaskByStatusAndStudent(Status status, Student student);
}
