package com.example.Recipe.Models;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.apache.catalina.User;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

//@JsonIgnoreProperties({"userFavRecipe"})//    Updated by alaa
@Entity
public class RecipeModel {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "recipe_id")
        private int recipe_id;

        private int id;
        @Column(nullable = false)
        private String name;
        @Column(columnDefinition = "TEXT")
        private String description;
        @Column(length = 5000)
        private String thumbnail_url = "https://www.cvent.com/sites/default/files/styles/focus_scale_and_crop_800x450/public/migrated_attachments/meal-918638_1280-1.jpg?itok=dMJGxEC2"; // image
        @Column(length = 5000)

        @OneToMany(mappedBy = "recipeModelComments", cascade = CascadeType.MERGE )
        List<Comment> comments;

        @ManyToOne
        UserApp userOwnRecipe;

//        @ManyToMany(mappedBy = "favoriteRecipeModels",cascade = CascadeType.ALL, fetch= FetchType.EAGER)
       @ManyToMany(mappedBy = "favoriteRecipeModels",cascade=CascadeType.ALL, fetch = FetchType.EAGER)
        private List<UserApp> userFavRecipe;

        @OneToMany(mappedBy = "recipes_ingredient" ,cascade = CascadeType.ALL)
        List<Ingredient> ingredientModels;

        @OneToMany(mappedBy = "recipes_instruction",cascade = CascadeType.ALL)
        List<InstructionModel> instructionModels;


        public RecipeModel() {
            }

    public RecipeModel(String name, String  description, String thumbnail_url ) {
                this.name = name;
                this.description = description;
                this.thumbnail_url = thumbnail_url;
        }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public void setRecipe_id(int recipe_id) {
        this.recipe_id = recipe_id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setRecipeName(String name) {
        this.name = name;
    }

    public String getThumbnail_url() {
        return thumbnail_url;
    }

    public void setThumbnail_url(String thumbnail_url) {
        this.thumbnail_url = thumbnail_url;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public UserApp getUserOwnRecipe() {
        return userOwnRecipe;
    }

    public void setUserOwnRecipe(UserApp userOwnRecipe) {
        this.userOwnRecipe = userOwnRecipe;
    }


    public List<UserApp> getUserFavRecipe() {
        return userFavRecipe;
    }

    public void setUserFavRecipe(List<UserApp> userFavRecipe) {
        this.userFavRecipe = userFavRecipe;
    }

    public int getRecipe_id() {
                return recipe_id;
        }

    public List<Ingredient> getIngredientModels() {
        return ingredientModels;
    }

    public void setIngredientModels(List<Ingredient> ingredientModels) {
        this.ingredientModels = ingredientModels;
    }

    public List<InstructionModel> getInstructionModels() {
        return instructionModels;
    }

    public void setInstructionModels(List<InstructionModel> instructionModels) {
        this.instructionModels = instructionModels;
    }

    @Override
        public String toString() {
                return "Recipe{" +
                        "id=" + recipe_id +
                        ", name='" + name + '\'' +
                        ", description='" + description + '\'' +
                        ", thumbnail_url='" + thumbnail_url + '\'' +
//                        ", original_video_url='" + original_video_url + '\'' +
//                        ", instructions=" + instructions +
//                        ", sections=" + sections +
//                        ", comments=" + comments +
//                        ", userOwnRecipe=" + userOwnRecipe +
//                        ", userFavRecipe=" + userFavRecipe +
                        '}';
        }
}
