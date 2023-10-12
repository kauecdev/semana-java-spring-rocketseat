package com.kauecdev.todolist.task;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kauecdev.todolist.exception.InvalidTaskRequestException;
import com.kauecdev.todolist.exception.TaskNotFoundException;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/task")
public class TaskController {
    
    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private TaskService taskService;

    @GetMapping
    public ResponseEntity<Object> getAll(HttpServletRequest request) {
        List<Task> taskList = this.taskRepository.findByUserId((UUID)request.getAttribute("userId"));
        return ResponseEntity.status(HttpStatus.OK).body(taskList);
    }
    
    @PostMapping
    public ResponseEntity<Object> create(@RequestBody TaskRequest taskRequest, HttpServletRequest request) {
        try {
            UUID userId = (UUID) request.getAttribute("userId");
            UUID taskCreatedId = this.taskService.save(taskRequest, userId);
            return ResponseEntity.status(HttpStatus.CREATED).body(taskCreatedId);
        } catch (InvalidTaskRequestException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> update(@RequestBody TaskRequest taskRequest, @PathVariable UUID id, HttpServletRequest request) {
        try {
            UUID createdTaskId = this.taskService.update(taskRequest, id);
            
            return ResponseEntity.status(HttpStatus.CREATED).body(createdTaskId);
        } catch (TaskNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (InvalidTaskRequestException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
