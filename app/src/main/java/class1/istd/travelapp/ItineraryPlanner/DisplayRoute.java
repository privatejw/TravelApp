package class1.istd.travelapp.ItineraryPlanner;

import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import class1.istd.travelapp.Location.ExtraMapActivity;
import class1.istd.travelapp.R;

public class DisplayRoute extends AppCompatActivity implements AdapterView.OnItemClickListener{
    ListView listRoutes;
    RouteAdapter adapter;
    Geocoder geocoder;
    String locationClicked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_route);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        // initialize geocoder
        geocoder = new Geocoder(getApplicationContext());

        // row items start
        List<ItemRoute> itemRoutes = (ArrayList<ItemRoute>) getIntent().getExtras().get("itemRoutes");

        listRoutes = (ListView) findViewById(R.id.listRoutes);
        adapter = new RouteAdapter(this, itemRoutes);
        listRoutes.setAdapter(adapter);
        listRoutes.setOnItemClickListener(this);
        // row items end

        // other ui items
        String overallRouteInfo = getIntent().getExtras().getString("overallRouteInfo");

        TextView txtRouteInfo = (TextView) findViewById(R.id.txtRouteInfo);
        txtRouteInfo.setText(overallRouteInfo);
        // other ui items end
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        locationClicked = ((ItemRoute) adapter.getItem(i)).getLocation();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Do you wish to view the location on the map?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        showMapsExtra();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });
        AlertDialog d = builder.create();
        d.show();
    }

    void showMapsExtra() {
        try {
            List<Address> matchedList = geocoder.getFromLocationName(locationClicked, 1);

            double lat = Double.parseDouble(String.valueOf(matchedList.get(0).getLatitude()));
            double lon = Double.parseDouble(String.valueOf(matchedList.get(0).getLongitude()));

            Intent intent = new Intent(getApplicationContext(), ExtraMapActivity.class);
            intent.putExtra("lat", lat);
            intent.putExtra("lon", lon);
            intent.putExtra("title", locationClicked);
            startActivity(intent);
        } catch (IOException e) {
            Toast.makeText(this, "Not able to find location: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        } catch (IllegalArgumentException e) {
            Toast.makeText(this, "Invalid location: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        } catch (ArrayIndexOutOfBoundsException e) {
            Toast.makeText(this, "Not found location: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(this, "Problem retrieving location: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}
