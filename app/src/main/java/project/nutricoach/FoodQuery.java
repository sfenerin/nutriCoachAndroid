package project.nutricoach;

/**
 * Created by trinasarkar on 5/23/17.
 */

public class FoodQuery {
    String foodItem;
    String servingSize;
    double servingCount;
    String sentiment;

    public FoodQuery() {
        foodItem = "";
        servingSize = "";
        servingCount = 1;
        sentiment = "";
    }

    public FoodQuery(String item_type, String item_size, double item_count, String sentiment_value) {
        foodItem = item_type;
        servingSize = item_size;
        servingCount = (int) item_count;
        sentiment = sentiment_value;
    }

    public String getFoodItem() {return foodItem; }

    public String getServingSize() {return servingSize; }

    public double getServingCount(){ return servingCount; }

    public String getSentiment(){ return sentiment; }
}
