package com.kauecdev.todolist.exception;

public class TaskNotFoundException extends RuntimeException {
    private final static String DEFAULT_MESSAGE = "A tarefa não existe.";

    public TaskNotFoundException() {
        super(DEFAULT_MESSAGE);
    } 

    public TaskNotFoundException(String message) {
        super(message);
    } 
}
