package com.phoenix.activitytracker.service.impl;

import com.phoenix.activitytracker.dto.TaskDTO;
import com.phoenix.activitytracker.entities.Task;
import com.phoenix.activitytracker.entities.Student;
import com.phoenix.activitytracker.enums.Status;
import com.phoenix.activitytracker.repository.StudentRepository;
import com.phoenix.activitytracker.repository.TaskRepository;
import com.phoenix.activitytracker.services.impl.TaskServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class TaskServiceImplTest {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private StudentRepository studentRepository;

    private TaskServiceImpl underTest;

    @BeforeEach
    void setup(){
        underTest = new TaskServiceImpl(taskRepository, studentRepository);
    }

    @AfterEach
    void tearDown(){
        taskRepository.deleteAll();
        studentRepository.deleteAll();
    }

    @Test
    void createTask() {
        Student jane = new Student("Jane", "Doe", "janedoe@gmail.com", "1234");
        studentRepository.save(jane);

        TaskDTO taskDto = new TaskDTO();
        taskDto.setTitle("Test");
        taskDto.setDescription("Testing Task Creation");

        underTest.createTask(1L, taskDto);

        Task actual = taskRepository.findById(1L).get();

        assertEquals(1L, actual.getId());
        assertEquals(1L, actual.getStudent().getId());
    }

    @Test
    void getTasks() {
        Student jane = new Student( "Jane", "Doe", "janedoe@gmail.com", "1234");
        studentRepository.save(jane);
        Student rose = new Student( "Rose", "Rivers", "roserivers@gmail.com", "1234");
        studentRepository.save(rose);

        rose = studentRepository.findStudentByEmail("roserivers@gmail.com").get();

        Task task = new Task("Title", "Test Description", Status.PENDING, jane);
        Task task1 = new Task("Title", "Test Description", Status.PENDING, rose);
        Task task2 = new Task("Title", "Test Description", Status.PENDING, jane);
        taskRepository.saveAll(List.of(task, task1, task2));

        List<TaskDTO> actual = underTest.getTasks(jane.getId());

        assertEquals(2, actual.size());
    }

    @Test
    void getTask() {
        Student rose = new Student( "Rose", "Rivers", "roserivers@gmail.com", "1234");
        studentRepository.save(rose);

        rose = studentRepository.findStudentByEmail("roserivers@gmail.com").get();

        Task task = new Task("Title1", "Test Description", Status.PENDING, rose);
        task = taskRepository.save(task);

        TaskDTO actual = underTest.getTask(rose.getId(), task.getId() );

        assertEquals(task.getTitle(), actual.getTitle());
        assertEquals(task.getCreatedAt(), actual.getCreatedAt());
    }

    @Test
    void getTasksByStatus() {
        Student jane = new Student( "Jane", "Doe", "janedoe@gmail.com", "1234");
        studentRepository.save(jane);

        jane = studentRepository.findStudentByEmail("janedoe@gmail.com").get();

        Task task = new Task("Title", "Test Description", Status.DONE, jane);
        Task task1 = new Task("Title1", "Test Description1", Status.IN_PROGRESS, jane);
        Task task2 = new Task("Title2", "Test Description2", Status.PENDING, jane);
        Task task3 = new Task("Title3", "Test Description3", Status.DONE, jane);
        taskRepository.saveAll(List.of(task, task1, task2, task3));

        List<TaskDTO> actual = underTest.getTasksByStatus(jane.getId(), "done");

        assertEquals(2, actual.size());
    }

    @Test
    void updateTaskStatus() {
        Student jane = new Student( "Jane", "Doe", "janedoe@gmail.com", "1234");
        studentRepository.save(jane);

        jane = studentRepository.findStudentByEmail("janedoe@gmail.com").get();

        Task task = new Task("Title1", "Test Description", Status.PENDING, jane);
        task = taskRepository.save(task);

        TaskDTO actual = underTest.updateTaskStatus(jane.getId(), task.getId(), "done");

        assertEquals(Status.DONE, actual.getStatus());
    }

    @Test
    void updateTask() {
        Student jane = new Student( "Jane", "Doe", "janedoe@gmail.com", "1234");
        studentRepository.save(jane);

        jane = studentRepository.findStudentByEmail("janedoe@gmail.com").get();

        Task task = new Task("Title1", "Test Description", Status.PENDING, jane);
        task = taskRepository.save(task);

        TaskDTO actual = underTest.updateTask(jane.getId(), task.getId(), "Title 2", "Test Description Updated");

        assertEquals("Title 2", actual.getTitle());
        assertEquals("Test Description Updated", actual.getDescription());
    }

    @Test
    void deleteTask() {
        Student jane = new Student( "Jane", "Doe", "janedoe@gmail.com", "1234");
        studentRepository.save(jane);

        jane = studentRepository.findStudentByEmail("janedoe@gmail.com").get();

        Task task = new Task("Title1", "Test Description", Status.PENDING, jane);
        Long taskId = taskRepository.save(task).getId();

        underTest.deleteTask(jane.getId(), task.getId());

        assertThrows(NoSuchElementException.class, () -> {
            taskRepository.findById(taskId).get();
        });
    }
}
