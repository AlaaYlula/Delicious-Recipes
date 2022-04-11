package com.example.Recipe.Models;

import lombok.*;

import javax.persistence.*;
import java.util.Date;
import java.util.List;


@Setter
@Getter
@Entity
public class UserApp {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true,nullable = false)
    private String username;
    @Column(nullable = false)
    private String password;
    @Column(nullable = false)
    private String firstName;
    @Column(nullable = false)
    private String lastName;
    @Column(nullable = false)
    private Date dateOfBirth;
    @Column(nullable = false)
    private String nationality;
    @Column(nullable = false)
    private String bio;

    @OneToOne
    @JoinColumn(name = "role_id", referencedColumnName = "id")
    private Role role;

    @OneToMany(mappedBy = "userOwnRecipe")
    List<Recipe> ownRecipes;

    @OneToMany(mappedBy = "userFavRecipe")
    List<Recipe> favoriteRecipes;

    @OneToMany(mappedBy = "userComments")///////////////////////////
    List<Comment> comments;

    @ManyToMany
    @JoinTable(
            name = "user_user",
            joinColumns = {@JoinColumn(name = "from_id")},
            inverseJoinColumns = {@JoinColumn(name = "to_id")}
    )
    public List<UserApp> following;

    @ManyToMany(mappedBy = "following", fetch = FetchType.EAGER)
    public List<UserApp> followers;

    public UserApp() {
    }

    public UserApp(String username, String password, String firstName, String lastName, Date dateOfBirth, String nationality, String bio, Role role) {
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.nationality = nationality;
        this.bio = bio;
        this.role = role;

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public List<Recipe> getOwnRecipes() {
        return ownRecipes;
    }

    public void setOwnRecipes(List<Recipe> ownRecipes) {
        this.ownRecipes = ownRecipes;
    }

    public List<Recipe> getFavoriteRecipes() {
        return favoriteRecipes;
    }

    public void setFavoriteRecipes(List<Recipe> favoriteRecipes) {
        this.favoriteRecipes = favoriteRecipes;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public List<UserApp> getFollowing() {
        return following;
    }

    public void setFollowing(List<UserApp> following) {
        this.following = following;
    }

    public List<UserApp> getFollowers() {
        return followers;
    }

    public void setFollowers(List<UserApp> followers) {
        this.followers = followers;
    }

    @Override
    public String toString() {
        return "UserApp{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", dateOfBirth=" + dateOfBirth +
                ", nationality='" + nationality + '\'' +
                ", bio='" + bio + '\'' +
                ", role=" + role +
                ", ownRecipes=" + ownRecipes +
                ", favoriteRecipes=" + favoriteRecipes +
                ", comments=" + comments +
                ", following=" + following +
                ", followers=" + followers +
                '}';
    }
}
