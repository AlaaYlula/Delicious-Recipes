package com.example.Recipe.Models;
;

import javax.persistence.*;


@Entity
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    private String text;

    @ManyToOne
    Recipe recipeComments;

    @ManyToOne
    UserApp userComments;

    public Comment() {
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

    public Recipe getRecipeComments() {
        return recipeComments;
    }

    public void setRecipeComments(Recipe recipeComments) {
        this.recipeComments = recipeComments;
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
                ", recipeComments=" + recipeComments +
                ", userComments=" + userComments +
                '}';
    }
}
