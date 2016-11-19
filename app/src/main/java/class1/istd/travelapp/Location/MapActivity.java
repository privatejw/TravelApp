package class1.istd.travelapp.Location;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
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
import class1.istd.travelapp.MyDatabase;
import class1.istd.travelapp.R;
import class1.istd.travelapp.SearchFunction;

/**
 * Created by 1001827 on 14/11/16.
 */


public class MapActivity extends BaseActivity implements OnMapReadyCallback {

    Geocoder geocoder;
    GoogleMap mMap;
    private static final int ERROR_DIALOGUE_REQUEST = 9001;
    private static final CharSequence[] MAP_TYPE_ITEMS = {"Road Map", "Hybrid", "Satellite", "Terrain"};


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
                    SearchFunction searchFunction = new SearchFunction(getResources().getStringArray(R.array.item_attractions));
                    String returnLocation = searchFunction.search(query);
                    
                    if (!returnLocation.contains("Singapore")){
                        returnLocation = "Singapore " + returnLocation;
                    }

                    List<Address> location = geocoder.getFromLocationName(returnLocation, 1);
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        super.onOptionsItemSelected(item);
        int id = item.getItemId();

        switch (id){
            case R.id.changeMapType:
                showMapTypeSelectorDialog();
                break;
        }
        return true;
    }

    private void showMapTypeSelectorDialog(){
        //setting up the builder
        final String title = "Select Map Type";
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);

        //check current map type
        int checkCurrentMapType = mMap.getMapType() - 1;

        //add click listener to dialog
        builder.setSingleChoiceItems(MAP_TYPE_ITEMS,checkCurrentMapType,new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item){
                switch(item){
                    case 1:
                        mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                        break;
                    case 2:
                        mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                        break;
                    case 3:
                        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                        break;
                    default:
                        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                }
                dialog.dismiss();
            }
        });

        //build dialog
        AlertDialog fMapTypeDialogue = builder.create();
        fMapTypeDialogue.setCanceledOnTouchOutside(true);
        fMapTypeDialogue.show();
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
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        LatLng initialLocation = new LatLng(1.366898,103.814047);
        mMap.addMarker(new MarkerOptions().position(initialLocation));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(initialLocation,10));
    }
}
