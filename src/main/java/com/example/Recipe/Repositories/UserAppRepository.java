package com.example.Recipe.Repositories;

import com.example.Recipe.Models.UserApp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.sql.Date;

@Repository
public interface UserAppRepository extends JpaRepository<UserApp,Long> {
    UserApp findByUsername(String username);

    UserApp findUserAppById(Long id);

    // TODO: 4/20/2022  
    @Transactional
    @Modifying
    @Query(value= "update user_app set username = ?1, " +
            " password = ?2, first_name = ?3, last_name = ?4," +
            " date_of_birth = ?5, nationality = ?6," +
            "bio = ?7 where id = ?8" , nativeQuery = true)
    int updateUserInfo(String username, String password, String first_name,
                       String last_name, Date date_of_birth, String nationality, String bio, Integer id);

}
