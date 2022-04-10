package com.example.Recipe.Models;

import lombok.Getter;
import lombok.Setter;
import javax.persistence.*;
import java.util.List;

import javax.persistence.Lob;

@Setter
@Getter
@Entity
public class Recipe {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "id", nullable = false)
        private Long id;

        @Column(nullable = false,unique = true)
        private String name;
        @Column(columnDefinition = "TEXT")
        private String description;
        @Column(length = 5000)
        private String thumbnail_url; // image
        @Column(length = 5000)
        private String original_video_url; // video
    //    private List<Instruction> instructions; // How cook ?
//        private List<Component> sections; // ingredients


        @OneToMany(mappedBy = "recipeComments")
        List<Comment> comments;


        @ManyToOne
        UserApp userOwnRecipe;

        @ManyToOne
        UserApp userFavRecipe;

        public Recipe() {
        }

        public Recipe(String name, String  description, String thumbnail_url, String original_video_url, List<Instruction> instructions,
                      List<Component> sections) {
                this.name = name;
                this.description = description;
                this.thumbnail_url = thumbnail_url;
                this.original_video_url = original_video_url;
//                this.instructions = instructions;
//                this.sections = sections;
        }

        public Long getId() {
                return id;
        }

        @Override
        public String toString() {
                return "Recipe{" +
                        "id=" + id +
                        ", name='" + name + '\'' +
                        ", description='" + description + '\'' +
                        ", thumbnail_url='" + thumbnail_url + '\'' +
                        ", original_video_url='" + original_video_url + '\'' +
//                        ", instructions=" + instructions +
//                        ", sections=" + sections +
                        ", comments=" + comments +
                        ", userOwnRecipe=" + userOwnRecipe +
                        ", userFavRecipe=" + userFavRecipe +
                        '}';
        }
}
