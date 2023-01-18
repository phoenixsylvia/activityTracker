package com.phoenix.activitytracker.controller;

import com.phoenix.activitytracker.dto.StudentDTO;
import com.phoenix.activitytracker.services.StudentService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/Students")
@AllArgsConstructor
public class StudentController {

    private final StudentService studentService;

    @GetMapping
    public StudentDTO getStudent(@RequestBody StudentDTO studentDto){

        return studentService.getStudent(studentDto);

    }

    @PostMapping
    public StudentDTO createStudent(@RequestBody StudentDTO studentDto){

     return studentService.createStudent(studentDto);
    }



}
