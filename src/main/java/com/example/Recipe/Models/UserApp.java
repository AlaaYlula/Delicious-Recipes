package com.example.Recipe.Models;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCrypt;

import javax.persistence.*;
import java.util.*;
import java.sql.Date;

//@JsonIgnoreProperties({"favoriteRecipeModels"})
@Setter
@Getter
@Entity
public class UserApp implements UserDetails {
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
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date dateOfBirth;
    @Column(nullable = false)
    private String nationality;
    @Column(nullable = false)
    private String bio;

    private String flag; // added to check if the user followed or not

    private String userImage;


    @OneToOne//(fetch = FetchType.EAGER)
    @JoinColumn(name = "role_id", referencedColumnName = "id")
    private Role role;

    @OneToMany(mappedBy = "userOwnRecipe",cascade = CascadeType.ALL)
    List<RecipeModel> ownRecipeModels;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "userFavRecipes",
            joinColumns = @JoinColumn(name = "Recipe_id"),
            inverseJoinColumns = @JoinColumn(name = "User_id")
    )
    List<RecipeModel> favoriteRecipeModels;


    @OneToMany(mappedBy = "userComments",cascade = CascadeType.ALL)///////////////////////////
    List<Comment> comments;

    @ManyToMany(cascade=CascadeType.ALL)
    @JoinTable(
            name = "user_user",
            joinColumns = {@JoinColumn(name = "from_id")},
            inverseJoinColumns = {@JoinColumn(name = "to_id")}
    )
    public List<UserApp> following;

    @ManyToMany(mappedBy = "following", fetch = FetchType.EAGER,cascade=CascadeType.ALL)
    public List<UserApp> followers;

    public UserApp() {
    }

    public UserApp(String username, String password, String firstName, String lastName, Date dateOfBirth, String nationality, String bio) {
        this.username = username;
        this.password =  password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.nationality = nationality;
        this.bio = bio;
        this.userImage = "https://i.pravatar.cc/300";
    }


    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public List<RecipeModel> getOwnRecipeModels() {
        return ownRecipeModels;
    }

    public void setOwnRecipeModels(List<RecipeModel> ownRecipeModels) {
        this.ownRecipeModels = ownRecipeModels;
    }

    public List<RecipeModel> getFavoriteRecipeModels() {
        return favoriteRecipeModels;
    }

    public void setFavoriteRecipeModels(List<RecipeModel> favoriteRecipeModels) {
        this.favoriteRecipeModels = favoriteRecipeModels;
    }

    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
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

    ////////////////////////////////////// Security ////////////////////////////////
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(role.getName()));
        return authorities;
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
    public List<RecipeModel> getOwnRecipes() {
        return ownRecipeModels;
    }

    public void setOwnRecipes(List<RecipeModel> ownRecipeModels) {
        this.ownRecipeModels = ownRecipeModels;
    }

    public List<RecipeModel> getFavoriteRecipes() {
        return favoriteRecipeModels;
    }

    public void setFavoriteRecipes(List<RecipeModel> favoriteRecipeModels) {
        this.favoriteRecipeModels = favoriteRecipeModels;
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
//                ", role=" + role +
//                ", ownRecipes=" + ownRecipeModels +
//                ", favoriteRecipes=" + favoriteRecipeModels +
//                ", comments=" + comments +
//                ", following=" + following +
//                ", followers=" + followers +
                '}';
    }
}
