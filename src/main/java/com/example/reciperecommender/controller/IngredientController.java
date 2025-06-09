package com.example.reciperecommender.controller;

import com.example.reciperecommender.model.Ingredient;
import com.example.reciperecommender.repository.IngredientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/ingredients")
@RequiredArgsConstructor
public class IngredientController {

    private final IngredientRepository ingredientRepository;

    @GetMapping("/autocomplete")
    public ResponseEntity<List<String>> autocompleteIngredients(@RequestParam("q") String query) {
        List<Ingredient> results = ingredientRepository.findByNameStartingWithIgnoreCase(query);
        List<String> names = results.stream()
                .map(Ingredient::getName)
                .collect(Collectors.toList());
        return ResponseEntity.ok(names);
    }
}