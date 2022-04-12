package com.example.Recipe.Repositories;

import com.example.Recipe.Models.RecipeModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface RecipeRepository extends JpaRepository<RecipeModel,Integer> {
}
