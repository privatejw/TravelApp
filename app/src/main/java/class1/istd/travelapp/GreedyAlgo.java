package class1.istd.travelapp;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;

import class1.istd.travelapp.ItineraryPlanner.ItemRoute;

/**
 * Created by dilyl on 11/17/2016.
 */

public class GreedyAlgo {
    MyDatabase db;
    int[] transport;
    public GreedyAlgo() {
        db = new MyDatabase();
    }
    public String[] bestpath(ArrayList<String> places,String hotel) {
        double currentWT, lowestWT;
        String nextDest;
        String[] path = new String[places.size() + 2];
        path[0] = hotel;

        for (int j=0; j<path.length-1;j++) {
            lowestWT = 1000;
            nextDest = "";
            for (int i = 0; i < places.size(); i++) {
                currentWT = (db.database.get(path[j]).get(places.get(i)))[5];
                if (currentWT < lowestWT){
                    lowestWT = currentWT;
                    nextDest = places.get(i);
                }
            }
            path[j+1] = nextDest;
            places.remove(nextDest);
        }
        path[path.length-1] = hotel;
        return path;
    }

    void optimise(String[] arr, double budget) {
        double totalTime=0;
        double totalPrice=0;

//        for (int i=0;i<arr.length-1;i++){
//            TaxiTime=(db.database.get(arr[i]).get(arr[i+1]))[0];
//            TaxiPrice=(db.database.get(arr[i]).get(arr[i+1]))[1];
//            BusTime=(db.database.get(arr[i]).get(arr[i+1]))[2];
//            BusPrice=(db.database.get(arr[i]).get(arr[i+1]))[3];
//            WalkTime=(db.database.get(arr[i]).get(arr[i+1]))[4];
//        }
        transport = new int[arr.length-1];
        for (int i=0 ; i < transport.length;i++){
            transport[i] = 4;
            totalTime += (db.database.get(arr[i]).get(arr[i+1]))[5];
        }

        if (totalPrice < budget) {
            for (int i = 0; i < arr.length-1; i++) {
                transport[i] = 2;
                totalPrice += (db.database.get(arr[i]).get(arr[i+1]))[2];
                totalTime -= (db.database.get(arr[i]).get(arr[i+1]))[5];
                totalTime += (db.database.get(arr[i]).get(arr[i+1]))[3];
                if (budget < totalPrice) {
                    transport[i] = 4;
                    totalPrice -= (db.database.get(arr[i]).get(arr[i+1]))[2];
                    totalTime += (db.database.get(arr[i]).get(arr[i+1]))[5];
                    totalTime -= (db.database.get(arr[i]).get(arr[i+1]))[3];
                    break;
                }

            }
        }
        if (totalPrice<budget){
            for (int i = 0; i < arr.length-1; i++) {
                transport[i] = 0;
                totalPrice -= (db.database.get(arr[i]).get(arr[i+1]))[2];
                totalPrice += (db.database.get(arr[i]).get(arr[i+1]))[0];
                totalTime -= (db.database.get(arr[i]).get(arr[i+1]))[3];
                totalTime += (db.database.get(arr[i]).get(arr[i+1]))[1];
                if (budget < totalPrice) {
                    transport[i] = 2;
                    totalPrice += (db.database.get(arr[i]).get(arr[i+1]))[2];
                    totalPrice -= (db.database.get(arr[i]).get(arr[i+1]))[0];
                    totalTime += (db.database.get(arr[i]).get(arr[i+1]))[3];
                    totalTime -= (db.database.get(arr[i]).get(arr[i+1]))[1];
                    break;
                }

            }

        }

        System.out.println(Arrays.toString(arr));
        System.out.println(Arrays.toString(transport));
        System.out.println(totalTime);
        System.out.println(totalPrice);
    }

    public ArrayList<ItemRoute> getBestRoute(ArrayList<String> places, double budget, String hotel){
        String[] arr = bestpath(places, hotel);
        optimise(arr, budget);
        return new ArrayList<ItemRoute>();
    }

    public static void main(String[] args){
        GreedyAlgo Galgo = new GreedyAlgo();
        ArrayList<String> a = new ArrayList<>();
        a.add("Wonder Full at Marina Bay Sands");
        a.add("ArtScience Museum");
        a.add("Singapore Zoo");
        a.add("Al-Abrar Mosque");
        a.add("Asian Civilisations Museum");
        a.add("Changi Prison Chapel and Museum");
        a.add("Singapore Flyer");
        a.add("Central Sikh Temple");
        a.add("Chinatown Heritage Centre");
        a.add("East Coast Park");

        String[] path = Galgo.bestpath((ArrayList<String>) a.clone(), "Bukit Timah Nature Reserve");
        Galgo.getBestRoute(a,1, "Bukit Timah Nature Reserve");
    }
}
