package com.example.Recipe.Models;

public class Component {
    private String raw_text;

    public Component(String raw_text) {
        this.raw_text = raw_text;
    }

    public String getRaw_text() {
        return raw_text;
    }

    public void setRaw_text(String raw_text) {
        this.raw_text = raw_text;
    }

    @Override
    public String toString() {
        return "Component{" +
                "raw_text='" + raw_text + '\'' +
                '}';
    }
}
