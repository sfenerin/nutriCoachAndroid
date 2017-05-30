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

    public FoodQuery(String item_type) {
        foodItem = item_type;
        servingSize = "";
        servingCount = 1;
        sentiment = "";
    }

    public FoodQuery(String foodItem, String servingSize, double servingCount, String sentiment) {
        this.foodItem = foodItem;
        this.servingSize = servingSize;
        this.servingCount = servingCount;
        this.sentiment = sentiment;
    }


    public void printFoodQueryInfo() {
        System.out.println("Item type: " + this.foodItem +
                "\n Serving Size: " + this.servingSize +
                "\n Serving Count: " + this.servingCount +
                "\n Sentiment: " + this.sentiment);
    }

    public void setItemType(String item_type) { this.foodItem = item_type; }

    public void setServingSize(String item_size) { this.servingSize = item_size; }

    public void setServingCount(double item_count) { servingCount = item_count; }

    public void setSentiment(String sentiment_value) { this.sentiment = sentiment_value; }

    public String getFoodItem() {return foodItem; }

    public String getServingSize() {return servingSize; }

    public double getServingCount(){ return servingCount; }

    public String getSentiment(){ return sentiment; }
}
