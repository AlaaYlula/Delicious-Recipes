package com.example.Recipe.Repositories;

import com.example.Recipe.Models.InstructionModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InstructionRepository extends JpaRepository<InstructionModel, Long> {



}
