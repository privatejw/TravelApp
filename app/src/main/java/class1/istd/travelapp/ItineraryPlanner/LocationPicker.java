package class1.istd.travelapp.ItineraryPlanner;

import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import class1.istd.travelapp.R;

public class LocationPicker extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        AdapterView.OnItemClickListener{
    ListView listAttractions;
    AttractionAdapter adapter;
    EditText txtBudget;
    AutoCompleteTextView txtHotel;
    Button btnPlanRoute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_picker);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // row items start
        List<ItemAttraction> itemAttractions = new ArrayList<ItemAttraction>();

        String[] item_name = getResources().getStringArray(R.array.item_attractions);

        for (int i=0; i < item_name.length; i++) {
            ItemAttraction item = new ItemAttraction(item_name[i], "Stars go here");
            itemAttractions.add(item);
        }

        listAttractions = (ListView) findViewById(R.id.listAttractions);
        adapter = new AttractionAdapter(this, itemAttractions);
        listAttractions.setAdapter(adapter);

        listAttractions.setOnItemClickListener(this);
        // row items end

        // drawer start
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        // drawer end

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
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        String locationClicked = ((ItemAttraction) adapter.getItem(i)).getItem_name();
        Toast.makeText(getApplicationContext(), locationClicked, Toast.LENGTH_SHORT).show();
        //TODO: put extra and add new intent to review screen
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.location_picker, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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
                "Yayyy!", Toast.LENGTH_SHORT).show();

        ArrayList<ItemRoute> itemRoutes = new ArrayList<ItemRoute>();
        itemRoutes.add(new ItemRoute("Marina Bay", R.drawable.bus_icon, "Bus: $4\nTime: 1 mins"));
        itemRoutes.add(new ItemRoute("HOs", 0, ""));
        itemRoutes.add(new ItemRoute("Marina Bay", R.drawable.walk_icon, "Bus: $4\nTime: 2 mins"));
        itemRoutes.add(new ItemRoute("Marina Bay", R.drawable.car_icon, "Bus: $4\nTime: 3 mins"));
        itemRoutes.add(new ItemRoute("Marina Bay", R.drawable.bus_icon, "Bus: $4\nTime: 4 mins"));
        itemRoutes.add(new ItemRoute("Marina Bay", R.drawable.walk_icon, "Bus: $4\nTime: 5 mins"));
        itemRoutes.add(new ItemRoute("Marina Bay", R.drawable.car_icon, "Bus: $4\nTime: 6 mins"));
        itemRoutes.add(new ItemRoute("HOTEL", 0, ""));
        itemRoutes.add(new ItemRoute("Marina Bay", R.drawable.bus_icon, "Bus: $4\nTime: 7 mins"));
        itemRoutes.add(new ItemRoute("HOTEL", 0, ""));
        overallRouteInfo[0] = "Yeapp";

        return itemRoutes;
    }
}
