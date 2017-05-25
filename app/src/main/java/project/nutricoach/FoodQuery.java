package project.nutricoach;

/**
 * Created by trinasarkar on 5/23/17.
 */

public class FoodQuery {
    String foodItem;
    String servingSize;
    double servingCount;

    public FoodQuery() {
        foodItem = "";
        servingSize = "";
        servingCount = 1;
    }

    public FoodQuery(String item_type, String item_size, double item_count) {
        foodItem = item_type;
        servingSize = item_size;
        servingCount = (int) item_count;
    }

    public String getFoodItem() {return foodItem; }
g
    public String getServingSize() {return servingSize; }

    public double getServingCount(){ return servingCount; }
}
