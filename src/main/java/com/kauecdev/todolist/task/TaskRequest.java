package com.kauecdev.todolist.task;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Column;
import lombok.Data;

@Data
public class TaskRequest {
    private UUID id;

    private String description;

    @Column(length = 50)
    private String title;

    private LocalDateTime startAt;
    
    private LocalDateTime endAt;

    private String priority;
}
