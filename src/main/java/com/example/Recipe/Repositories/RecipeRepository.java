package com.example.Recipe.Repositories;

import com.example.Recipe.Models.RecipeModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface RecipeRepository extends JpaRepository<RecipeModel,Integer> {


    @Query(value = "select * from recipe_model s where s.name like %:keyword% or s.description like %:keyword%", nativeQuery = true)
    List<RecipeModel> search(@Param("keyword") String keyword);


}
