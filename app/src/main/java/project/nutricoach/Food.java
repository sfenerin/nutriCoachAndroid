package project.nutricoach;

/**
 * Created by Katy on 5/22/17.
 */

public class Food {
    String ID;
    String name;
    double calories;
    double protein;
    double carbs;
    double fat;


    public Food(String ID, String name, double calories, double protein, double carbs, double fat ) {
        this.ID = ID;
        this.name = name;
        this.protein = protein;
        this.calories = calories;
        this.carbs = carbs;
        this.fat = fat;
    }
    public String getID() { return ID;}

    public String getName() { return name; }

    public double getCalories() {
        return calories;
    }

    public double getProtein() {
        return protein;
    }

    public double getFat() {
        return fat;
    }

    public double getCarbs() {
        return carbs;
    }

    @Override
    public String toString() {
        return "Food ID: " + getID() + " name: " + getName() + " calories: " + getCalories();
    }
}
