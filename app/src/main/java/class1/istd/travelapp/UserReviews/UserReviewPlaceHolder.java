package class1.istd.travelapp.UserReviews;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.HashMap;

import class1.istd.travelapp.BaseActivity;
import class1.istd.travelapp.R;

public class UserReviewPlaceHolder extends BaseActivity {
    private ListView myLocationList;

    private FirebaseDatabase myDB;  //firebase objects
    private FirebaseStorage myStorage;
    private HashMap<String, HashMap> metaData;
    private ValueEventListener metaListen;
    private DatabaseReference TotalRevRef;

    String[] defname = {"loading.."}; //default lists for initial listadapters
    float[] defrating = {(float)0};
    int[] defimgid = {R.drawable.bus_icon};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_review_place_holder);

        myDB = FirebaseDatabase.getInstance();
        myStorage = FirebaseStorage.getInstance();

        TotalRevRef = myDB.getReference("metaData");
        TotalRevRef.addValueEventListener(metaListen = new ValueEventListener() { //start listening to rating values
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                metaData = (HashMap<String, HashMap>) dataSnapshot.getValue();
                ArrayList<String> locationNames = new ArrayList<String>();
                ArrayList<Float> locationRatings = new ArrayList<Float>();
                for(HashMap.Entry locMeta: metaData.entrySet()) {
                    locationNames.add((String)locMeta.getKey());
                    try {
                        locationRatings.add(((Double) ((HashMap)locMeta.getValue()).get("currentRating")).floatValue());
                    } catch (Exception e) {
                        locationRatings.add(((Long) ((HashMap)locMeta.getValue()).get("currentRating")).floatValue());
                    }
                }

                defname = locationNames.toArray(new String[locationNames.size()]);
                defrating = new float[locationRatings.size()];
                for(int i=0; i<locationRatings.size();i++) {
                    defrating[i] = locationRatings.get(i).floatValue();
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

    public void refreshLocList() {
        myLocationList = (ListView) findViewById(R.id.locationListView);
        LocationListAdapter newLocListAdapt = new LocationListAdapter(this, R.layout.list_item_location_review, defname, defrating, defimgid);
        myLocationList.setAdapter(newLocListAdapt);
        myLocationList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                LocationListAdapter getMyAdapter = (LocationListAdapter) parent.getAdapter();
                Toast.makeText(getApplicationContext(), "Going to:" + getMyAdapter.getLocationNames()[position], Toast.LENGTH_SHORT).show();
                prepareToGo(getMyAdapter.getLocationNames()[position]);
            }
        });
    }

    public void prepareToGo(String destination) {
        // ----- sending whole array to next activity method
        //setFeedListenerLoc(destination);
        //

        Intent gothereintent = new Intent(this, LocationReviews.class);
//        gothereintent.putExtra("Method", "Length");
        gothereintent.putExtra("location", destination);
        HashMap<String, Long> placeMeta = metaData.get(destination);
        gothereintent.putExtra("averageRating", placeMeta.get("currentRating"));
//        gothereintent.putExtra("listLength", placeMeta.get("totalReviews"));
        startActivity(gothereintent);
    }

    @Override public void onBackPressed() {
        super.onBackPressed();
        TotalRevRef.removeEventListener(metaListen);
        this.finish();
    }


}
