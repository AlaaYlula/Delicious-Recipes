package com.example.Recipe.Models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;


@Setter
@Getter
@Entity
public class Recipe {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "id", nullable = false)
        private Long id;

        @Column(nullable = false,unique = true)
        private String title;
        private String image;

        // List of Ingredients


        @OneToMany(mappedBy = "recipeComments")
        List<Comment> comments;

        // Maybe we don't need it
        @OneToMany(mappedBy = "recipeIngredients")
        List<Ingredient> ingredients;
        ///////////////////////////


        @ManyToOne
        UserApp userOwnRecipe;

        @ManyToOne
        UserApp userFavRecipe;

        public Recipe() {
        }

        public Recipe(String title, String image) {
                this.title = title;
                this.image = image;
        }

        public Long getId() {
                return id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        @Override
        public String toString() {
            return "Recipe{" +
                    "title='" + title + '\'' +
                    ", image='" + image + '\'' +
                    '}';
        }

}
