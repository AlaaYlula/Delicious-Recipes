package com.example.Recipe.Repositories;

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

    @Query(value = "select * from recipe_model s where s.recipe_name like %:keyword% or s.description like %:keyword%", nativeQuery = true)
    List<RecipeModel> search(@Param("keyword") String keyword);

    @Query(value = "SELECT * FROM recipe_model r INNER JOIN ingredient i ON r.recipe_id = i.recipes_ingredient_recipe_id AND i.name LIKE %:keywords%", nativeQuery = true)
    List<RecipeModel> searchIngredient(@Param("keywords") String keyword);


    RecipeModel findRecipeModelById(Integer id);

//    @Transactional
//    @Modifying
//    @Query("update recipe_model r set r.recipe_name = ?1, r.description = ?2 where r.recipe_id = ?3")
//    int updateRecipeModelById(String name, String description, Integer id); // return recipe not int

}
