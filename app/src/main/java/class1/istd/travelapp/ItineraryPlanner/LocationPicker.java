package class1.istd.travelapp.ItineraryPlanner;

import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import class1.istd.travelapp.Algo;
import class1.istd.travelapp.BaseActivity;
import class1.istd.travelapp.MyDatabase;
import class1.istd.travelapp.R;

public class LocationPicker extends BaseActivity implements AdapterView.OnItemClickListener{
    ListView listAttractions;
    AttractionAdapter adapter;
    EditText txtBudget;
    AutoCompleteTextView txtHotel;
    Button btnPlanRoute;
    Algo algo;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_picker);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setLogo(R.drawable.ic_menu_send);
            getSupportActionBar().setDisplayUseLogoEnabled(true);
        }

        // row items start
        List<ItemAttraction> itemAttractions = new ArrayList<ItemAttraction>();

        String[] attraction = getResources().getStringArray(R.array.item_attractions);

        for (String anItem_name : attraction) {
            itemAttractions.add(new ItemAttraction(anItem_name, "Stars go here"));
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
        } catch (IOException e) {
            Log.e("failed to load database", ": nooo");
        }
        // initialize database end
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        String locationClicked = ((ItemAttraction) adapter.getItem(i)).getItem_name();
        Toast.makeText(getApplicationContext(), locationClicked, Toast.LENGTH_SHORT).show();
        //TODO: put extra and add new intent to review screen
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
        } else if (!Arrays.asList(locations).contains(hotel)) {
            Toast.makeText(getApplicationContext(), "Invalid Hotel", Toast.LENGTH_SHORT).show();
        } else {
            // get the item routes
            String[] overallRouteInfo = new String[1];
            ArrayList<ItemRoute> itemRoutes = planRoute(budget, hotel, placesToVisit, overallRouteInfo);

            // add the route information and start the new activity
            Intent newIntent = new Intent(getApplicationContext(), DisplayRoute.class);
            newIntent.putExtra("itemRoutes", itemRoutes);
            newIntent.putExtra("overallRouteInfo", overallRouteInfo[0]);
            startActivity(newIntent);
        }
    }

    public ArrayList<ItemRoute> planRoute(double budget, String hotel,
                ArrayList<String> placesToVisit, String[] overallRouteInfo) {
        Toast.makeText(getApplicationContext(),
                "Path found!", Toast.LENGTH_SHORT).show();

        ArrayList<ItemRoute> itemRoutes = algo.getBestPath(
                placesToVisit.toArray(new String[placesToVisit.size()]), budget, hotel, overallRouteInfo);

        return itemRoutes;
    }
}
