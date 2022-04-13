package com.example.Recipe.Controller;

import com.example.Recipe.Models.Ingredient;
import com.example.Recipe.Models.InstructionModel;
import com.example.Recipe.Models.RecipeModel;
import com.example.Recipe.Models.UserApp;
import com.example.Recipe.Recipe.*;
import com.example.Recipe.Repositories.IngredientRepository;
import com.example.Recipe.Repositories.InstructionRepository;
import com.example.Recipe.Repositories.RecipeRepository;
import com.example.Recipe.Repositories.UserAppRepository;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Date;

@Controller
public class MainController {


    @Autowired
    private PasswordEncoder passwordEncoder;
    private final UserAppRepository userAppRepository;
    private final InstructionRepository instructionRepository;
   private final RecipeRepository recipeRepository;
   private final IngredientRepository ingredientRepository;

    public MainController(UserAppRepository userAppRepository,
                          InstructionRepository instructionRepository,
                          RecipeRepository recipeRepository,
                          IngredientRepository ingredientRepository) {
        this.userAppRepository = userAppRepository;
        this.instructionRepository = instructionRepository;
        this.recipeRepository = recipeRepository;
        this.ingredientRepository = ingredientRepository;
    }


    @GetMapping("/signup")
    public String signupPage(){
        return "signup";
    }

    @GetMapping("/login")
    public String loginPage(){
        return "login";
    }

//    @GetMapping("/")
//    public String getMainPage(){
//        return "index";
//    }

    @PostMapping("/signup")
    public String getSignupPage(
            @RequestParam String username,
            @RequestParam String password,
            @RequestParam String firstname,
            @RequestParam String lastname,
            @RequestParam Date dateOfBirth,
            @RequestParam String nationality,
            @RequestParam String bio
    ){

        UserApp userApp = new UserApp(username,passwordEncoder.encode(password),firstname,lastname,dateOfBirth,nationality,bio);
        userAppRepository.save(userApp);
        return "login";
    }



    @GetMapping("/")
    public String getHomePage(){


        return "index";
    }


}

