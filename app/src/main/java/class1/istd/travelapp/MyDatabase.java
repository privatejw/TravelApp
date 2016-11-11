package class1.istd.travelapp;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;

/**
 * Created by privatejw on 11/11/2016.
 */

public class MyDatabase {
    MyDatabase() {
        HashMap<String, double[]> cost = new HashMap<String, double[]>();
        Node MarinaBaySands = new Node("MarinaBaySands");
        cost.put("SingaporeFlyer", new double[]{3.22,3});
        cost.put("VivoCity", new double[]{6.96,14});
        cost.put("ResortsWorldSentosa", new double[]{8.5,19});
        cost.put("BuddhaToothRelicTemple", new double[]{4.98,8});
        cost.put("Zoo", new double[]{18.4,30});
        MarinaBaySands.setCost(cost);
    }
    HashMap<String, HashMap<String,double[]>> readFile(String filename) throws IOException{
        HashMap<String, HashMap<String,double[]>> nodes = new HashMap<String, HashMap<String,double[]>>();
        HashMap<String, double[]> cost = new HashMap<String, double[]>();

        FileReader f = new FileReader("taxi");
        BufferedReader reader = new BufferedReader(f);

        String line = reader.readLine();

        String[] names = line.split(",");
        String[] locations;
        double[] cc;

        for (int i=0; i<names.length; i++) {
            locations = reader.readLine().split("~");
            for (int j=0; j<locations.length; j++) {
                cc = new double[2];
                cc[0] = Double.parseDouble(locations[j].split(",")[0]);
                cc[1] = Double.parseDouble(locations[j].split(",")[0]);
                cost.put(names[j], cc);
            }
            nodes.put(names[i], (HashMap<String, double[]>) cost.clone());

            cost.clear();
        }
        return nodes;
    }
}
