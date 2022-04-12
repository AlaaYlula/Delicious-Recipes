package com.example.Recipe.Repositories;

import com.example.Recipe.Models.InstructionModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InstructionRepository extends JpaRepository<InstructionModel, Long> {
}
