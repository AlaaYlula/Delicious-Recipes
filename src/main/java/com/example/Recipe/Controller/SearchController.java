package com.example.Recipe.Controller;


import com.example.Recipe.Models.Ingredient;
import com.example.Recipe.Models.RecipeModel;
import com.example.Recipe.Repositories.IngredientRepository;
import com.example.Recipe.Repositories.RecipeRepository;
import com.example.Recipe.Security.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
public class SearchController {

        @Autowired
        private UserDetailsServiceImpl service;

        @Autowired
        RecipeRepository RecipeRepository;

        @Autowired
        IngredientRepository IngredientRepository;

        @RequestMapping(path = {"/","/search/recipes"})
        public String home(RecipeModel recipe, Model model, String keyword) {
            if(keyword!=null) {
                List<RecipeModel> list = service.getByKeyword(keyword);
                model.addAttribute("RecipesList", list);
            }else {
                List<RecipeModel> list = service.getAllRecipes();
                model.addAttribute("RecipesList", list);}
            return "index";
        }


        @GetMapping("/search/recipe/ingredients")
        public String getAllIngredients(Model model)
        {
            return "searchIngredient";
        }


        @RequestMapping(path = {"/","/search/Ingredients"})
        public String homeIngredients(Model model, String keywords) {
            if(keywords != null) {
                List<RecipeModel> list = service.getByIngredientKeyword(keywords);
                model.addAttribute("RecipesList", list);
            }
            else
            {
                List<Ingredient> list = service.getAllIngredients();
                model.addAttribute("IngredientsList", list);
            }

            return "searchIngredient";
        }



        @GetMapping("/GetAllRecipes")
        public String getAllRecipes(Model model)
        {
            model.addAttribute("RecipesList",RecipeRepository.findAll());
            return "index";
        }


}

