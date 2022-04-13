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
import org.springframework.web.bind.annotation.RequestMapping;

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
    public String home(RecipeModel recipe, Model model, String keyword) {
        if(keyword!=null) {
            List<RecipeModel> list = service.getByKeyword(keyword);
            model.addAttribute("RecipesList", list);
        }else {
            List<RecipeModel> list = service.getAllRecipes();
            model.addAttribute("RecipesList", list);}
        return "index";
    }


    @RequestMapping(path = {"/","/searchIngredients"})
    public String homeIngredients(RecipeModel recipe, Model model, String keyword) {
        if(keyword!=null) {
            List<Ingredient> list = service.getByIngredientKeyword(keyword);
            model.addAttribute("IngredientsList", list);
        }else {
            List<Ingredient> list = service.getAllIngredients();
            model.addAttribute("IngredientsList", list);}
        return "searchIngredient";
    }

//    @RequestMapping(path = {"/","/searchIngredients"})
//    public String homeIngredients(RecipeModel recipe, Model model, String keyword) {
//        if(keyword!=null) {
//            List<Ingredient> list = service.getRecipes();
//            model.addAttribute("IngredientsList", list);
//        }else {
//            List<Ingredient> list = service.getAllIngredients();
//            model.addAttribute("IngredientsList", list);}
//        return "searchIngredient";
//    }

//
//    @RequestMapping("/")
//    public String viewHomePage(Model model, @Param("keyword") String keyword) {
//        List<RecipeModel> listRecipes = service.listAll(keyword);
//        model.addAttribute("listRecipes", listRecipes);
//        model.addAttribute("keyword", keyword);
////        model.addAttribute("postList", AppUserRepo.findByUsername(userDetails.getUsername()).getPost());
//        model.addAttribute("RecipesList", RecipeRepository.search(keyword).addAll(listRecipes));
//        return "GetAllRecipes";
//    }
//
//
//
    @GetMapping("/GetAllRecipes")
    public String getAllRecipes(Model model)
    {
        model.addAttribute("RecipesList",RecipeRepository.findAll());
        return "index";
    }


    @GetMapping("/GetAllIngredients")
    public String getAllIngredients(Model model)
    {
        model.addAttribute("IngredientsList",IngredientRepository.findAll());
        return "searchIngredient";
    }
//

}

