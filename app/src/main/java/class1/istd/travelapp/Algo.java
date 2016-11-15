package com.example.yingjie.travel;

import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by YingJie on 14/11/2016.
 */

public class Algo {
    static int bestTime=100000;
    static String[] bestRoute;
    static String[] thisRoute;
    static int thisWalkTime;
    static int thisBusTime;
    static int thisTaxiTime;
    static double thisBusPrice;
    static double thisTaxiPrice;
    static int bestWalkTime;
    static int bestBusTime;
    static int bestTaxiTime;
    static double bestBusPrice;
    static double bestTaxiPrice;

    public static void anagram(String prefix, String suffix) {
        String newPrefix, newSuffix;
        int numOfChars = suffix.length();
        if (numOfChars == 1) {
            //End case: print out one anagram
            System.out.println(prefix + suffix);
        } else {
            for (int i = 1; i <= numOfChars; i++) {
                newSuffix = suffix.substring(1, numOfChars);
                newPrefix = prefix + suffix.charAt(0);
                anagram(newPrefix, newSuffix);
                //recursive call//rotate left to create a rearranged suffix
                suffix = newSuffix + suffix.charAt(0);
            }
        }
    }


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


    public static void permute(int[] arr,String[]location){
        permuteHelper(arr, 0,location);
    }

    private static void permuteHelper(int[] arr, int index,String[]location){

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
            thisTime=getPriceAndTime(indexToString(arr,location));
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

    public static String[] indexToString(int[] ind,String[] arr) {
        String[]out=new String[arr.length];
        for(int i=0;i<arr.length;i++){
            out[i]=arr[ind[i]-1];
        }
        return out;
    }

    public static int getPriceAndTime(String[] path){
        thisRoute=path;
        int totalTaxiTime=0;
        double totalTaxiPrice=0;
        int totalBusTime=0;
        double totalBusPrice=0;
        int totalWalkTime=0;
        int totalWalkPrice=0;
        for (int i=0;i<=path.length-2;i++){
            totalTaxiTime+=(MyDatabase.database.get(path[i]).get(path[i+1]))[1];
            totalTaxiPrice+=(MyDatabase.database.get(path[i]).get(path[i+1]))[0];
            totalBusTime+=(MyDatabase.database.get(path[i]).get(path[i+1]))[3];
            totalBusPrice+=(MyDatabase.database.get(path[i]).get(path[i+1]))[2];
            totalWalkTime+=(MyDatabase.database.get(path[i]).get(path[i+1]))[5];

        }
        thisWalkTime=totalWalkTime;
        thisBusTime=totalBusTime;
        thisTaxiTime=totalTaxiTime;
        thisBusPrice=totalBusPrice;
        thisTaxiPrice=totalTaxiPrice;

        System.out.println("route: "+Arrays.toString(path)+"\ntime: "+totalBusTime+
                "\nprice: "+totalBusPrice+"\nwalktime: "+totalWalkTime);
        return totalBusTime;
    }

    public static int getPriceAndTime(String[] path, float budget){
        thisRoute=path;
        int totalTaxiTime=0;
        double totalTaxiPrice=0;
        int totalBusTime=0;
        double totalBusPrice=0;
        int totalWalkTime=0;
        int totalWalkPrice=0;
        for (int i=0;i<=path.length-2;i++){
            totalTaxiTime+=(MyDatabase.database.get(path[i]).get(path[i+1]))[0];
            totalTaxiPrice+=(MyDatabase.database.get(path[i]).get(path[i+1]))[1];
            totalBusTime+=(MyDatabase.database.get(path[i]).get(path[i+1]))[3];
            totalBusPrice+=(MyDatabase.database.get(path[i]).get(path[i+1]))[2];
            totalWalkTime+=(MyDatabase.database.get(path[i]).get(path[i+1]))[5];

        }
//        System.out.println("route: "+Arrays.toString(path)+"\ntime: "+totalBusTime+
//                "\nprice: "+totalBusPrice+"\nwalktime: "+totalWalkTime);



        return totalBusTime;
    }



    public static void checkBudget(int budget){
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

    public static void detailBudget(double budget){
        int currentTime=bestTaxiTime;
        double currentPrice=bestTaxiPrice;
        String[] transport= new String[bestRoute.length-1];

        for (int i=0;i<transport.length;i++)transport[i]="taxi";
        if (budget>=currentPrice){
            //System.out.println("taxi all the way");
            //System.out.println("Price:"+bestTaxiPrice);
            //System.out.println("Time:"+bestTaxiTime);
        } else if(budget<currentPrice){
           for (int i=0;i<transport.length;i++) {
               transport[i]="bus";
               currentTime-=(MyDatabase.database.get(bestRoute[i]).get(bestRoute[i+1]))[1];
               currentTime+=(MyDatabase.database.get(bestRoute[i]).get(bestRoute[i+1]))[3];
               currentPrice-=(MyDatabase.database.get(bestRoute[i]).get(bestRoute[i+1]))[0];
               currentPrice+=(MyDatabase.database.get(bestRoute[i]).get(bestRoute[i+1]))[2];
               if (budget>=currentPrice)break;
           }
            //System.out.println(currentPrice);
        }
        if(budget<currentPrice){
            for (int i=0;i<transport.length;i++) {
                transport[i]="walk";
                currentTime-=(MyDatabase.database.get(bestRoute[i]).get(bestRoute[i+1]))[3];
                currentTime+=(MyDatabase.database.get(bestRoute[i]).get(bestRoute[i+1]))[5];
                currentPrice-=(MyDatabase.database.get(bestRoute[i]).get(bestRoute[i+1]))[2];
                //currentPrice+=(MyDatabase.database.get(bestRoute[i]).get(bestRoute[i+1]))[4]; is zero
                if (budget>=currentPrice)break;
            }
        }

        System.out.println(Arrays.toString(transport));
        System.out.println(Arrays.toString(bestRoute));
        System.out.println("Price: $"+Math.round(currentPrice * 100.0) / 100.0);
        System.out.println("Time: "+currentTime+ " minutes");
    }



    public static void main(String[] args){
        (new MyDatabase()).run();
        //System.out.println(  Arrays.toString((MyDatabase.database.get("Marina Bay Sands").get("Vivocity")))  );
        //anagram("","cde");
        String[] arr={"Marina Bay Sands","Vivocity","Zoo","Singapore Flyer"};  //TODO: user's inputs from app.
        //permute(new int[]{1,2,3});
        permute(new int[]{1,2,3,4},arr);
        //getPriceAndTime(arr);
        //System.out.println("Best Time: "+bestTime);
        //System.out.println("Best Route: "+Arrays.toString(bestRoute) );
        detailBudget(0.3);
    }
}



