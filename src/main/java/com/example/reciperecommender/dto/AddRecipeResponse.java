package com.example.reciperecommender.dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddRecipeResponse {
    private Long id;
    private String name;
    private String instructions;
    private String imageUrl;
}
