package com.example.Recipe.Controller;

import com.example.Recipe.Models.*;
import com.example.Recipe.Repositories.*;


import com.example.Recipe.Security.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;
import java.util.ArrayList;
import java.util.List;

@Controller
public class UserController {

    @Autowired
    UserDetailsServiceImpl service;
    @Autowired
    PasswordEncoder passwordEncoder;

    private final UserAppRepository userAppRepository;
    private final RecipeRepository recipeRepository;
    private final CommentRepository commentRepository;
    private final IngredientRepository ingredientRepository;
    private final InstructionRepository instructionRepository;


    public UserController(UserAppRepository userAppRepository, RecipeRepository recipeRepository, CommentRepository commentRepository, IngredientRepository ingredientRepository, InstructionRepository instructionRepository) {
        this.userAppRepository = userAppRepository;
        this.recipeRepository = recipeRepository;
        this.commentRepository = commentRepository;
        this.ingredientRepository = ingredientRepository;
        this.instructionRepository = instructionRepository;
    }

    /*
    List of all Application users
     */
    @GetMapping("/users")
    public String GetAllAppUsers(Model model) {

        final String currentUser = SecurityContextHolder.getContext().getAuthentication().getName();
        UserApp appUser = userAppRepository.findByUsername(currentUser);
        // Remove the current user From the users List
        List<UserApp> allUser = userAppRepository.findAll();
        allUser.remove(appUser);

        for (int i = 0; i < allUser.size(); i++) {
            if (appUser.getFollowing().contains(allUser.get(i))) {
                // to show unfollow button
                allUser.get(i).setFlag("false");
            } else {
                // to show follow button
                allUser.get(i).setFlag("true");
            }
        }
        model.addAttribute("usersList", allUser);
        model.addAttribute("username", currentUser);


        return "users";
    }

    // Follow and show the account for the user from the Application
    @PostMapping("/users/follow")
    public RedirectView FollowUserById(@RequestParam Long user_id, Model model) {
        UserApp userToFollow = userAppRepository.findUserAppById(user_id);

        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        UserApp currentUser = userAppRepository.findByUsername(name);

        currentUser.getFollowing().add(userToFollow);
        userAppRepository.save(currentUser);

        userToFollow.getFollowers().add(currentUser);
        userAppRepository.save(userToFollow);



        return new RedirectView( "/user/account/"+user_id);
    }

    // UnFollow the user from the Application
    @PostMapping("/users/unfollow")
    public RedirectView UnFollowUserById(@RequestParam Long user_id, Model model) {
        UserApp userToUnFollow = userAppRepository.findUserAppById(user_id);

        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        UserApp appUser = userAppRepository.findByUsername(name);

        appUser.getFollowing().remove(userToUnFollow);
        userAppRepository.save(appUser);

        userToUnFollow.getFollowers().remove(appUser);
        userAppRepository.save(userToUnFollow);

        return new RedirectView("/users");
        //return new RedirectView( "/user/account/"+user_id);

    }

    /////////////////////////////////////////////////////////////////////////////////////
    /*
    open the User Account
    Link to the user information
    show the favourite List
     */
    @GetMapping("/myprofile")
    public String GetUserProfile(Model model) {

        final String currentUser = SecurityContextHolder.getContext().getAuthentication().getName();
        UserApp userApp = userAppRepository.findByUsername(currentUser);

        List<RecipeModel> userFavRecipe2 = userApp.getFavoriteRecipeModels();

        model.addAttribute("username", currentUser);
        model.addAttribute("favoriteRecipesList", userFavRecipe2);
        model.addAttribute("userInfo", userApp);

        return "myprofile";
    }
    /*
  User can Delete Fav recipe
   */
    @PostMapping("/recipe/favorite/delete")
    public RedirectView DeleteUserFavRecipe(Integer Recipe_id) {
        final String currentUser = SecurityContextHolder.getContext().getAuthentication().getName();
        UserApp userApp = userAppRepository.findByUsername(currentUser);

        RecipeModel recipeModel = recipeRepository.getById(Recipe_id);

        List<RecipeModel> favRecipes = userApp.getFavoriteRecipeModels();
        favRecipes.remove(recipeModel);
        userApp.setFavoriteRecipeModels(favRecipes);
        userAppRepository.save(userApp);

        List<UserApp> usersFav = recipeModel.getUserFavRecipe();
        usersFav.remove(userApp);
        recipeModel.setUserFavRecipe(usersFav);
        recipeRepository.save(recipeModel);


        return new RedirectView("/myprofile");
    }
    /*
    show the User Follower
     */
    @GetMapping("/followers")
    public String GetUserFollowers(Model model) {
        final String currentUser = SecurityContextHolder.getContext().getAuthentication().getName();
        UserApp userApp = userAppRepository.findByUsername(currentUser);
        List<UserApp> allUser = userAppRepository.findAll();
        allUser.remove(userApp);

        for (int i = 0; i < allUser.size(); i++) {
            if (userApp.getFollowing().contains(allUser.get(i))) {
                // to show unfollow button
                allUser.get(i).setFlag("false");
            } else {
                // to show follow button
                allUser.get(i).setFlag("true");
            }
        }

        model.addAttribute("usersList", userApp.getFollowers());
        return "users";
    }
        /*
    show the User Follower
     */
    @GetMapping("/following")
    public String GetUserFollowing(Model model) {
        final String currentUser = SecurityContextHolder.getContext().getAuthentication().getName();
        UserApp userApp = userAppRepository.findByUsername(currentUser);
        List<UserApp> allUser = userAppRepository.findAll();
        allUser.remove(userApp);

        for (int i = 0; i < allUser.size(); i++) {
            if (userApp.getFollowing().contains(allUser.get(i))) {
                // to show unfollow button
                allUser.get(i).setFlag("false");
            } else {
                // to show follow button
                allUser.get(i).setFlag("true");
            }
        }

        model.addAttribute("usersList", userApp.getFollowing());



        return "users";
    }


