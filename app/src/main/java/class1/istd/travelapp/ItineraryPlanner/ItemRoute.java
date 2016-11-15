package class1.istd.travelapp.ItineraryPlanner;

import java.io.Serializable;

public class ItemRoute implements Serializable{
    private static final long serialVersionUID = 0L;
    private String location;
    private int imagePathType;
    private String routeInfo;

    public ItemRoute(String location, int imagePathType, String routeInfo) {
        this.location = location;
        this.imagePathType = imagePathType;
        this.routeInfo = routeInfo;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setImagePathType(int imagePathType) {
        this.imagePathType = imagePathType;
    }

    public void setRouteInfo(String routeInfo) {
        this.routeInfo = routeInfo;
    }

    public String getLocation() {
        return location;
    }

    public int getImagePathType() {
        return imagePathType;
    }

    public String getRouteInfo() {
        return routeInfo;
    }
}
