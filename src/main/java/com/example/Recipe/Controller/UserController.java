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
    public String FollowUserById(@RequestParam Long user_id, Model model) {
        UserApp userToFollow = userAppRepository.findUserAppById(user_id);
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        UserApp currentUser = userAppRepository.findByUsername(name);
        model.addAttribute("username", name);

        // if this is the first time the current user follow this user // and go to the profile page for the user
        if (!currentUser.getFollowing().contains(userToFollow)) {
            currentUser.getFollowing().add(userToFollow);
            userAppRepository.save(currentUser);

            userToFollow.getFollowers().add(currentUser);
            userAppRepository.save(userToFollow);

        } else { // if already follow him/her // keep in the users html page
            // Remove the current user From the users List
            List<UserApp> allUser = userAppRepository.findAll();
            allUser.remove(currentUser);
            model.addAttribute("usersList", allUser);

            for (int i = 0; i < allUser.size(); i++) {
                if (currentUser.getFollowing().contains(allUser.get(i))) {
                    // to show unfollow button
                    allUser.get(i).setFlag("false");
                } else {
                    // to show follow button
                    allUser.get(i).setFlag("true");
                }
            }
            return "users";
        }
        return "myprofile";
    }

    // UnFollow the user from the Application
    @PostMapping("/users/unfollow")
    public String UnFollowUserById(@RequestParam Long user_id, Model model) {
        UserApp userToUnFollow = userAppRepository.findUserAppById(user_id);
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        UserApp currentUser = userAppRepository.findByUsername(name);

        UserApp appUser = userAppRepository.findByUsername(name);
        model.addAttribute("userInfo", appUser);
        model.addAttribute("followingList", appUser.getFollowing());
        model.addAttribute("username", name);

        // if the current user doesn't follow this user
        if (!currentUser.getFollowing().contains(userToUnFollow)) {
            List<UserApp> allUser = userAppRepository.findAll();
            allUser.remove(appUser);
            model.addAttribute("usersList", allUser);

            for (int i = 0; i < allUser.size(); i++) {
                if (appUser.getFollowing().contains(allUser.get(i))) {
                    // to show unfollow button
                    allUser.get(i).setFlag("false");
                } else {
                    // to show follow button
                    allUser.get(i).setFlag("true");
                }
            }
            return "users";
        } else { // if already follow him/her // Unfollow
            currentUser.getFollowing().remove(userToUnFollow);
            userAppRepository.save(currentUser);

            userToUnFollow.getFollowers().remove(currentUser);
            userAppRepository.save(userToUnFollow);
        }
        return "myprofile";
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
        List<RecipeModel> userFavRecipe = userApp.getFavoriteRecipes();
        model.addAttribute("username", currentUser);
        model.addAttribute("favoriteRecipesList", userFavRecipe);

        return "myprofile";
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

    /*
    User can Update
     */
    @PostMapping("/recipe/update")
    public RedirectView UpdateUserOwnRecipe(@RequestParam String name, @RequestParam String description,
                                            Integer userRecipe_id) {
        int update = recipeRepository.updateRecipeModelById(name, description, userRecipe_id);

        return new RedirectView("/user/recipes");

    }

    @GetMapping("/recipe/update")
    public String UpdateUserOwnRecipeByGet(@RequestParam Integer userRecipe_id, @RequestParam String userRecipe_name, @RequestParam String userRecipe_description, Model model) {
        model.addAttribute("recipe_id", userRecipe_id);
        model.addAttribute("recipe_name", userRecipe_name);
        model.addAttribute("recipe_description", userRecipe_description);
        return "updateRecipe";

    }

    ///////////////////////////////////////////////////////
    /*
    Get the /account page for each user
     */
    @GetMapping("/account")
    public String GetAccount(Model model) {
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        UserApp currentUser = userAppRepository.findByUsername(name);
        model.addAttribute("username", name);
        return "account";
    }

    @PostMapping("/account")
    public String GetUserFollowingAccount(Long user_id, Model model) {

        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        model.addAttribute("username", name);
        UserApp currentuser = userAppRepository.findByUsername(name);

        UserApp appUser = userAppRepository.findUserAppById(user_id);
        //check if the user follow the logged in account, and you want to show this logged in account
        if (currentuser.equals(appUser)) {
            // it must not have follow or unfollow button
            model.addAttribute("flag", "Me");
        } else   //Check if the user followed or not

            if (currentuser.getFollowing().contains(appUser)) {
                String flag = "false";
                model.addAttribute("flag", flag);
            } else {
                String flag = "true";
                model.addAttribute("flag", flag);
            }
        model.addAttribute("UserAccount", appUser);
        model.addAttribute("recipesList", appUser.getOwnRecipes());

        return "account";
    }

    /*
   Get the /account page for each user
   In the Other way using the path variable id
    */
    @GetMapping("/user/account/{id}")
    public String GetAccountUser(@PathVariable Long id, Model model) {
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        UserApp currentUser = userAppRepository.findByUsername(name);
        model.addAttribute("username", name);

        UserApp appUser = userAppRepository.findUserAppById(id);
        model.addAttribute("UserAccount", appUser);
        model.addAttribute("recipesList", appUser.getOwnRecipes());

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
        } else   //Check if the user followed or not

            if (currentuser.getFollowing().contains(appUser)) {
                String flag = "false";
                model.addAttribute("flag", flag);
            } else {
                String flag = "true";
                model.addAttribute("flag", flag);
            }

        return new RedirectView("/user/account/" + id);
    }

    ///////////////////////////////////////////////////////
    /*
    Add comments
     */
    @PostMapping("/comment")
    public RedirectView AddCommentForRecipe(@RequestParam String text, Integer id) {
        RecipeModel recipe = recipeRepository.getById(id);

        Comment comment = new Comment(text);

        final String currentUser = SecurityContextHolder.getContext().getAuthentication().getName();
        UserApp userApp = userAppRepository.findByUsername(currentUser);
        userApp.getComments().add(comment);
        comment.setUserComments(userApp);

        recipe.getComments().add(comment);
        comment.setRecipeComments(recipe);

        commentRepository.save(comment);

        return new RedirectView("/account");
    }

    /*
    Delete Comment
     */
    @PostMapping("/comment/delete")
    public RedirectView DeleteCommentForRecipe( Long id) {
        commentRepository.deleteById(id);
        return new RedirectView("/account");

    }

}