    /*
        show the User Own Recipes
     */
    @GetMapping("/user/recipes")
    public String GetUserRecipes(Model model) {
        final String currentUser = SecurityContextHolder.getContext().getAuthentication().getName();
        UserApp userApp = userAppRepository.findByUsername(currentUser);

        model.addAttribute("userRecipes", userApp.getOwnRecipes());
        model.addAttribute("username", currentUser);

        return "userRecipe";
    }

    /*
        User Can Add Own Recipes
     */
    @PostMapping("/user/newRecipe")
    public RedirectView AddNewOwnRecipe(@RequestParam String name, @RequestParam String description,
                                        @RequestParam String ingredientModels , @RequestParam String instructionModels
                                        ){
        final String currentUser = SecurityContextHolder.getContext().getAuthentication().getName();
        UserApp userApp = userAppRepository.findByUsername(currentUser);

        RecipeModel recipe = new RecipeModel();
        recipe.setName(name);
        recipe.setDescription(description);

        recipe.setUserOwnRecipe(userApp);
        List<RecipeModel> ownRecipeList = userApp.getOwnRecipes();
        ownRecipeList.add(recipe);
        recipeRepository.save(recipe);
        // For Ingredients :////////////////////////
        String[] ingredients = ingredientModels.split(",");
        List<Ingredient> ingredientList = new ArrayList<>();
        for (String ingredient:
             ingredients) {
            Ingredient ingredientNew = new Ingredient(ingredient);
            ingredientNew.setRecipes_ingredient(recipe);
            ingredientList.add(ingredientNew);
            ingredientRepository.save(ingredientNew);

        }
        recipe.setIngredientModels(ingredientList);
        ///////////////////////////////////////////
        // For Instructions :////////////////////////
        String[] instructions = instructionModels.split(",");
        List<InstructionModel> instructionList = new ArrayList<>();
        for (int i = 0; i <instructions.length ; i++) {
            InstructionModel instructionNew = new InstructionModel(i+1,instructions[i]);
            instructionNew.setRecipes_instruction(recipe);
            instructionList.add(instructionNew);
            instructionRepository.save(instructionNew);

        }
        recipe.setInstructionModels(instructionList);
        ///////////////////////////////////////////


        return new RedirectView("/user/recipes");
    }

    /*
    User can Delete Own recipe
     */
    @PostMapping("/recipe/delete")
    public RedirectView DeleteUserOwnRecipe(Integer userRecipe_id) {
        recipeRepository.deleteById(userRecipe_id);
        return new RedirectView("/user/recipes");
    }
///////////////////////////////////////////////////////////////////////////

    /*
    User can Update By Query
     */

