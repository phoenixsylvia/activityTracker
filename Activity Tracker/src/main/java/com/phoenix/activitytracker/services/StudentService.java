package com.phoenix.activitytracker.services;

import com.phoenix.activitytracker.dto.StudentDTO;


public interface StudentService {

StudentDTO createStudent(StudentDTO studentDto);

StudentDTO getStudent(StudentDTO studentDto);

}
