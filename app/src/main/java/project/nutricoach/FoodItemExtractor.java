package project.nutricoach;

/**
 * Created by Shawn on 5/18/2017.
 */

public class FoodItemExtractor {
    String foodItem;
    int servingCount;

    public FoodItemExtractor(){

    }

    public boolean foundFoodItem(String input){
        foodItem = input;
        servingCount = 1;
        return true;
    }

    public String getFoodItem(){
        return foodItem;
    }

    public int getServingCount(){ return servingCount; }
}
