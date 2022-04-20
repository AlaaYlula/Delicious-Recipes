package com.example.Recipe.Models;

import javax.persistence.*;
import java.util.Set;

@Entity
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    private String name;
/////////////////////////////////////////
//    @ManyToMany(mappedBy = "roles",fetch = FetchType.EAGER)
//    private Set<UserApp> user1;
//
//    public Set<UserApp> getUser1() {
//        return user1;
//    }
//
//    public void setUser1(Set<UserApp> user1) {
//        this.user1 = user1;
//    }
///////////////////////////////////////////
    @OneToOne(mappedBy = "role")
    private UserApp user;


    public Role() {
    }


    public void setId(Long id) {
        this.id = id;
    }


    public Role(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Long getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UserApp getUser() {
        return user;
    }

    public void setUser(UserApp user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "Role{" +
                "name='" + name + '\'' +
//                ", user=" + user +
                '}';
    }
}
