package com.example.Recipe.Models;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Section {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "section", nullable = false)
    private Long section_id;

    @OneToMany(mappedBy = "section" )
    private List<Component> components;

    @ManyToOne
    Recipe recipeSections;


    public Section(List<Component> components) {
        this.components = components;
    }

    public List<Component> getComponents() {
        return components;
    }

    public void setComponents(ArrayList<Component> components) {
        this.components = components;
    }

    public Long getSection_id() {
        return section_id;
    }

    public void setSection_id(Long section_id) {
        this.section_id = section_id;
    }

    public void setComponents(List<Component> components) {
        this.components = components;
    }

    public Recipe getRecipeSections() {
        return recipeSections;
    }

    public void setRecipeSections(Recipe recipeSections) {
        this.recipeSections = recipeSections;
    }

    @Override
    public String toString() {
        return "Section{" +
                "components=" + components +
                '}';
    }
}
