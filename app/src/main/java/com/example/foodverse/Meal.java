package com.example.foodverse;


import java.util.ArrayList;
import java.util.Date;


/**
 * A class to represent a meal. A meal can have a recipe,
 * a list of ingredients, and a date.
 *
 * @version 1.0
 */
public class Meal implements Comparable<Meal> {
    ArrayList<Ingredient> ingredients;
    // Recipe recipe;
    Date date;
    int recipeHashCode = 0;
    String recipeTitle = "No Recipe";
    int servings = 0;
    int servingScaling = 1;
    String name = "";

    /**
     * A constructor for meal taking no parameters.
     * @since version 1.0
     */
    public Meal() {
        this.ingredients = new ArrayList<Ingredient>();
        this.date = new Date();
    }

    /**
     * A meal constructor allowing for the creation of a meal
     * with a list of ingredients and a date
     * @param ingredients An {@link ArrayList<Ingredient>} to be given to a meal
     * @param date A {@link Date} for the meal to be set
     */
    public Meal(ArrayList<Ingredient> ingredients, Date date) {
        this.ingredients = ingredients;
        this.date = date;
    }

    /**
     * Generates a hash code for the meal by summing the hash codes of its
     * members. Should be equal for equal meal objects.
     *
     * @return An integer primitive representing the hash code for the meal.
     */
    public int hashCode() {
        // TODO: Add recipe hashCode when complete
        int hash = 0;
        hash += ingredients.hashCode() + date.hashCode();
        return hash;
    }

    /**
     * Returns an {@link ArrayList<Ingredient>} containing the ingredients in the meal
     * @return An {@link ArrayList<Ingredient>} with the ingredients of the meal
     * @since version 1.0
     */
    public ArrayList<Ingredient> getIngredients() {
        return ingredients;
    }

    /**
     * Sets the ingredients of the meal
     * @param ingredients An {@link ArrayList<Ingredient>} to be added to the meal
     * @since version 1.0
     */
    public void setIngredients(ArrayList<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    /**
     * Returns the date of the meal
     * @return The {@link Date} of the meal
     * @since version 1.0
     */
    public Date getDate() {
        return this.date;
    }

    /**
     * Sets the date of the meal to the given date
     * @param date The {@link Date} that will be set for the meal.
     * @since version 1.0
     */
    public void setDate(Date date) {
        this.date = date;
    }

    /**
     * Adds a recipe to the meal.
     * @param recipeHashCode An {@link int} that represents the hash code of the recipe
     * @param name A {@link String} that represents the meal's name
     */
    public void addRecipe(int recipeHashCode, String name) {
        this.recipeHashCode = recipeHashCode;
        this.recipeTitle = name;
    }

    /**
     * Returns the title of the meal's recipe
     * @return The {@link String} of the meal's recipe's name.
     */
    public String getRecipeTitle() {
        return this.recipeTitle;
    }

    /**
     * Returns the hash code of the meal's recipe
     * @return An {@link int} representing the recipe's hash code
     */
    public int getRecipeHashCode() {
        return this.recipeHashCode;
    }

    /**
     * Returns the number of servings the meal has. Only for meals with recipes.
     * @return An {@link int} representing the number of servings from a meal
     */
    public int getServings() {
        return this.servings;
    }

    /**
     * Returns the scaling of the meal
     * @return
     */
    public int getServingScaling() {
        return this.servingScaling;
    }

    /**
     * Sets the number of servings that a recipe within the meal has
     * @param servings An {@link int} representing the number of servings
     */
    public void setServings(int servings) {
        this.servings = servings;
    }

    /**
     * Sets the number by which the servings is scaled by
     * @param scale An {@link int} representing the scaling number
     */
    public void setServingScaling(int scale) {
        this.servingScaling = scale;
    }

    /**
     * Sets the name of the meal
     * @param name A {@link String} representing the meal's name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the name of the meal
     * @return A {@link String} of the meals name
     */
    public String getName() {
        return this.name;
    }

    /**
     * Allows the meals to be sorted in order of increasing date
     * @param meal A {@link Meal} to compare to this meal
     * @return An {@link int} representing the comparison
     */
    @Override
    public int compareTo(Meal meal) {
        return this.date.compareTo(meal.getDate());
    }
}
