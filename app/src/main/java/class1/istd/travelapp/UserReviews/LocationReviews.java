package class1.istd.travelapp.UserReviews;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import class1.istd.travelapp.Location.MapActivity;
import class1.istd.travelapp.R;

public class LocationReviews extends AppCompatActivity {
    private String[] locNameList;
    private String[] locParaList;
    private float[] locRatingList;
    private Bitmap[] locBitmapList;
    private ArrayList<Bitmap> bitmapTempList = new ArrayList<>();
    private HashMap<String, Bitmap> bitmapHashMap = new HashMap<>();
    private String[] locImgURLList;
    private String titleString;
    private FeedStringAdapter newAdaper;

    private String[] emptyStringName = {"Nobody"};
    private String[] emptyStringUrl = {"images/100000000.jpg"};
    private String[] emptyStringPara = {"There is currently no review on this place"};
    private float[] emptyFloat = {0};
    private Uri[] emptyUri;

    private DatabaseReference locationRef;
    private ValueEventListener locationRefListen;

    private String passMethod;
    private int passedLength;

    private float rating = 0;
    private ListView feedslist;
    private FirebaseDatabase myDB;
    private FirebaseStorage myStorage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_review_location);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        myDB = FirebaseDatabase.getInstance();
        myStorage = FirebaseStorage.getInstance();
        emptyUri = new Uri[1];
        emptyUri[0]=  Uri.parse("android.resource://" + getPackageName() + "/" + R.drawable.no_image);

        Bundle extra = getIntent().getExtras();
        if (extra!=null) {
                titleString = (String) extra.get("location");
        } else {
            //ERROR - must take a location (String)

            this.finish();
        }

        TextView title = (TextView) findViewById(R.id.locRevsTextView);
        title.setText(titleString);
        RatingBar thisrating = (RatingBar) findViewById(R.id.loclocratingBar);
        thisrating.setRating(rating);

        setFeedListenerLoc(titleString);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }
        return super.onOptionsItemSelected(item);
    }

    public void setFeedListenerLoc(final String location) { //set which locaiton to lsiten to
        myDB = FirebaseDatabase.getInstance();
        locationRef = myDB.getReference("feeds").child(location);
//        locationRef.addValueEventListener(new ValueEventListener() {
        locationRef.orderByKey().addListenerForSingleValueEvent(locationRefListen = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                HashMap<String, Object> getVal = (HashMap<String, Object>) dataSnapshot.getValue();
                if(getVal!=null) {
                    try {
//                    locAveRating
                        rating = ((Long) getVal.get("currentRating")).floatValue();
                    } catch (Exception e) {
//                    locAveRating
                        rating = ((Double) getVal.get("currentRating")).floatValue();
                    }
                    HashMap<Integer, HashMap> locData = (HashMap<Integer, HashMap>) getVal.get("reviews");
                    if (locData != null) {

//                    intentGoTo(location);
                        ArrayList<String> nameTempList = new ArrayList<>();
                        ArrayList<String> paraTempList = new ArrayList<>();
                        ArrayList<Float> ratingTempList = new ArrayList<>();
                        ArrayList<String> urlTempList = new ArrayList<>();
                        final ArrayList<Bitmap> bitmapTempList = new ArrayList<>();
                        for (final HashMap<String, Object> feedInstance : locData.values()) {

                            nameTempList.add((String) feedInstance.get("Name"));
                            paraTempList.add((String) feedInstance.get("Review"));
                            try {
                                ratingTempList.add(((Long) feedInstance.get("Rating")).floatValue());
                            } catch (Exception e) {
                                ratingTempList.add(((Double) feedInstance.get("Rating")).floatValue());
                            }
                            urlTempList.add((String) feedInstance.get("ImageURL"));

                            String[] newnamelist = nameTempList.toArray(new String[nameTempList.size()]);
                            String[] newPara = paraTempList.toArray(new String[paraTempList.size()]);
                            String[] newUrl = urlTempList.toArray(new String[urlTempList.size()]);
                            Uri[] newURI = new Uri[urlTempList.size()];
                            float[] newRatt = new float[ratingTempList.size()];
                            if (ratingTempList.size() > 0) {
                                newRatt = new float[ratingTempList.size()];
                                for (int i = 0; i < ratingTempList.size(); i++) {
                                    newRatt[i] = ratingTempList.get(i);
                                }
                            }
                            locNameList = newnamelist;
                            locParaList = newPara;
                            locImgURLList = newUrl;
                            locRatingList = newRatt;
                        }
                        refreshFeedList();
                    } else {
                        locNameList = emptyStringName;
                        locParaList = emptyStringPara;
                        locRatingList = emptyFloat;
                        locImgURLList = emptyStringUrl;
                        refreshFeedList();
                    }
                } else {
                    HashMap<String, Object> newPlace = new HashMap<String, Object>();
                    newPlace.put("currentRating", 0.0);
                    newPlace.put("totalReviews", 0);
                    newPlace.put("totalRating", 0);
                    locationRef.updateChildren(newPlace);
                    locNameList = emptyStringName;
                    locParaList = emptyStringPara;
                    locRatingList = emptyFloat;
                    locImgURLList = emptyStringUrl;
                    refreshFeedList();

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void refreshFeedList() {
        RatingBar thisrating = (RatingBar) findViewById(R.id.loclocratingBar);
        thisrating.setRating(rating);
        feedslist = (ListView) findViewById(R.id.feedList);
        if(feedslist.getAdapter()==null) {
            newAdaper = new FeedStringAdapter(this,  R.layout.list_item_review_feed, locNameList, locParaList, locRatingList, locImgURLList, myStorage);
            feedslist.setAdapter(newAdaper);
        }

        try {
            newAdaper.changeData(locNameList, locParaList, locRatingList, locImgURLList, myStorage);
            newAdaper.notifyDataSetChanged();
        } catch (Exception e) {
            Toast.makeText(this, "ChangeDaTAeRROR:"+e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void goToLocation(View view) {
        Intent mapTo = new Intent(this, MapActivity.class);
        mapTo.putExtra("location", titleString);
        startActivity(mapTo);
    }

    public void goToAddReview(View view) {
        Intent reviewIntent = new Intent(this, AddReview.class);
        reviewIntent.putExtra("location", titleString);
        startActivityForResult(reviewIntent, 333);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
            if (requestCode == 333) {
                feedslist.setAdapter(null);
                setFeedListenerLoc(titleString);
            }
//        }

    }

    @Override public void finish() {
        locationRef.removeEventListener(locationRefListen);
        super.finish();
    }

    @Override public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }
}
