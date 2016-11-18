package class1.istd.travelapp;

import java.lang.reflect.Array;
import java.util.HashMap;

/**
 * Created by privatejw on 11/11/2016.
 */

public class Node {
    String name;
    HashMap<String, double[]> cost;
    Node(String name) {
        this.name = name;
    }

    public void setCost(HashMap<String, double[]> cost) {
        this.cost = cost;
    }
}
