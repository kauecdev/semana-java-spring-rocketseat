package com.kauecdev.todolist.exception;

public class InvalidTaskRequestException extends RuntimeException {
    public InvalidTaskRequestException(String message) {
        super(message);
    } 
}
