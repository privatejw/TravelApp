package class1.istd.travelapp.UserReviews;

import android.content.Intent;
import android.location.Address;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.view.menu.MenuBuilder;
import android.util.Log;
import android.view.MenuInflater;
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
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import class1.istd.travelapp.BaseActivity;
import class1.istd.travelapp.R;
import class1.istd.travelapp.SearchFunction;

public class UserReviewPlaceHolder extends BaseActivity {
    private ListView myLocationList;
    private ProgressBar thisProgress;
    private int progressStatus = 0;

    private FirebaseDatabase myDB;  //firebase objects
    private FirebaseStorage myStorage;
    private HashMap<String, HashMap> metaData;
    private ValueEventListener metaListen;
    private DatabaseReference TotalRevRef;

    HashMap<String, Integer> attractionlist;
    String[] attractionNames;
    String[] defname = {"loading.."}; //default lists for initial listadapters
    float[] defrating = {(float)0};
    int[] defimgid = {R.drawable.bus_icon};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_review_place_holder);

        myDB = FirebaseDatabase.getInstance();
        myStorage = FirebaseStorage.getInstance();

        thisProgress = (ProgressBar) findViewById(R.id.locRatingsProgressBar);

        defname = getResources().getStringArray(R.array.item_attractions);
        attractionNames = getResources().getStringArray(R.array.item_attractions);
        defrating = new float[defname.length];
        defimgid = new int[defname.length];
        attractionlist = new HashMap<>();
        for(int i=0; i<defname.length; i++) {
            attractionlist.put(defname[i], i);
            defrating[i] = 0;
            defimgid[i] = R.drawable.bus_icon;
        }

        TotalRevRef = myDB.getReference("metaData");
        TotalRevRef.addValueEventListener(metaListen = new ValueEventListener() { //start listening to rating values
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                thisProgress.setIndeterminate(true);
                thisProgress.setVisibility(View.VISIBLE);
                metaData = (HashMap<String, HashMap>) dataSnapshot.getValue();
                for(HashMap.Entry locMeta: metaData.entrySet()) {
                    try {
                        int i = attractionlist.get((String) locMeta.getKey());
                        try {
                            defrating[i] = ((Double) ((HashMap) locMeta.getValue()).get("currentRating")).floatValue();
                        } catch (Exception e) {
                            defrating[i] = ((Long) ((HashMap) locMeta.getValue()).get("currentRating")).floatValue();
                        }
                    } catch (Exception i) {
                        //foregin data
                        Log.e("Failed to fetch entry", ": no such place in database");
                    }
                }

                refreshLocList();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        myLocationList = (ListView) findViewById(R.id.locationListView);
        LocationListAdapter newLocListAdapt = new LocationListAdapter(this, R.layout.list_item_location_review, defname, defrating, defimgid);
        myLocationList.setAdapter(newLocListAdapt);
        myLocationList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                LocationListAdapter getMyAdapter = (LocationListAdapter) parent.getAdapter();
                Toast.makeText(getApplicationContext(), "please wait", Toast.LENGTH_SHORT).show();
            }
        });


    }

    @Override protected void onDestroy() {
        myDB.goOffline();
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_review,menu);
        MenuItem searchViewItem = menu.findItem(R.id.search_bar2);
        final SearchView thisSearchView = (SearchView) MenuItemCompat.getActionView(searchViewItem);
        thisSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                ArrayList<String> newnames = new ArrayList<String>();

                for(String place: attractionNames) {
                    if(place.toLowerCase().contains(s.toLowerCase())) {
                        newnames.add(place);
                    }
                }

                defname = newnames.toArray(new String[newnames.size()]);
                defrating = new float[defname.length];
                defimgid = new int[defname.length];
                attractionlist = new HashMap<>();
                for(int i=0; i<defname.length; i++) {
                    attractionlist.put(defname[i], i);
                    defrating[i] = 0;
                    defimgid[i] = R.drawable.bus_icon;
                }

                refreshLocList();


                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    public void refreshLocList() {
        myLocationList = (ListView) findViewById(R.id.locationListView);
        LocationListAdapter newLocListAdapt = new LocationListAdapter(this, R.layout.list_item_location_review, defname, defrating, defimgid);
        myLocationList.setAdapter(newLocListAdapt);
        myLocationList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                LocationListAdapter getMyAdapter = (LocationListAdapter) parent.getAdapter();
                prepareToGo(getMyAdapter.getLocationNames()[position]);
            }
        });
        thisProgress.setIndeterminate(false);
        thisProgress.setVisibility(View.INVISIBLE);
    }

    public void prepareToGo(String destination) {
        // ----- sending whole array to next activity method
        Intent gothereintent = new Intent(this, LocationReviews.class);
        gothereintent.putExtra("location", destination);
        startActivity(gothereintent);
    }

    @Override public void onBackPressed() {
        super.onBackPressed();
        TotalRevRef.removeEventListener(metaListen);
        this.finish();
    }


}
