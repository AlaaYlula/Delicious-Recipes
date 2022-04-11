package com.example.Recipe.Repositories;

import com.example.Recipe.Models.Instruction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InstructionRepository extends JpaRepository<Instruction,Long> {
}
