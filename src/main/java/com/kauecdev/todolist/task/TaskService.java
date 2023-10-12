package com.kauecdev.todolist.task;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kauecdev.todolist.exception.InvalidTaskRequestException;
import com.kauecdev.todolist.exception.TaskNotFoundException;
import com.kauecdev.todolist.utils.Utils;

@Service
public class TaskService {
    
    @Autowired
    private TaskRepository taskRepository;

    public UUID save(TaskRequest taskRequest, UUID userId) {
        try {
            validateTaskRequest(taskRequest);

            Task task = Task.builder()
                .title(taskRequest.getTitle())
                .description(taskRequest.getDescription())
                .priority(taskRequest.getPriority())
                .startAt(taskRequest.getStartAt())
                .endAt(taskRequest.getEndAt())
                .userId((UUID) userId)
                .build();

        Task createdTask = this.taskRepository.save(task);

        return createdTask.getId();
        } catch (InvalidTaskRequestException e) {
            throw e;
        } catch (Exception e) {
            throw e;
        }
    }

    public UUID update(TaskRequest taskRequest, UUID taskId) {
        try {
            Optional<Task> existingTaskFound = this.taskRepository.findById(taskId);

            if (!existingTaskFound.isPresent()) {
                throw new TaskNotFoundException();
            }

            Task existingTask = existingTaskFound.get();

            validateTaskRequest(taskRequest);

            Utils.copyNonNullProperties(taskRequest, existingTask);
            
            Task createdTask = this.taskRepository.save(existingTask);

            return createdTask.getId();

        } catch (TaskNotFoundException e) {
            throw e;
        } catch (InvalidTaskRequestException e) {
            throw e;
        } catch (Exception e) {
            throw e;
        }
    }

    private void validateTaskRequest(TaskRequest taskRequest) throws InvalidTaskRequestException {
        LocalDateTime currentDateTime = LocalDateTime.now();

        if (currentDateTime.isAfter(taskRequest.getStartAt()) || currentDateTime.isAfter(taskRequest.getEndAt())) {
            throw new InvalidTaskRequestException("A data de início/término deve ser maior que a data atual.");
        }

        if (taskRequest.getStartAt().isAfter(taskRequest.getEndAt())) {
            throw new InvalidTaskRequestException("A data de início deve ser menor que a data de término.");
        }
    }
}
