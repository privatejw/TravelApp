package class1.istd.travelapp;

import java.util.ArrayList;
import java.util.Arrays;

import class1.istd.travelapp.ItineraryPlanner.ItemRoute;

public class Algo {
    int bestTime;
    String[] bestRoute;
    String[] thisRoute;
    int thisWalkTime;
    int thisBusTime;
    int thisTaxiTime;
    double thisBusPrice;
    double thisTaxiPrice;
    int bestWalkTime;
    int bestBusTime;
    int bestTaxiTime;
    double bestBusPrice;
    double bestTaxiPrice;
    String hotel;
    int currentTime;
    double currentPrice;
    int[] transport;
    MyDatabase db;
//    public static void permute(int[] arr){
//        permuteHelper(arr, 0);
//    }
//
//    private static void permuteHelper(int[] arr, int index){
//        if(index >= arr.length - 1){ //If we are at the last element - nothing left to permute
//            //System.out.println(Arrays.toString(arr));
//            //Print the array
//            System.out.print("[");
//            for(int i = 0; i < arr.length - 1; i++){
//                System.out.print(arr[i] + ", ");
//            }
//            if(arr.length > 0)
//                System.out.print(arr[arr.length - 1]);
//            System.out.println("]");
//            return;
//        }
//
//        for(int i = index; i < arr.length; i++){ //For each index in the sub array arr[index...end]
//
//            //Swap the elements at indices index and i
//            int t = arr[index];
//            arr[index] = arr[i];
//            arr[i] = t;
//
//            //Recurse on the sub array arr[index+1...end]
//            permuteHelper(arr, index+1);
//
//            //Swap the elements back
//            t = arr[index];
//            arr[index] = arr[i];
//            arr[i] = t;
//        }
//    }


    public void permute(int[] arr,String[]location){
        permuteHelper(arr, 0,location);
    }

    private void permuteHelper(int[] arr, int index,String[]location){

        int thisTime;

        if(index >= arr.length - 1){ //If we are at the last element - nothing left to permute
            //System.out.println(Arrays.toString(arr));
            //Print the array

//            System.out.print("[");
//            for(int i = 0; i < arr.length - 1; i++){
//                System.out.print(arr[i] + ", ");
//            }
//            if(arr.length > 0)
//                System.out.print(arr[arr.length - 1]);
//            System.out.println("]");

            //System.out.println(Arrays.toString(indexToString(arr,location)));
            thisTime=getPriceAndTime(  addHotel(indexToString(arr,location))  );
            if (thisTime<bestTime){
                bestTime=thisTime;
                bestRoute=thisRoute;
                bestWalkTime=thisWalkTime;
                bestBusTime=thisBusTime;
                bestTaxiTime=thisTaxiTime;
                bestBusPrice=thisBusPrice;
                bestTaxiPrice=thisTaxiPrice;
            }

            return;
        }

        for(int i = index; i < arr.length; i++){ //For each index in the sub array arr[index...end]

            //Swap the elements at indices index and i
            int t = arr[index];
            arr[index] = arr[i];
            arr[i] = t;

            //Recurse on the sub array arr[index+1...end]
            permuteHelper(arr, index+1,location);

            //Swap the elements back
            t = arr[index];
            arr[index] = arr[i];
            arr[i] = t;
        }
    }

    public String[] indexToString(int[] ind,String[] arr) {
        String[]out=new String[arr.length];
        for(int i=0;i<arr.length;i++){
            out[i]=arr[ind[i]-1];
        }
        return out;
    }

    public int getPriceAndTime(String[] path){
        thisRoute=path;
        int totalTaxiTime=0;
        double totalTaxiPrice=0;
        int totalBusTime=0;
        double totalBusPrice=0;
        int totalWalkTime=0;
        int totalWalkPrice=0;
        for (int i=0;i<=path.length-2;i++){
            totalTaxiTime+=(db.database.get(path[i]).get(path[i+1]))[1];
            totalTaxiPrice+=(db.database.get(path[i]).get(path[i+1]))[0];
            totalBusTime+=(db.database.get(path[i]).get(path[i+1]))[3];
            totalBusPrice+=(db.database.get(path[i]).get(path[i+1]))[2];
            totalWalkTime+=(db.database.get(path[i]).get(path[i+1]))[5];

        }
        thisWalkTime=totalWalkTime;
        thisBusTime=totalBusTime;
        thisTaxiTime=totalTaxiTime;
        thisBusPrice=totalBusPrice;
        thisTaxiPrice=totalTaxiPrice;

//        System.out.println("route: "+Arrays.toString(path)+"\ntime: "+totalBusTime+
//                "\nprice: "+totalBusPrice+"\nwalktime: "+totalWalkTime);
        return totalBusTime;
    }

    public int getPriceAndTime(String[] path, float budget){
        thisRoute=path;
        int totalTaxiTime=0;
        double totalTaxiPrice=0;
        int totalBusTime=0;
        double totalBusPrice=0;
        int totalWalkTime=0;
        int totalWalkPrice=0;
        for (int i=0;i<=path.length-2;i++){
            totalTaxiTime+=(db.database.get(path[i]).get(path[i+1]))[0];
            totalTaxiPrice+=(db.database.get(path[i]).get(path[i+1]))[1];
            totalBusTime+=(db.database.get(path[i]).get(path[i+1]))[3];
            totalBusPrice+=(db.database.get(path[i]).get(path[i+1]))[2];
            totalWalkTime+=(db.database.get(path[i]).get(path[i+1]))[5];

        }
//        System.out.println("route: "+Arrays.toString(path)+"\ntime: "+totalBusTime+
//                "\nprice: "+totalBusPrice+"\nwalktime: "+totalWalkTime);



        return totalBusTime;
    }

