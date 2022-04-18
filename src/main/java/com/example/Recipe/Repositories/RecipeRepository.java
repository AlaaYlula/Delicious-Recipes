package com.example.Recipe.Repositories;

import com.example.Recipe.Models.Ingredient;
import com.example.Recipe.Models.RecipeModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.transaction.Transactional;


@Repository
public interface RecipeRepository extends JpaRepository<RecipeModel, Integer> {


    @Query(value = "select * from recipe_model s where s.name like %:keyword% or s.description like %:keyword%", nativeQuery = true)
    List<RecipeModel> search(@Param("keyword") String keyword);


    @Transactional
    @Modifying
    @Query("update RecipeModel r set r.name = ?1, r.description = ?2 where r.recipe_id = ?3")
    int updateRecipeModelById(String name, String description, Integer id);

//    @Transactional
//    @Modifying
//    @Query("update RecipeModel r set r.name = ?1, r.description = ?2 r.ingredientModels = ?3 where r.recipe_id = ?4")
//    int updateRecipeModelById(String name, String description, List<Ingredient> ingredientModels, Integer id);

    @Query(value = "SELECT * FROM recipe_model R INNER JOIN ingredient I ON R.recipe_id = I.recipes_ingredient_recipe_id AND I.name LIKE %:keyword%", nativeQuery = true)
    List<RecipeModel> searchIngredient(@Param("keyword") String keyword);

}
