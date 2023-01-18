package com.phoenix.activitytracker.dto;

import com.phoenix.activitytracker.enums.Status;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class TaskDTO {
    @NotBlank(message = "Task Title Must Not Be Blank")
    private String title;

    @NotBlank(message = "Task Description Must Not Be Blank")
    private String description;

    private Status status;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private LocalDateTime completedAt;
}
