package com.example.Recipe.Repositories;

import com.example.Recipe.Models.RecipeModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.transaction.Transactional;


@Repository
public interface RecipeRepository extends JpaRepository<RecipeModel,Integer> {

    RecipeModel findRecipeModelById(int id);


    @Transactional
    @Modifying
    @Query("update RecipeModel r set r.name = ?1, r.description = ?2 where r.recipe_id = ?3")
    int updateRecipeModelById(String name, String description, Integer id);

}
