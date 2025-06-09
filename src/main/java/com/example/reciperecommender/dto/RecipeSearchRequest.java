// RecipeSearchRequest.java
package com.example.reciperecommender.dto;

import lombok.Data;
import java.util.List;

@Data
public class RecipeSearchRequest {
    private List<String> ingredientNames;
}
