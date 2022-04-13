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

//    @Query(value = "select * from recipe_model s where s.name like %:keyword% ", nativeQuery = true)
    @Query(value = "SELECT * FROM recipe_model JOIN ingredient ON recipe_model.recipe_id == recipes_ingredient_recipe_id WHERE ingredient.name LIKE %:keyword%", nativeQuery = true)

//    , recipe_model.name, ingredient.name\n" +
//            "FROM recipe_model\n" +
//            "INNER JOIN ingredient\n" +
//            "ON recipe_model.recipe_id = recipes_ingredient_recipe_id", nativeQuery = true)

//    SELECT name FROM dishes
//    JOIN recipes
//    ON dishes.id=recipes.id_dish
//    JOIN ingredient
//    ON recipes.id_ingredient=ingredient.id
//    WHERE ingredient.name ="A"

//    @Query(value = "select * from recipe_model AS s INNER JOIN ingredient AS p ON s.recipe_id = p.ingredient_id where p.name=?", nativeQuery = true)
//    select * from persoon AS p INNER JOIN ploeg AS ploeg ON p.ploeg_id =ploeg.id where ploeg.naam=?"


    List<Ingredient> searchIngredient(@Param("keyword") String keyword);


    //    List<Ingredient> ingrList();

}
