package com.example.reciperecommender.dto;

import lombok.Data;

@Data
public class AddFavoriteRequest {
   private Long userId;
    private Long recipeId;
}
