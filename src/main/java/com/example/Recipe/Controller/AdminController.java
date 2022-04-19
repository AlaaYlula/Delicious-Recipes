package com.example.Recipe.Controller;


import com.example.Recipe.Models.RecipeModel;
import com.example.Recipe.Models.Role;
import com.example.Recipe.Models.UserApp;
import com.example.Recipe.Repositories.RoleRepository;
import com.example.Recipe.Repositories.UserAppRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;

import java.sql.Date;

@Controller
public class AdminController {


    @Autowired
    UserAppRepository userAppRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    RoleRepository roleRepository;


    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin")
    public String adminPage(Model model)
        {
            model.addAttribute("userList", userAppRepository.findAll());
            return "admin";
        }

    @PostMapping("/admin/users")
    public RedirectView getSignupPage(
            @RequestParam String username,
            @RequestParam String password,
            @RequestParam String firstname,
            @RequestParam String lastname,
            @RequestParam Date dateOfBirth,
            @RequestParam String nationality,
            @RequestParam String bio
    )
    {

        UserApp userApp = new UserApp(username,passwordEncoder.encode(password),firstname,lastname,dateOfBirth,nationality,bio);
        Role role = roleRepository.getById(2L);
        userApp.getRoles().add(role);
        userApp.setRole(role);
        userAppRepository.save(userApp);
        return new RedirectView("/admin");
    }

    @PostMapping("/delete/users")
    public RedirectView deleteUsers(@RequestParam(value = "id", required =false) int id, Model model){

        System.out.println("*************************************************************" +id);
        userAppRepository.deleteById((long) id);
        model.addAttribute("userList",userAppRepository.findAll());
        return new RedirectView("/admin") ;
    }




}