    @PostMapping("/recipe/update")
    public RedirectView UpdateUserOwnRecipe(@RequestParam String name, @RequestParam String description,

                                            @RequestParam String ingredientModels, @RequestParam String instructionModels,

                                            @RequestParam Integer recipe_id) {
        RecipeModel recipe = recipeRepository.getById(recipe_id);
        // For Ingredients :////////////////////////
        String[] ingredients = ingredientModels.split(",");
        //recipe.getIngredientModels().removeAll(recipe.getIngredientModels());
        recipe.getIngredientModels().clear();
        List<Ingredient> ingredientList = new ArrayList<>();
        for (String ingredient:
                ingredients) {
            Ingredient ingredientNew = new Ingredient(ingredient);
            ingredientNew.setRecipes_ingredient(recipe);
            ingredientList.add(ingredientNew);
        }
        recipe.setIngredientModels(ingredientList);
        ///////////////////////////////////////////
        // For Instructions :////////////////////////
        String[] instructions = instructionModels.split(",");
        recipe.getInstructionModels().removeAll(recipe.getInstructionModels());
        List<InstructionModel> instructionList = new ArrayList<>();
        for (int i = 0; i <instructions.length ; i++) {
            InstructionModel instructionNew = new InstructionModel(i+1,instructions[i]);
            instructionNew.setRecipes_instruction(recipe);
            instructionList.add(instructionNew);
        }
        recipe.setInstructionModels(instructionList);

        recipeRepository.save(recipe);
        int update = recipeRepository.updateRecipeModelById(name, description,recipe_id);
        return new RedirectView("/user/recipes");

    }

    @GetMapping("/recipe/update")
    public String UpdateUserOwnRecipeByGet(@RequestParam Integer recipe_id, @RequestParam String name, @RequestParam String description,
                                           Model model) {
        model.addAttribute("recipe_id", recipe_id);
        model.addAttribute("recipe_name", name);
        model.addAttribute("recipe_description", description);
        return "updateRecipe";

    }
    /*
   Get the /account page for each user
   In the Other way using the path variable id
    */
    @GetMapping("/user/account/{id}")
    public String GetAccountUser(@PathVariable Long id, Model model) {
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        UserApp currentuser = userAppRepository.findByUsername(name);
        model.addAttribute("username", name);

        UserApp appUser = userAppRepository.findUserAppById(id);
        //check if the user follow the logged in account, and you want to show this logged in account
        if (currentuser.equals(appUser)) {
            List<RecipeModel> userFavRecipe2 = currentuser.getFavoriteRecipeModels();
            model.addAttribute("username", name);
            model.addAttribute("favoriteRecipesList", userFavRecipe2);
            model.addAttribute("userInfo", currentuser);
            return "myprofile";
        } else {
            //check if the user follow the logged in account, and you want to show this logged in account
                if(currentuser.getFollowing().contains(appUser)){
                    String flag = "false";
                    model.addAttribute("flag",flag);
                }else  {
                    String flag = "true";
                    model.addAttribute("flag",flag);
                }
        }
        model.addAttribute("UserAccount", appUser);
        model.addAttribute("recipesList", appUser.getOwnRecipes());
        model.addAttribute("username", name);

        return "account";
    }

    @PostMapping("/user/account/{id}")
    public RedirectView GetUserAccount(@PathVariable Long id, Model model) {

        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        model.addAttribute("username", name);
        UserApp currentuser = userAppRepository.findByUsername(name);

        UserApp appUser = userAppRepository.findUserAppById(id);
        //check if the user follow the logged in account, and you want to show this logged in account
        if (currentuser.equals(appUser)) {
            // it must not have follow or unfollow button
            model.addAttribute("flag", "Me");
        } else {  //Check if the user followed or not

            String flag;
            if (currentuser.getFollowing().contains(appUser)) {
                flag = "true";
            } else {
                flag = "false";
            }
            model.addAttribute("flag", flag);
        }
        return new RedirectView("/user/account/" + id);
    }

    ///////////////////////////////////////////////////////
    /*
    Add comments
     */
//    @PostMapping("/comment")
//    public RedirectView AddCommentForRecipe(@RequestParam String text, Integer id,Long user_id) {
//        RecipeModel recipe = recipeRepository.getById(id);
//
//        Comment comment = new Comment(text);
//
//        final String currentUser = SecurityContextHolder.getContext().getAuthentication().getName();
//        UserApp userApp = userAppRepository.findByUsername(currentUser);
//
//        userApp.getComments().add(comment);
//        comment.setUserComments(userApp);
//
//        recipe.getComments().add(comment);
//        comment.setRecipeComments(recipe);
//
//        commentRepository.save(comment);
//
//        if(userApp.getId().equals(user_id)){
//            return new RedirectView("/user/recipes");
//
//        }
//        return new RedirectView("/user/account/"+user_id);
//    }

    /*
    Delete Comment
     */
//    @PostMapping("/comment/delete")
//    public RedirectView DeleteCommentForRecipe( Long id,Long user_id,Long comment_user_id) {
//        final String currentUser = SecurityContextHolder.getContext().getAuthentication().getName();
//        UserApp userApp = userAppRepository.findByUsername(currentUser);
//        if(userApp.getId().equals(comment_user_id)) {
//            commentRepository.deleteById(id);
//        }
//        if(userApp.getId().equals(user_id)){
//            return new RedirectView("/user/recipes");
//
//        }
//        return new RedirectView("/user/account/"+user_id);
//
//    }




}
