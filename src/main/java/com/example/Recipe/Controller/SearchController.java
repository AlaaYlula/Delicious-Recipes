package com.example.Recipe.Controller;


import com.example.Recipe.Models.Ingredient;
import com.example.Recipe.Models.RecipeModel;
import com.example.Recipe.Repositories.IngredientRepository;
import com.example.Recipe.Repositories.RecipeRepository;
import com.example.Recipe.Security.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class SearchController {

        @Autowired
        private UserDetailsServiceImpl service;

        @Autowired
        RecipeRepository RecipeRepository;

        @Autowired
        IngredientRepository IngredientRepository;
        

    @RequestMapping(path = {"/","/search"})
    public String homePage(RecipeModel recipe, Model model, @RequestParam String type, @RequestParam String searchterms) { //

        System.out.println(type);
        if(type.equals("recipe")) {
            List<RecipeModel> list = service.getByKeyword(searchterms);
            model.addAttribute("recipesList", list);
        }

        else if(type.equals("ingredient")) {
            List<RecipeModel> list = service.getByIngredientKeyword(searchterms);
            model.addAttribute("recipesList", list);
        }

        else {
            List<RecipeModel> list = service.getAllRecipes();
            model.addAttribute("recipesList", list);}

        return "testhome";
    }


}

