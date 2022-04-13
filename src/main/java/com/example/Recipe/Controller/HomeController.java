package com.example.Recipe.Controller;


import com.example.Recipe.Repositories.IngredientRepository;
import com.example.Recipe.Repositories.RecipeRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {


    RecipeRepository recipeRepository;
    IngredientRepository ingredientRepository;




}
