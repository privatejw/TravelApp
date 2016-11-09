package class1.istd.travelapp;

/**
 * Created by privatejw on 9/11/2016.
 *
 * Attempts an implementation of the Travelling Salesman Problem
 */

public class RouteOptimisation {


    double getAggregateScore(double time, double price) {
//        return (1 + price) * time;
        return 0.6*time + 0.4*price;
    }

    private class Node {}
}
