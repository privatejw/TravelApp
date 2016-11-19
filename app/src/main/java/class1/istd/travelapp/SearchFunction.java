package class1.istd.travelapp;

import java.util.ArrayList;
import java.util.Arrays;

public class SearchFunction {
    private String[] places;

    public SearchFunction(String[] attractions) {
        this.places = attractions;
    }

    public String search(String name) {
        String[] nameSplit = name.split("\\s+");
        ArrayList<String> nameSplitv2 = new ArrayList<String>(Arrays.asList(nameSplit));

        ArrayList<Integer> errorList = new ArrayList<Integer>();
        int counter = 0;
        for (String s: places) {
            errorList.add(0);
            String[] placeSplit = s.split("\\s+");
            ArrayList<String> placeSplitv2 = new ArrayList<String>(Arrays.asList(placeSplit));
            for (String s1 : nameSplitv2) {
                int checkEachWord = 100000;
                for (String placev1 : placeSplitv2) {
                    if (editDistance(s1.toLowerCase(), placev1.toLowerCase()) < checkEachWord) {
                        checkEachWord = editDistance(s1.toLowerCase(), placev1.toLowerCase());
                    }
                }
                errorList.set(counter, errorList.get(counter) + checkEachWord);
            }
            counter += 1;
        }
        int smallest = 0;
        int smallestIndex = 0;
        ArrayList<String> locations = new ArrayList<String>();
        for (int i = 0; i < places.length; i++) {
            if (i == 0) {
                smallest = errorList.get(0);
                smallestIndex = i;
                locations.add(places[smallestIndex]);
            }
            else {
                if (errorList.get(i) < smallest) {
                    locations = new ArrayList<String>();
                    smallest = errorList.get(i);
                    smallestIndex = i;
                    locations.add(places[smallestIndex]);
                }
                else if(errorList.get(i) == smallest){
                    smallest = errorList.get(i);
                    smallestIndex = i;
                    locations.add(places[smallestIndex]);
                }
            }
        }
        return locations.get(0);
    }

    private static int editDistance(String s, String t) {
        int m = s.length();
        int n = t.length();
        int[][] d = new int[m + 1][n + 1];
        for (int i = 0; i <= m; i++) {
            d[i][0] = i;
        }
        for (int j = 0; j <= n; j++) {
            d[0][j] = j;
        }
        for (int j = 1; j <= n; j++) {
            for (int i = 1; i <= m; i++) {
                if (s.charAt(i - 1) == t.charAt(j - 1)) {
                    d[i][j] = d[i - 1][j - 1];
                } else {
                    d[i][j] = min((d[i - 1][j] + 1), (d[i][j - 1] + 1), (d[i - 1][j - 1] + 1));
                }
            }
        }
        return (d[m][n]);
    }

    private static int min(int a, int b, int c) {
        return (Math.min(Math.min(a, b), c));
    }

    public static void main(String[] args) {
//        SearchFunction searchFunction = new SearchFunction(new String[]{"Sentosa", "Vivocity", "Singapore Flyer"});
//        System.out.println(searchFunction.search("sentossa"));

    }
}
