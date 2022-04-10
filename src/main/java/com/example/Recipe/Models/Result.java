package com.example.Recipe.Models;

import java.util.List;

public class Result {

    List<Recipe> results;

    public Result(List<Recipe> results) {
        this.results = results;
    }

    public List<Recipe> getResults() {
        return results;
    }

    public void setResults(List<Recipe> results) {
        this.results = results;
    }

    @Override
    public String toString() {
        return "Result{" +
                "Recipes =" + results +
                '}';
    }
}
