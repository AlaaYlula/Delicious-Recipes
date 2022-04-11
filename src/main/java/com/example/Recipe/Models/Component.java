package com.example.Recipe.Models;

import javax.persistence.*;

@Entity
public class Component {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "component_id", nullable = false)
    private Long component_id;


    @OneToOne(mappedBy = "component")
    public Ingredient ingredient;


    @ManyToOne
    Section section;

    public Long getComponent_id() {
        return component_id;
    }

    public void setComponent_id(Long component_id) {
        this.component_id = component_id;
    }

    public Section getSection() {
        return section;
    }

    public void setSection(Section section) {
        this.section = section;
    }

    public Component(Ingredient ingredient) {
        this.ingredient = ingredient;
    }

    public Ingredient getIngredient() {
        return ingredient;
    }

    public void setIngredient(Ingredient ingredient) {
        this.ingredient = ingredient;
    }


    @Override
    public String toString() {
        return "Component{" +
                "ingredient=" + ingredient +
                '}';
    }
}
