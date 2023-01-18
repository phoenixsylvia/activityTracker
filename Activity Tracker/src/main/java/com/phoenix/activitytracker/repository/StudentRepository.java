package com.phoenix.activitytracker.repository;

import com.phoenix.activitytracker.entities.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository <Student, Long> {

    Optional<Student> findStudentByEmail (String studentEmail);

    boolean existsByEmail(String studentEmail);
}
