package com.example.Recipe.Repositories;

import com.example.Recipe.Models.UserApp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserAppRepository extends JpaRepository<UserApp,Long> {
    UserApp findByUsername(String username);
}
