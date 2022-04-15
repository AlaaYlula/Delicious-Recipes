package com.example.Recipe.Controller;


import com.example.Recipe.Models.*;
import com.example.Recipe.Repositories.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;

import java.util.ArrayList;
import java.util.List;

@Controller
public class RecipeController {

    private final IngredientRepository ingredientRepository;
    private final InstructionRepository instructionRepository;
    private final RecipeRepository recipeRepository;
    private final UserAppRepository userAppRepository;
    private final CommentRepository commentRepository;

    public RecipeController(IngredientRepository ingredientRepository,
                            InstructionRepository instructionRepository,
                            RecipeRepository recipeRepository,
                            UserAppRepository userAppRepository,
                            CommentRepository commentRepository) {
        this.ingredientRepository = ingredientRepository;
        this.instructionRepository = instructionRepository;
        this.recipeRepository = recipeRepository;
        this.userAppRepository = userAppRepository;
        this.commentRepository = commentRepository;
    }
    // get data for one recipe
    @GetMapping("/recipe")
    public String getOneRecipe(Model model , @RequestParam int id){
        RecipeModel recipeModel=recipeRepository.getById(id);

       //current user
        String currentUser= SecurityContextHolder.getContext().getAuthentication().getName();
        UserApp userApp=userAppRepository.findByUsername(currentUser);

        // set in the model
        model.addAttribute("instruction",recipeModel.getInstructionModels());
        model.addAttribute("recipe",recipeModel);
        model.addAttribute("ingredients",recipeModel.getIngredientModels());
        model.addAttribute("recipeId",id);
        model.addAttribute("currentUserId",userApp.getId());
        model.addAttribute("allComment",recipeModel.getComments());



        return "recipeinfo";
    }

    // add to favorite list rout
    @PostMapping("/favorite")
    public RedirectView addToFavorite(@RequestParam int id,Model model){
        RecipeModel recipeModel=recipeRepository.getById(id);
        //current user
        String currentUser= SecurityContextHolder.getContext().getAuthentication().getName();
        UserApp userApp=userAppRepository.findByUsername(currentUser);
        // add to favorite list
        List<RecipeModel> favList= userApp.getFavoriteRecipeModels(); // updated by alaa
        favList.add(recipeModel);


        userApp.setFavoriteRecipeModels(favList);
       // userApp.setFavoriteRecipes(favList);

        recipeModel.setUserFavRecipe(userApp);


        return new RedirectView("/recipe?id="+id);
    }


    @PostMapping("/recipe/comment")
    public RedirectView addComment(@RequestParam String text,@RequestParam int id ,Model model){
        RecipeModel recipeModel=recipeRepository.getById(id);
        //current user
        String currentUser= SecurityContextHolder.getContext().getAuthentication().getName();
        UserApp userApp=userAppRepository.findByUsername(currentUser);
        Comment comment=new Comment(text);
        // add comment to the user
        userApp.getComments().add(comment);
        comment.setUserComments(userApp);
        // add comment to the recipe
        recipeModel.getComments().add(comment);
        comment.setRecipeComments(recipeModel);

        commentRepository.save(comment);

        return new RedirectView("/recipe?id="+id);

    }


    @PostMapping("/deletecomment")
    public RedirectView deleteComment(@RequestParam int id, @RequestParam int rid, @RequestParam int cUid){
        RecipeModel recipeModel=recipeRepository.getById(rid);

        String currentUser= SecurityContextHolder.getContext().getAuthentication().getName();
        UserApp userApp=userAppRepository.findByUsername(currentUser);

        if (cUid==userApp.getId()){
            commentRepository.deleteById((long) id);
        }


        return new RedirectView("/recipe?id="+rid);
    }





}
