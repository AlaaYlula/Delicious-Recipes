package com.example.Recipe.Models;

import org.hibernate.annotations.Cascade;

import javax.persistence.*;


@Entity
public class Ingredient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Ingredient_id", nullable = false)
    private Long ingredientId;

    private String name;

    @ManyToOne
    RecipeModel recipes_ingredient;

    public Ingredient() {

    }
    public Ingredient(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getIngredientId() {
        return ingredientId;
    }

    public void setIngredientId(Long ingredientId) {
        this.ingredientId = ingredientId;
    }

    public RecipeModel getRecipes_ingredient() {
        return recipes_ingredient;
    }

    public void setRecipes_ingredient(RecipeModel recipesModel) {
        this.recipes_ingredient = recipesModel;
    }

    @Override
    public String toString() {
        return name;
    }
}