    public void checkBudget(int budget){
        if (budget>=bestTaxiPrice){
            System.out.println("taxi");
            System.out.println("Price:"+bestTaxiPrice);
            System.out.println("Time:"+bestTaxiTime);
        } else if(budget>=bestBusPrice){
            System.out.println("bus");
            System.out.println("Price:"+bestBusPrice);
            System.out.println("Time:"+bestBusTime);
        }else{
            System.out.println("walk");
            System.out.println("Time:"+bestWalkTime);
        }
        System.out.println(Arrays.toString(bestRoute));
    }

    public void detailBudget(double budget){
        currentTime=bestTaxiTime;
        currentPrice=bestTaxiPrice;
        transport= new int[bestRoute.length-1];

        for (int i=0;i<transport.length;i++)transport[i]=0;
        if (budget>=currentPrice){
            //System.out.println("taxi all the way");
            //System.out.println("Price:"+bestTaxiPrice);
            //System.out.println("Time:"+bestTaxiTime);
        } else if(budget<currentPrice){
           for (int i=0;i<transport.length;i++) {
               transport[i]=2;
               currentTime-=(db.database.get(bestRoute[i]).get(bestRoute[i+1]))[1];
               currentTime+=(db.database.get(bestRoute[i]).get(bestRoute[i+1]))[3];
               currentPrice-=(db.database.get(bestRoute[i]).get(bestRoute[i+1]))[0];
               currentPrice+=(db.database.get(bestRoute[i]).get(bestRoute[i+1]))[2];
               if (budget>=currentPrice)break;
           }
            //System.out.println(currentPrice);
        }
        if(budget<currentPrice){
            for (int i=0;i<transport.length;i++) {
                transport[i]=4;
                currentTime-=(db.database.get(bestRoute[i]).get(bestRoute[i+1]))[3];
                currentTime+=(db.database.get(bestRoute[i]).get(bestRoute[i+1]))[5];
                currentPrice-=(db.database.get(bestRoute[i]).get(bestRoute[i+1]))[2];
                //currentPrice+=(db.database.get(bestRoute[i]).get(bestRoute[i+1]))[4]; is zero
                if (budget>=currentPrice)break;
            }
        }
    }

    public String[] addHotel(String[] arr){
        String[] out=new String[arr.length+2];
        out[0]=hotel;
        for (int i=0;i<arr.length;i++){
            out[i+1]=arr[i];
        }

        out[arr.length+1]=hotel;

        return out;
    }

    public Algo() {
        this.db = new MyDatabase();
    }

    public Algo(MyDatabase db) {
        this.db = db;
    }

    public ArrayList<ItemRoute> getBestPath(String[] arr, double budget, String hotel,
                                            String[] overallRouteInfo) {
        this.bestTime = 100000;
        this.hotel= hotel;
        int[] dily = new int[arr.length];
        for (int i=1; i<=arr.length;i++){
            dily[i-1]=i;
        }
        permute(dily,arr);
        detailBudget(budget);

        overallRouteInfo[0] = String.format("Total travel cost: $%.2f\nTotal travel time: %d minutes",
                this.currentPrice, this.currentTime);

        ArrayList<ItemRoute> itemRoutes = new ArrayList<ItemRoute>();
        String source, dest, routeInfo, pathType;
        int tMode, picture;

        for (int i = 0; i < this.transport.length; i++) {
            source = this.bestRoute[i];
            dest = this.bestRoute[i+1];
            tMode = this.transport[i];
            switch (tMode) {
                case 0: picture = R.drawable.car_icon; pathType = "Taxi"; break;
                case 2: picture = R.drawable.bus_icon; pathType = "Bus"; break;
                case 4: picture = R.drawable.walk_icon; pathType = "Walking"; break;
                default: picture = R.drawable.walk_icon; pathType = "Walking";
            }
            routeInfo = String.format("%s cost: $%.2f\nTravel time: %.0f minutes", pathType,
                    db.database.get(source).get(dest)[tMode],
                    db.database.get(source).get(dest)[tMode+1]);
            itemRoutes.add(new ItemRoute(source, picture, routeInfo));
        }
        itemRoutes.add(new ItemRoute("Hotel: "+this.bestRoute[0], 0, ""));

        return itemRoutes;
    }

    public static void main(String[] args) {
        Algo algo = new Algo();
        algo.getBestPath(new String[]{"Wonder Full at Marina Bay Sands", "ArtScience Museum",
                "Singapore Zoo", "Singapore Flyer"}, 0.6, "Bukit Timah Nature Reserve", new String[1]);


        System.out.println(Arrays.toString(algo.transport));
        System.out.println(Arrays.toString(algo.bestRoute));
        System.out.println("Price: $"+Math.round(algo.currentPrice * 100.0) / 100.0);
        System.out.println("Time: "+algo.currentTime+ " minutes");
    }
}