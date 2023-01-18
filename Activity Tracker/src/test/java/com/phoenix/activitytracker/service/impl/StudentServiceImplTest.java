package com.phoenix.activitytracker.service.impl;

import dev.decagon.activitytracker.dto.StudentDTO;
import dev.decagon.activitytracker.entities.Student;
import dev.decagon.activitytracker.repository.StudentRepository;
import dev.decagon.activitytracker.services.impl.StudentServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class StudentServiceImplTest {
    @Autowired
    private StudentRepository repository;

    private StudentServiceImpl underTest;

    @BeforeEach
    void setup(){
        underTest = new StudentServiceImpl(repository);
    }

    @AfterEach
    void tearDown(){
        repository.deleteAll();
    }

    @Test
    void createStudent() {
        StudentDTO studentDto = new StudentDTO("Jane", "Doe", "janedoe@gmail.com", "1234");

        StudentDTO actual = underTest.createStudent(studentDto);

        assertEquals(studentDto, actual);
    }

    @Test
    void getStudent() {
        Student jane = new Student("Jane", "Doe", "janedoe@gmail.com", "1234");
        StudentDTO studentDto = new StudentDTO("Jane", "Doe", "janedoe@gmail.com", "1234");

        repository.save(jane);

        StudentDTO actual = underTest.getStudent(studentDto);

        assertEquals("Jane", actual.getFirstName());
        assertEquals("Doe", actual.getLastName());
        assertEquals("janedoe@gmail.com", actual.getEmail());
    }
}
