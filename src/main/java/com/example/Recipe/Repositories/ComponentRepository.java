package com.example.Recipe.Repositories;

import com.example.Recipe.Models.Component;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ComponentRepository extends JpaRepository<Component,Long> {
}
