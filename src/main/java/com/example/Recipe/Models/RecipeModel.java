package com.example.Recipe.Models;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@JsonIgnoreProperties({"userFavRecipe"})
@Entity
public class RecipeModel {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "recipe_id")
        private int recipe_id;

        private int id;
        @Column(nullable = false,unique = true)
        private String name;
        @Column(columnDefinition = "TEXT")
        private String description;
        @Column(length = 5000)
        private String thumbnail_url; // image
        @Column(length = 5000)
//        private String original_video_url; // video

//        @OneToMany(mappedBy = "recipe")
//        private List<Instruction> instructions; // How cook ?
//
//        @OneToMany(mappedBy = "recipeSections") // Ingredients
//        public List<Section> sections;


        @OneToMany(mappedBy = "recipeModelComments")
        List<Comment> comments;


        @ManyToOne
        UserApp userOwnRecipe;

        @ManyToOne
        UserApp userFavRecipe;

        public RecipeModel() {
        }


    public RecipeModel(String name, String  description, String thumbnail_url ) {
                this.name = name;
                this.description = description;
                this.thumbnail_url = thumbnail_url;
//                this.original_video_url = original_video_url;
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

//    public List<Section> getSections() {
//        return sections;
//    }

//    public void setSections(List<Section> sections) {
//        this.sections = sections;
//    }

    public void setRecipe_id(int recipe_id) {
        this.recipe_id = recipe_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getThumbnail_url() {
        return thumbnail_url;
    }

    public void setThumbnail_url(String thumbnail_url) {
        this.thumbnail_url = thumbnail_url;
    }

//    public String getOriginal_video_url() {
//        return original_video_url;
//    }
//
//    public void setOriginal_video_url(String original_video_url) {
//        this.original_video_url = original_video_url;
//    }

//    public List<Instruction> getInstructions() {
//        return instructions;
//    }

//    public void setInstructions(List<Instruction> instructions) {
//        this.instructions = instructions;
//    }


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

    public UserApp getUserFavRecipe() {
        return userFavRecipe;
    }

    public void setUserFavRecipe(UserApp userFavRecipe) {
        this.userFavRecipe = userFavRecipe;
    }

    public int getRecipe_id() {
                return recipe_id;
        }


    @OneToMany(mappedBy = "recipes_ingredient")
    Set<Ingredient> ingredientModels;

    @OneToMany(mappedBy = "recipes_instruction")
    Set<InstructionModel> instructionModels;

    public Set<Ingredient> getIngredientModels() {
        return ingredientModels;
    }

    public void setIngredientModels(Set<Ingredient> ingredientModels) {
        this.ingredientModels = ingredientModels;
    }

    public Set<InstructionModel> getInstructionModels() {
        return instructionModels;
    }

    public void setInstructionModels(Set<InstructionModel> instructionModels) {
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
                        ", userFavRecipe=" + userFavRecipe +
                        '}';
        }
}
