package com.example.Recipe.Models;


import javax.persistence.*;

@Entity
public class Instruction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    private int position;
    @Column(columnDefinition = "TEXT")
    private String display_text;

    @ManyToOne
    Recipe recipe;

    public Instruction() {
    }

    public Instruction(int position, String display_text) {
        this.position = position;
        this.display_text = display_text;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getDisplay_text() {
        return display_text;
    }

    public void setDisplay_text(String display_text) {
        this.display_text = display_text;
    }


    public Long getTable_id() {
        return id;
    }

    public void setTable_id(Long id) {
        this.id = id;
    }


    public Recipe getRecipe() {
        return recipe;
    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }

    @Override
    public String toString() {
        return "Instruction{" +
                "position='" + position + '\'' +
                ", display_text='" + display_text + '\'' +
                '}';
    }
}
