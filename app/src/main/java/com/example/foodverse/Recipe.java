package com.example.foodverse;

import android.widget.TextView;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.ArrayList;

public class Recipe implements Serializable {
   private String title;
   private Integer prep_time; //in mins
   private Integer servings;
   private String category;
   private String comments;
   //private ArrayList<Ingredient> ingredientArrayList;

    public Recipe(String title, int prep_time, int servings, String category, String comments) {
        this.title = title; //add char limit
        this.prep_time = prep_time;
        this.servings = servings;
        this.category = category;
        this.comments = comments; //add limit
        //this.ingredientArrayList = ingredientArrayList;
    }

    public int hashCode() {
        int hash = 0;
        hash += title.hashCode() + category.hashCode() + comments.hashCode()
                + prep_time + servings; // + ingredientArrayList.hashCode();
        return hash;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getPrepTime() {
        return prep_time;
    }

    public void setPrepTime(int prep_time) {
        this.prep_time = prep_time;
    }

    public Integer getServings() {
        return servings;
    }

    public void setServings(int servings) {
        this.servings = servings;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

//    public ArrayList<Ingredient> getIngredientArrayList() {
//        return ingredientArrayList;
//    }
//
//    public void setIngredientArrayList(ArrayList<Ingredient> ingredientArrayList) {
//        this.ingredientArrayList = ingredientArrayList;
//    }

}
