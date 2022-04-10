package com.example.Recipe.Models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;



@Setter
@Getter
@Entity
public class Ingredient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    private String name;

    // Maybe we don't need it
    @ManyToOne
    Recipe recipeIngredients;
    ////////////////////////////


    public Ingredient() {
    }

    public Ingredient(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }
}
