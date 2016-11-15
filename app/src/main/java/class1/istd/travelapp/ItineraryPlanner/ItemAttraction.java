package class1.istd.travelapp.ItineraryPlanner;

public class ItemAttraction {
    private String item_name;
    private String item_description;
    private boolean selected;

    public ItemAttraction(String item_name, String item_description) {
        this.item_name = item_name;
        this.item_description = item_description;
        this.selected = false;
    }

    public String getItem_name() {
        return item_name;
    }

    public String getItem_description() {
        return item_description;
    }

    public void setSelected(boolean s) {
        selected = s;
    }

    public boolean isSelected() {
        return selected;
    }
}
