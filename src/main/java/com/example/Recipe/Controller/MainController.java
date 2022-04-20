package com.example.Recipe.Controller;

import com.example.Recipe.Models.*;
import com.example.Recipe.Repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.sql.Date;

import java.util.List;


@Controller
public class MainController {


    @Autowired
    private PasswordEncoder passwordEncoder;

    private final UserAppRepository userAppRepository;
    private final RoleRepository roleRepository;
   private final RecipeRepository recipeRepository;

    public MainController(UserAppRepository userAppRepository,
                          RoleRepository roleRepository,
                          RecipeRepository recipeRepository
                        ) {
        this.userAppRepository = userAppRepository;
        this.roleRepository = roleRepository;
        this.recipeRepository = recipeRepository;
    }


    @GetMapping("/signup")
    public String signupPage(){
        return "signup";
    }

    @GetMapping("/login")
    public String loginPage(){
        return "login";
    }


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

        UserApp userApp1=userAppRepository.findByUsername(username);
        if(userApp1 != null){
            return "signup";
        }

        UserApp userApp = new UserApp(username,passwordEncoder.encode(password),firstname,lastname,dateOfBirth,nationality,bio);
        Role role = roleRepository.getById(2L);
        userApp.setRole(role);
        userAppRepository.save(userApp);

        return "login";
    }



    @GetMapping("/")
    public String getHomePage(Model model){
        List<RecipeModel> recipeModelList = recipeRepository.findAll();
        //Remove th own recipes from the Home page
        recipeModelList.removeIf(recipe -> recipe.getUserOwnRecipe() != null);

        final String currentUser = SecurityContextHolder.getContext().getAuthentication().getName();
        UserApp current = userAppRepository.findByUsername(currentUser);

        model.addAttribute("recipesList", recipeModelList) ;
        model.addAttribute("username", currentUser) ;
        model.addAttribute("Role_id", current.getRole().getId()) ;

        return "testhome";
    }


    @GetMapping(value = "/error")
    public String handleError(HttpServletRequest request) {

        return "error";
    }
}

