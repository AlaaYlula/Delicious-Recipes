package com.example.Recipe.Repositories;

import com.example.Recipe.Models.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface CommentRepository extends JpaRepository<Comment,Long> {


}
