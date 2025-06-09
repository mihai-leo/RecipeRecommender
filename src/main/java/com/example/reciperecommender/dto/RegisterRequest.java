package com.example.reciperecommender.dto;

import com.example.reciperecommender.model.Role;
import lombok.Data;

@Data
public class RegisterRequest {
    private String name;
    private String email;
    private String password;
    private Role role;
}
