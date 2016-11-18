package class1.istd.travelapp.Location;

import android.app.Dialog;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

import class1.istd.travelapp.BaseActivity;
import class1.istd.travelapp.R;

/**
 * Created by 1001827 on 14/11/16.
 */


public class MapActivity extends BaseActivity implements OnMapReadyCallback {

    Geocoder geocoder;
    GoogleMap mMap;
    private static final int ERROR_DIALOGUE_REQUEST = 9001;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        //initialize geocoder
        geocoder = new Geocoder(getApplicationContext());

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_menu,menu);
        MenuItem searchViewItem = menu.findItem(R.id.search_bar);
        final SearchView searchViewAndroidActionBar = (SearchView) MenuItemCompat.getActionView(searchViewItem);
        searchViewAndroidActionBar.setOnQueryTextListener(new SearchView.OnQueryTextListener(){
            @Override
            public boolean onQueryTextSubmit(String query){

                try {
                    List<Address> location = geocoder.getFromLocationName(query, 1);
                    double lat = Double.parseDouble(String.valueOf(location.get(0).getLatitude()));
                    double lon = Double.parseDouble(String.valueOf(location.get(0).getLongitude()));

                    LatLng newLocation = new LatLng(lat,lon);

                    mMap.clear();
                    mMap.addMarker(new MarkerOptions().position(newLocation));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(newLocation,15));
                } catch (Exception e){
                    e.printStackTrace();
                }

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText){
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    public boolean servicesOK() {

        int isAvailable = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);

        if (isAvailable == ConnectionResult.SUCCESS){
            return true;
        } else if (GooglePlayServicesUtil.isUserRecoverableError(isAvailable)){
            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(isAvailable, this, ERROR_DIALOGUE_REQUEST);
            dialog.show();
        } else {
            Toast.makeText(this, "Can't connect to mapping service", Toast.LENGTH_SHORT).show();
        }

        return false;
    }

    @Override
    public void onMapReady(GoogleMap map){
        mMap = map;

        LatLng initialLocation = new LatLng(1.366898,103.814047);
        mMap.addMarker(new MarkerOptions().position(initialLocation));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(initialLocation,10));
    }
}
