package com.example.Recipe.Models;

import javax.persistence.*;


@Entity
public class Ingredient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Ingredient_id", nullable = false)
    private Long Ingredient_id;

    public String name;
    @OneToOne
    @JoinColumn (name = "component_id")
    Component component;

    public Ingredient() {
    }

    public Ingredient(String name) {
        this.name = name;
    }

    public Component getComponent() {
        return component;
    }

    public void setComponent(Component component) {
        this.component = component;
    }

    public Long getIngredient_id() {
        return Ingredient_id;
    }

    public void setIngredient_id(Long ingredient_id) {
        Ingredient_id = ingredient_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }



    @Override
    public String toString() {
        return "Ingredient{" +
                "name='" + name + '\'' +
                '}';
    }
}
