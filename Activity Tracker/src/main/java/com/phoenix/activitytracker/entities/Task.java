package com.phoenix.activitytracker.entities;

import com.phoenix.activitytracker.enums.Status;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity(name = "Task")
@Table(name = "tasks")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description", nullable = false, columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", columnDefinition = "VARCHAR (255) default 'PENDING'")
    private Status status = Status.PENDING;

    @Basic
    private LocalDateTime createdAt;

    @Basic
    private LocalDateTime updatedAt;

    @Basic
    private LocalDateTime completedAt;


    @ManyToOne
    @JoinColumn(
            name = "student_id",
            referencedColumnName = "id",
            nullable = false
    )

    private Student student;

    public Task(String title, String description, Status status, Student student) {
        this.title = title;
        this.description = description;
        this.status = status;
        this.student = student;
    }

    @PrePersist
    public void setCreatedAt(){
        createdAt = LocalDateTime.now();
    }
    @PreUpdate
    public void setUpdatedAt(){
        updatedAt= LocalDateTime.now();
    }
}
