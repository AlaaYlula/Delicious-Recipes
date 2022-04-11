package com.example.Recipe.Models;

import lombok.Getter;
import lombok.Setter;
import javax.persistence.*;
import java.util.List;

import javax.persistence.Lob;


@Entity
public class Recipe {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "id", nullable = false)
        private Long id;

        @Column(nullable = false,unique = true)
        private String name;
      //  @Column(columnDefinition = "TEXT")
       // private String description;
        @Column(length = 5000)
        private String thumbnail_url; // image
        @Column(length = 5000)
        private String original_video_url; // video

        @OneToMany(mappedBy = "instructions")
        private List<Instructions> instructions; // How cook ?

//        @OneToMany
//        private  List<Sections> sections; // Ingredients

        @OneToMany(mappedBy = "recipeComments")
        List<Comment> comments;


        @ManyToOne
        UserApp userOwnRecipe;

        @ManyToOne
        UserApp userFavRecipe;

        public Recipe() {
        }

        public Recipe(String name, String  description, String thumbnail_url, String original_video_url, List<Instructions> instructions
                     ) {
                this.name = name;
              //  this.description = description;
                this.thumbnail_url = thumbnail_url;
                this.original_video_url = original_video_url;
                this.instructions = instructions;
           //     this.sections = sections;
        }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getThumbnail_url() {
        return thumbnail_url;
    }

    public void setThumbnail_url(String thumbnail_url) {
        this.thumbnail_url = thumbnail_url;
    }

    public String getOriginal_video_url() {
        return original_video_url;
    }

    public void setOriginal_video_url(String original_video_url) {
        this.original_video_url = original_video_url;
    }

    public List<Instructions> getInstructions() {
        return instructions;
    }

    public void setInstructions(List<Instructions> instructions) {
        this.instructions = instructions;
    }

//    public List<Sections> getSections() {
//        return sections;
//    }
//
//    public void setSections(List<Sections> sections) {
//        this.sections = sections;
//    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public UserApp getUserOwnRecipe() {
        return userOwnRecipe;
    }

    public void setUserOwnRecipe(UserApp userOwnRecipe) {
        this.userOwnRecipe = userOwnRecipe;
    }

    public UserApp getUserFavRecipe() {
        return userFavRecipe;
    }

    public void setUserFavRecipe(UserApp userFavRecipe) {
        this.userFavRecipe = userFavRecipe;
    }

    public Long getId() {
                return id;
        }

        @Override
        public String toString() {
                return "Recipe{" +
                        "id=" + id +
                        ", name='" + name + '\'' +
                      //  ", description='" + description + '\'' +
                        ", thumbnail_url='" + thumbnail_url + '\'' +
                        ", original_video_url='" + original_video_url + '\'' +
                        ", instructions=" + instructions +
//                        ", sections=" + sections +
                        ", comments=" + comments +
                        ", userOwnRecipe=" + userOwnRecipe +
                        ", userFavRecipe=" + userFavRecipe +
                        '}';
        }
}
