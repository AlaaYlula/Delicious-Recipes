package com.example.Recipe.Repositories;

import com.example.Recipe.Models.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;


@Repository
public interface IngredientRepository extends JpaRepository<Ingredient,Long> {

}

