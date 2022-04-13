package com.example.Recipe.Models;

import javax.persistence.*;


@Entity
public class Ingredient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Ingredient_id", nullable = false)
    private Long ingredientId;

    private String name;

    public Ingredient(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @ManyToOne
    RecipeModel recipes_ingredient;

    public Ingredient() {

    }


    public RecipeModel getRecipes_ingredient() {
        return recipes_ingredient;
    }

    public void setRecipes_ingredient(RecipeModel recipesModel) {
        this.recipes_ingredient = recipesModel;
    }
}
