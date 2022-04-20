package com.example.Recipe.Controller;


import com.example.Recipe.Models.*;
import com.example.Recipe.Repositories.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;
import java.util.List;

@Controller
public class RecipeController {

    private final RecipeRepository recipeRepository;
    private final UserAppRepository userAppRepository;
    private final CommentRepository commentRepository;

    public RecipeController(
                            RecipeRepository recipeRepository,
                            UserAppRepository userAppRepository,
                            CommentRepository commentRepository) {
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
        model.addAttribute("username",currentUser);
        model.addAttribute("Role_id", userApp.getRole().getId()) ;


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
        List<RecipeModel> favList= userApp.getFavoriteRecipeModels();
        if(!favList.contains(recipeModel)) {
            favList.add(recipeModel);
            userApp.setFavoriteRecipeModels(favList);
            userAppRepository.save(userApp);

            List<UserApp> usersFav = recipeModel.getUserFavRecipe();
            usersFav.add(userApp);
            recipeModel.setUserFavRecipe(usersFav);

            recipeRepository.save(recipeModel);
        }
        return new RedirectView("/recipe?id="+id);
    }

    // Add Comment To the Recipes
    @PostMapping("/recipe/comment")
    public RedirectView addComment(@RequestParam String text,@RequestParam int id ){
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

    // Delete Comment
    @PostMapping("/deletecomment")
    public RedirectView deleteComment(@RequestParam int id, @RequestParam int rid, @RequestParam int cUid){
        String currentUser= SecurityContextHolder.getContext().getAuthentication().getName();
        UserApp userApp=userAppRepository.findByUsername(currentUser);

        if (cUid==userApp.getId()){
            commentRepository.deleteById((long) id);
        }

        return new RedirectView("/recipe?id="+rid);
    }



}
