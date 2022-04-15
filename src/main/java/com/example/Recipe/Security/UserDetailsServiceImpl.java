package com.example.Recipe.Security;

import com.example.Recipe.Models.Ingredient;
import com.example.Recipe.Models.RecipeModel;
import com.example.Recipe.Repositories.IngredientRepository;
import com.example.Recipe.Repositories.RecipeRepository;
import com.example.Recipe.Repositories.UserAppRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    UserAppRepository userAppRepository;

    @Autowired
    RecipeRepository RecipeRepository;

    @Autowired
    IngredientRepository IngredientRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userAppRepository.findByUsername(username);
    }


//    public List<RecipeModel> listAll(String keyword) {
//        if (keyword != null) {
//            return RecipeRepository.search(keyword);
//        }
//        return RecipeRepository.findAll();
//    }

    public List<RecipeModel> getAllRecipes(){
        List<RecipeModel> list =  RecipeRepository.findAll();
        return list;
    }

    /*
     * TODO: Get Recipe By keyword
     */

    public List<RecipeModel> getByKeyword(String keyword){
        return RecipeRepository.search(keyword);
    }



    public List<Ingredient> getAllIngredients(){
        List<Ingredient> listIngredient =  IngredientRepository.findAll();
        return listIngredient;
    }


    /*
     * TODO: Get Ingredient By keywords
     */

    public List<RecipeModel> getByIngredientKeyword(String keywords){
        return RecipeRepository.searchIngredient(keywords);
    }

}
