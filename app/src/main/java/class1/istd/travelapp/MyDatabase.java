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
import java.io.InputStreamReader;
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
    static final String dbname = "finalDatabase";
    static HashMap<String, HashMap<String,double[]>> database;
    public MyDatabase() {
        try {
            this.database = readFile(dbname);
        } catch (IOException e) {
            this.database = new HashMap<String, HashMap<String,double[]>>();
            e.printStackTrace();
        }
    }
    public MyDatabase(InputStreamReader inputStreamReader) {
        try {
            this.database = readFile2(inputStreamReader);
        } catch (IOException e) {
            this.database = new HashMap<String, HashMap<String,double[]>>();
            e.printStackTrace();
        }
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

        for (String name : names) {
            locations = reader.readLine().split("~");
            cost = new HashMap<String, double[]>();
            for (int j = 0; j < locations.length; j++) {
                cc = new double[6];
                cc[0] = Double.parseDouble(locations[j].split(",")[0]); // taxi price
                cc[1] = Double.parseDouble(locations[j].split(",")[1]); // taxi time
                cc[2] = Double.parseDouble(locations[j].split(",")[2]); // bus price
                cc[3] = Double.parseDouble(locations[j].split(",")[3]); // bus time
                cc[4] = 0; // walk price
                cc[5] = Double.parseDouble(locations[j].split(",")[4]); // walk time
                cost.put(names[j], cc);
            }
            nodes.put(name, cost);
        }
        reader.close();
        return nodes;
    }
    static HashMap<String, HashMap<String,double[]>> readFile2(InputStreamReader inputStreamReader)
            throws IOException{
        HashMap<String, HashMap<String,double[]>> nodes = new HashMap<String, HashMap<String,double[]>>();
        HashMap<String, double[]> cost;

        BufferedReader reader = new BufferedReader(inputStreamReader);

        String line = reader.readLine();

        String[] names = line.split("~");
        String[] locations;
        double[] cc;

        for (String name : names) {
            locations = reader.readLine().split("~");
            cost = new HashMap<String, double[]>();
            for (int j = 0; j < locations.length; j++) {
                cc = new double[6];
                cc[0] = Double.parseDouble(locations[j].split(",")[0]); // taxi price
                cc[1] = Double.parseDouble(locations[j].split(",")[1]); // taxi time
                cc[2] = Double.parseDouble(locations[j].split(",")[2]); // bus price
                cc[3] = Double.parseDouble(locations[j].split(",")[3]); // bus time
                cc[4] = 0; // walk price
                cc[5] = Double.parseDouble(locations[j].split(",")[4]); // walk time
                cost.put(names[j], cc);
            }
            nodes.put(name, cost);
        }
        reader.close();
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

    public static void main(String[] args) {
//        (new MyDatabase()).run();
        printJSON();
    }
}