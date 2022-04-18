package com.example.Recipe.Controller;

import com.example.Recipe.Models.*;
import com.example.Recipe.Repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import java.sql.Date;

import java.util.List;


@Controller
public class MainController {


    @Autowired
    private PasswordEncoder passwordEncoder;
    private final UserAppRepository userAppRepository;
    private final RoleRepository roleRepository;
    private final InstructionRepository instructionRepository;
   private final RecipeRepository recipeRepository;
   private final IngredientRepository ingredientRepository;

    public MainController(UserAppRepository userAppRepository,
                          RoleRepository roleRepository, InstructionRepository instructionRepository,
                          RecipeRepository recipeRepository,
                          IngredientRepository ingredientRepository) {
        this.userAppRepository = userAppRepository;
        this.roleRepository = roleRepository;
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
        model.addAttribute("recipesList", recipeModelList) ;
        return "testhome";
    }


    @GetMapping(value = "/error")
    public String handleError(HttpServletRequest request) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

        if (status != null) {
            Integer statusCode = Integer.valueOf(status.toString());

            if(statusCode == HttpStatus.NOT_FOUND.value()) {
                return "error-404";
            }
            else if(statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
                return "error-500";
            }
        }
        return "error";
    }
}

