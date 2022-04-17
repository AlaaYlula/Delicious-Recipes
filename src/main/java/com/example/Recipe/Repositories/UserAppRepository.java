package com.example.Recipe.Repositories;

import com.example.Recipe.Models.RecipeModel;
import com.example.Recipe.Models.UserApp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.sql.Date;
import java.util.List;

@Repository
public interface UserAppRepository extends JpaRepository<UserApp,Long> {
    UserApp findByUsername(String username);

    UserApp findUserAppById(Long id);

}
