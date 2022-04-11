package com.example.Recipe.Models;

import javax.persistence.*;
import java.util.List;

@Entity
public class Instructions { // like Result

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    Instruction instructions ;

    public Instructions() {
    }

    public Instructions(Instruction instructions) {
        this.instructions = instructions;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instruction getInstructions() {
        return instructions;
    }

    public void setInstructions(Instruction instructions) {
        this.instructions = instructions;
    }



    @Override
    public String toString() {
        return "Instructions{" +
                "instructions=" + instructions +
                '}';
    }
}
