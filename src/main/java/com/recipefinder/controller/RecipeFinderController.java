package com.recipefinder.controller;

import com.recipefinder.client.model.RecipeResult;
import com.recipefinder.service.RecipeFinderService;
import com.recipefinder.service.UserRecipe;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("recipefinder")
public class RecipeFinderController {

    @Autowired
    private RecipeFinderService recipeFinderService;

    @GetMapping("/find")
    public List<RecipeResult> findRecipe(@RequestParam String recipeName) {
        return recipeFinderService.findRecipe(recipeName);
    }

    @PostMapping("/account")
    public String createUserAccount(@RequestBody Map<String, String> request) {
        return recipeFinderService.createUserAccount(request.get("username"), request.get("password"));
    }

    @PostMapping("/myrecipes")
    public String saveRecipes(String apiKey, @RequestBody UserRecipe userRecipe) {
        return recipeFinderService.saveRecipes(apiKey, userRecipe.getRecipeName(), userRecipe.getRecipeUrl(),
                userRecipe.getRating());
    }

    @GetMapping("/myrecipes")
    public List<UserRecipe> getUserRecipe(String apiKey) {
        return recipeFinderService.getUserRecipe(apiKey);
    }

    @PostMapping("/recoverapikey")
    public String recoverApiKey(@RequestBody Map<String, String> request) {
        return recipeFinderService.recoverApiKey(request.get("userName"), request.get("password"));
    }

}
