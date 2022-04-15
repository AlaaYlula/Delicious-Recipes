package com.example.Recipe.Repositories;

import com.example.Recipe.Models.Ingredient;
import com.example.Recipe.Models.RecipeModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IngredientRepository extends JpaRepository<Ingredient,Long> {
//    SELECT new Users(e.id, e.email, e.firstname, e.surname) FROM Users e

//    @Query(value = "SELECT * FROM recipe_model r INNER JOIN ingredient i ON r.recipe_id = i.recipes_ingredient_recipe_id AND i.name LIKE %:keywords%", nativeQuery = true)
//    List<RecipeModel> searchIngredient(@Param("keywords") String keyword);


//    @Query(value = "SELECT * FROM recipe_model INNER JOIN ingredient ON recipe_model.recipe_id = ingredient.recipes_ingredient_recipe_id AND ingredient.name LIKE %:keywords% ", nativeQuery = true) //+
            /// list of objects

}

