package class1.istd.travelapp.ItineraryPlanner;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import class1.istd.travelapp.Algo;
import class1.istd.travelapp.BaseActivity;
import class1.istd.travelapp.SearchFunction;
import class1.istd.travelapp.MyDatabase;
import class1.istd.travelapp.R;
import class1.istd.travelapp.UserReviews.LocationReviews;

public class LocationPicker extends BaseActivity implements AdapterView.OnItemClickListener{
    ListView listAttractions;
    AttractionAdapter adapter;
    EditText txtBudget;
    AutoCompleteTextView txtHotel;
    Button btnPlanRoute;
    Algo algo;
    SearchFunction searchFunction;
    List<ItemAttraction> itemAttractions;
    HashMap<String, Integer> attractionlist;

    private FirebaseDatabase myDB;  //firebase objects
    private FirebaseStorage myStorage;
    private HashMap<String, HashMap> metaData;
    private ValueEventListener metaListen;
    private DatabaseReference TotalRevRef;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_picker);

        // row items start
//        List<ItemAttraction> itemAttractions = new ArrayList<ItemAttraction>();
        itemAttractions = new ArrayList<ItemAttraction>();
        attractionlist = new HashMap<>();
        String[] attraction = getResources().getStringArray(R.array.item_attractions);

        int IndexCounter = 0;

        for (String anItem_name : attraction) {
            itemAttractions.add(new ItemAttraction(anItem_name, 0));
            attractionlist.put(anItem_name, IndexCounter);
            IndexCounter++;
        }

        listAttractions = (ListView) findViewById(R.id.listAttractions);
        adapter = new AttractionAdapter(this, itemAttractions);
        listAttractions.setAdapter(adapter);

        listAttractions.setOnItemClickListener(this);
        // row items end

        // other ui elements start
        txtBudget = (EditText) findViewById(R.id.txtBudget);
        txtHotel = (AutoCompleteTextView) findViewById(R.id.txtHotel);
        btnPlanRoute = (Button) findViewById(R.id.btnPlanRoute);

        txtHotel.setThreshold(1);
        txtHotel.setAdapter(new ArrayAdapter<>(getBaseContext(),
                android.R.layout.simple_spinner_dropdown_item,
                getResources().getStringArray(R.array.item_attractions)));
        btnPlanRoute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnPlanRouteClicked(view);
            }
        });
        // other ui elements end

        // initialize database
        try {
            MyDatabase db = new MyDatabase(new InputStreamReader(getAssets().open("finalDatabase")));
            algo = new Algo(db);
            searchFunction = new SearchFunction(getResources().getStringArray(R.array.item_attractions));
        } catch (IOException e) {
            Log.e("failed to load database", ": nooo");
        }
        // initialize database end

        myDB = FirebaseDatabase.getInstance();
        myStorage = FirebaseStorage.getInstance();
        TotalRevRef = myDB.getReference("metaData");
        TotalRevRef.addValueEventListener(metaListen = new ValueEventListener() { //start listening to rating values
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                metaData = (HashMap<String, HashMap>) dataSnapshot.getValue();
                for(HashMap.Entry locMeta: metaData.entrySet()) {
                    try {
                        int i = attractionlist.get((String) locMeta.getKey());

                        try {
                            float a = ((Double) ((HashMap) locMeta.getValue()).get("currentRating")).floatValue();
                            itemAttractions.get(i).setRating(a);
                        } catch (Exception e) {
                            float a = ((Long) ((HashMap) locMeta.getValue()).get("currentRating")).floatValue();
                            itemAttractions.get(i).setRating(a);
                            adapter.notifyDataSetChanged();
                        }
                    } catch (Exception i) {
                        Log.e("Failed to fetch entry", ": no such place in database");
                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        String locationClicked = ((ItemAttraction) adapter.getItem(i)).getItem_name();
        Toast.makeText(getApplicationContext(), locationClicked, Toast.LENGTH_SHORT).show();
        goToReviewScreen(locationClicked);
    }

    public void goToReviewScreen(String location) {
        Intent thisGo = new Intent(this, LocationReviews.class);
        thisGo.putExtra("location", location);
        startActivity(thisGo);
    }

    public void btnPlanRouteClicked(View view) {
        // Retrieve budget and starting hotel from user
        double budget;
        try {
            budget = Double.parseDouble(txtBudget.getText().toString());
        } catch (NumberFormatException e) {
            budget = 0;
        }
        String hotel = txtHotel.getText().toString();

        //  get places to visit and ensure that the hotel name is valid
        ArrayList<String> placesToVisit = new ArrayList<String>();
        String[] locations = getResources().getStringArray(R.array.item_attractions);

        for (ItemAttraction attraction: adapter.itemAttractions) {
            if (attraction.isSelected())
                placesToVisit.add(attraction.getItem_name());
        }
        if (placesToVisit.isEmpty()) {
            Toast.makeText(getApplicationContext(),
                    "Please select at least 1 location", Toast.LENGTH_SHORT).show();
        } else {
            // implement robust checking
            if (!Arrays.asList(locations).contains(hotel)) {
                hotel = searchFunction.search(hotel);
                Toast.makeText(getApplicationContext(), "Hotel changed to: "+hotel, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), "Path found!", Toast.LENGTH_SHORT).show();
            }
            // get the item routes
            String[] overallRouteInfo = new String[1];
            ArrayList<ItemRoute> itemRoutes = algo.getBestPath(placesToVisit.toArray(
                    new String[placesToVisit.size()]), budget, hotel, overallRouteInfo);

            // add the route information and start the new activity
            Intent newIntent = new Intent(getApplicationContext(), DisplayRoute.class);
            newIntent.putExtra("itemRoutes", itemRoutes);
            newIntent.putExtra("overallRouteInfo", overallRouteInfo[0]);
            startActivity(newIntent);
        }
    }

    public ArrayList<ItemRoute> planRoute(double budget, String hotel,
                ArrayList<String> placesToVisit, String[] overallRouteInfo) {
        ArrayList<ItemRoute> itemRoutes = algo.getBestPath(
                placesToVisit.toArray(new String[placesToVisit.size()]), budget, hotel, overallRouteInfo);

        return itemRoutes;
    }
}
