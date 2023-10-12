package com.kauecdev.todolist.user;

import lombok.Data;

@Data
public class UserRequest {
    private String username;
    private String name;
    private String password;   
}
