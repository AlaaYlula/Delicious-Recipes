package com.example.Recipe.Models;

import jdk.jfr.Enabled;

import javax.persistence.*;

@Entity
public class Instruction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "table_id", nullable = false)
    private Long table_id;

    private int id;
    private int position;
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
        return table_id;
    }

    public void setTable_id(Long table_id) {
        this.table_id = table_id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
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
