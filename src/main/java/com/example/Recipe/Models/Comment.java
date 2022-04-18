package com.example.Recipe.Models;
;

import org.hibernate.annotations.Cascade;

import javax.persistence.*;


@Entity
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(columnDefinition = "TEXT")
    private String text;

    @ManyToOne
    RecipeModel recipeModelComments;

    @ManyToOne
    UserApp userComments;

    public Comment() {
    }

    public RecipeModel getRecipeModelComments() {
        return recipeModelComments;
    }

    public void setRecipeModelComments(RecipeModel recipeModelComments) {
        this.recipeModelComments = recipeModelComments;
    }

    public Comment(String text) {
        this.text = text;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public RecipeModel getRecipeComments() {
        return recipeModelComments;
    }

    public void setRecipeComments(RecipeModel recipeModelComments) {
        this.recipeModelComments = recipeModelComments;
    }

    public UserApp getUserComments() {
        return userComments;
    }

    public void setUserComments(UserApp userComments) {
        this.userComments = userComments;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "text='" + text + '\'' +
                ", recipeComments=" + recipeModelComments +
                ", userComments=" + userComments +
                '}';
    }
}
