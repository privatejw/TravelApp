package class1.istd.travelapp.ItineraryPlanner;

public class ItemAttraction {
    private String item_name;
    private int item_rating;
    private boolean selected;

    public ItemAttraction(String item_name, int item_rating) {
        this.item_name = item_name;
        this.item_rating = item_rating;
        this.selected = false;
    }

    public String getItem_name() {
        return item_name;
    }

    public int getItem_rating() {
        return item_rating;
    }

    public void setSelected(boolean s) {
        selected = s;
    }

    public boolean isSelected() {
        return selected;
    }
}
