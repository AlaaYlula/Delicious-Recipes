package com.example.Recipe.Models;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;


@Setter
@Getter
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
    UserApp userComments;///////////////////////////

    public Comment() {
    }

    public Comment(String text) {
        this.text = text;
    }

    public Long getId() {
        return id;
    }
}
