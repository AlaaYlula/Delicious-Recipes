package com.example.Recipe.Controller;

import com.example.Recipe.Models.Comment;
import com.example.Recipe.Models.RecipeModel;
import com.example.Recipe.Models.UserApp;
import com.example.Recipe.Repositories.CommentRepository;
import com.example.Recipe.Repositories.RecipeRepository;
import com.example.Recipe.Repositories.UserAppRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.util.List;

@Controller
public class UserController {

    private final UserAppRepository userAppRepository;
    private final RecipeRepository recipeRepository;
    private final CommentRepository commentRepository;


    public UserController(UserAppRepository userAppRepository, RecipeRepository recipeRepository, CommentRepository commentRepository) {
        this.userAppRepository = userAppRepository;
        this.recipeRepository = recipeRepository;
        this.commentRepository = commentRepository;
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

        return new RedirectView( "/users");
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
    show the user Information
     */
    @GetMapping("/information")
    public String GetUserInformation(Model model) {
        final String currentUser = SecurityContextHolder.getContext().getAuthentication().getName();
        UserApp userApp = userAppRepository.findByUsername(currentUser);

        model.addAttribute("username", currentUser);
        model.addAttribute("userInfo", userApp);
        return "/userInfo";
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
    public RedirectView AddNewOwnRecipe(@RequestParam String name, @RequestParam String description) {
        final String currentUser = SecurityContextHolder.getContext().getAuthentication().getName();
        UserApp userApp = userAppRepository.findByUsername(currentUser);

        RecipeModel recipe = new RecipeModel();
        recipe.setName(name);
        recipe.setDescription(description);
        recipe.setUserOwnRecipe(userApp);
        List<RecipeModel> ownRecipeList = userApp.getOwnRecipes();
        ownRecipeList.add(recipe);
        recipeRepository.save(recipe);

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
                                            @RequestParam Integer recipe_id) {
       // int update = recipeRepository.updateRecipeModelById(name, description, recipe_id);

        RecipeModel recipefounded = recipeRepository.getById(recipe_id);
        recipefounded.setDescription(description);
        recipefounded.setName(name);
        recipeRepository.save(recipefounded);

        return new RedirectView("/user/recipes");

    }
    @GetMapping("/recipe/update")
    public String UpdateUserOwnRecipeByGet(@RequestParam Integer recipe_id, @RequestParam String name, @RequestParam String description, Model model) {
        model.addAttribute("recipe_id", recipe_id);
        model.addAttribute("recipe_name", name);
        model.addAttribute("recipe_description", description);
        return "updateRecipe";

    }
    @PutMapping("/recipe/update")
    public  RedirectView updatePlayerByID(
            @RequestBody Integer recipe_id,
            @RequestBody String name, @RequestBody String description ){

        RecipeModel findRecipe = recipeRepository.getById(recipe_id);

          findRecipe.setName(name);
          findRecipe.setDescription(description);

          recipeRepository.save(findRecipe);
        return new RedirectView("/user/recipes");

    }

///////////////////////////////////////////////////////////////////////////

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
            // it must not have follow or unfollow button
            List<RecipeModel> userFavRecipe2 = currentuser.getFavoriteRecipeModels();
            model.addAttribute("username", name);
            model.addAttribute("favoriteRecipesList", userFavRecipe2);
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

            if (currentuser.getFollowing().contains(appUser)) {
                String flag = "true";
                model.addAttribute("flag", flag);
            } else {
                String flag = "false";
                model.addAttribute("flag", flag);
            }
        }
        return new RedirectView("/user/account/" + id);
    }

    ///////////////////////////////////////////////////////
    /*
    Add comments
     */
    @PostMapping("/comment")
    public RedirectView AddCommentForRecipe(@RequestParam String text, Integer id,Long user_id) {
        RecipeModel recipe = recipeRepository.getById(id);

        Comment comment = new Comment(text);

        final String currentUser = SecurityContextHolder.getContext().getAuthentication().getName();
        UserApp userApp = userAppRepository.findByUsername(currentUser);

        userApp.getComments().add(comment);
        comment.setUserComments(userApp);

        recipe.getComments().add(comment);
        comment.setRecipeComments(recipe);

        commentRepository.save(comment);

        if(userApp.getId().equals(user_id)){
            return new RedirectView("/user/recipes");

        }
        return new RedirectView("/user/account/"+user_id);
    }

    /*
    Delete Comment
     */
    @PostMapping("/comment/delete")
    public RedirectView DeleteCommentForRecipe( Long id,Long user_id,Long comment_user_id) {
        final String currentUser = SecurityContextHolder.getContext().getAuthentication().getName();
        UserApp userApp = userAppRepository.findByUsername(currentUser);
        if(userApp.getId().equals(comment_user_id)) {
            commentRepository.deleteById(id);
        }
        if(userApp.getId().equals(user_id)){
            return new RedirectView("/user/recipes");

        }
        return new RedirectView("/user/account/"+user_id);

    }



}
