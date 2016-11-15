package class1.istd.travelapp;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Scanner;
import org.json.*;

public class MyDatabase {
    static final String dbname = "newDatabase2";
    HashMap<String, HashMap<String,double[]>> database;
    MyDatabase() {
        try {
            this.database = readFile(dbname);
        } catch (IOException e) {
            this.database = new HashMap<String, HashMap<String,double[]>>();
            e.printStackTrace();
        }
    }
    MyDatabase(int i) {
        HashMap<String, double[]> cost = new HashMap<String, double[]>();
        Node MarinaBaySands = new Node("MarinaBaySands");
        cost.put("SingaporeFlyer", new double[]{3.22,3});
        cost.put("VivoCity", new double[]{6.96,14});
        cost.put("ResortsWorldSentosa", new double[]{8.5,19});
        cost.put("BuddhaToothRelicTemple", new double[]{4.98,8});
        cost.put("Zoo", new double[]{18.4,30});
        MarinaBaySands.setCost(cost);
    }
    static HashMap<String, HashMap<String,double[]>> readFile(String filename) throws IOException{
        HashMap<String, HashMap<String,double[]>> nodes = new HashMap<String, HashMap<String,double[]>>();
        HashMap<String, double[]> cost;

        FileReader f = new FileReader(filename);
        BufferedReader reader = new BufferedReader(f);

        String line = reader.readLine();

        String[] names = line.split("~");
        String[] locations;
        double[] cc;

        for (int i=0; i<names.length; i++) {
            locations = reader.readLine().split("~");
            cost = new HashMap<String, double[]>();
            for (int j=0; j<locations.length; j++) {
                cc = new double[6];
                cc[0] = Double.parseDouble(locations[j].split(",")[0]); // taxi price
                cc[1] = Double.parseDouble(locations[j].split(",")[1]); // taxi time
                cc[2] = Double.parseDouble(locations[j].split(",")[2]); // bus price
                cc[3] = Double.parseDouble(locations[j].split(",")[3]); // bus time
                cc[4] = 0; // walk price
                cc[5] = Double.parseDouble(locations[j].split(",")[4]); // walk time
                cost.put(names[j], cc);
            }
            nodes.put(names[i], cost);
        }
        return nodes;
    }
    static void printJSON() {
        try {
            HashMap<String, HashMap<String, double[]>> n = MyDatabase.readFile(dbname);
            for (String s: n.keySet()) {
                System.out.println("\""+s+"\": {");
                for (String d: n.get(s).keySet()) {
                    System.out.print("\t\""+d+"\": ");
                    System.out.print(""+Arrays.toString(n.get(s).get(d))+",\n");
                }
                System.out.println("},");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    void readJSON() {
        // doesn't work exactly as intended
        try {
            JSONObject obj = new JSONObject("database.json");
            JSONArray pageName = obj.getJSONObject("Zoo").getJSONArray("Zoo");

            System.out.println(pageName.toString());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void run() {}

    public static void main(String[] args) {
//        (new MyDatabase()).run();
        printJSON();
    }
}