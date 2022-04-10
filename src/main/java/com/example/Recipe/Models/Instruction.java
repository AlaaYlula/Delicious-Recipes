package com.example.Recipe.Models;

public class Instruction {
    private String position;
    private String display_text;

    public Instruction(String position, String display_text) {
        this.position = position;
        this.display_text = display_text;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getDisplay_text() {
        return display_text;
    }

    public void setDisplay_text(String display_text) {
        this.display_text = display_text;
    }

    @Override
    public String toString() {
        return "Instruction{" +
                "position='" + position + '\'' +
                ", display_text='" + display_text + '\'' +
                '}';
    }
}
