package com.example.Recipe.Controller;


import com.example.Recipe.Models.RecipeModel;
import com.example.Recipe.Models.UserApp;
import com.example.Recipe.Repositories.UserAppRepository;
import com.example.Recipe.Security.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Set;

@Controller
public class SearchController {

        @Autowired
        private UserDetailsServiceImpl service;

        @Autowired
        UserAppRepository userAppRepository;


    @RequestMapping(path = {"/","/search"})
    public String homePage(RecipeModel recipe, Model model, @RequestParam String type, @RequestParam String searchterms) { //
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        UserApp current = userAppRepository.findByUsername(name);
        model.addAttribute("username",name);
        model.addAttribute("Role_id", current.getRole().getId()) ;

        System.out.println(type);
        if(type.equals("recipe")) {
            Set<RecipeModel> list = service.getByKeyword(searchterms);
            model.addAttribute("recipesList", list);
        }

        else if(type.equals("ingredient")) {
            Set<RecipeModel> list = service.getByIngredientKeyword(searchterms);
            model.addAttribute("recipesList", list);
        }

        return "home";
    }

}

