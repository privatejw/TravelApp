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

        TypedArray item_image = getResources().obtainTypedArray(R.array.item_images);
        String[] item_name = getResources().getStringArray(R.array.item_array);
        String[] item_description = getResources().getStringArray(R.array.item_description);

        for (int i=0; i < item_name.length; i++) {
            ItemAttraction item = new ItemAttraction(item_name[i], item_image.getResourceId(i, -1), item_description[i]);
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
                getResources().getStringArray(R.array.item_array)));
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
        switch (i) {
            case 1:
                Toast.makeText(getApplicationContext(), "Children: Two children", Toast.LENGTH_SHORT).show();
                break;
        }
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
        double budget;
        try {
            budget = Double.parseDouble(txtBudget.getText().toString());
        } catch (NumberFormatException e) {
            budget = 0;
        }
        String hotel = txtHotel.getText().toString();
        ArrayList<String> placesToVisit = new ArrayList<String>();
        String[] locations = getResources().getStringArray(R.array.item_array);

        for (ItemAttraction r: adapter.itemAttractions) {
            if (r.isSelected())
                placesToVisit.add(r.getItem_name());
        }
        if (placesToVisit.isEmpty()) {
            Toast.makeText(getApplicationContext(),
                    "Please select at least 1 location", Toast.LENGTH_SHORT).show();
        } else if (!Arrays.asList(locations).contains(hotel)) {
            Toast.makeText(getApplicationContext(),
                    "Invalid Hotel", Toast.LENGTH_SHORT).show();
        } else {
            planRoute(budget, hotel, placesToVisit);
        }
    }

    public void planRoute(double budget, String hotel, ArrayList<String> placesToVisit) {
        Toast.makeText(getApplicationContext(),
                "Yayyy!", Toast.LENGTH_SHORT).show();

        ArrayList<ItemRoute> itemRoutes = new ArrayList<ItemRoute>();
        String routeInfo = "Yeapp";

        Intent newIntent = new Intent(getApplicationContext(), DisplayRoute.class);
        newIntent.putExtra("itemRoutes", itemRoutes);
        newIntent.putExtra("routeInfo", routeInfo);
        startActivity(newIntent);
    }
}
