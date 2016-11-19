package class1.istd.travelapp.UserReviews;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
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

import java.util.ArrayList;
import java.util.HashMap;

import class1.istd.travelapp.R;

public class LocationReviews extends AppCompatActivity {
    private String[] locNameList;
    private String[] locParaList;
    private float[] locRatingList;
    private Bitmap[] locBitmapList;
    private Uri[] locImgURI;
    private ArrayList<Bitmap> bitmapTempList = new ArrayList<>();
    private HashMap<String, Bitmap> bitmapHashMap = new HashMap<>();
    private String[] locImgURLList;
    private String titleString;
//    private FeedAdapter newAdapt;
    private FeedStringAdapter newAdaper;

//    private Bitmap noImageBitmap;
//    private int loadImgCounter;
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
        setContentView(R.layout.content_user_review_location);

//        noImageBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.no_image);

        myDB = FirebaseDatabase.getInstance();
        myStorage = FirebaseStorage.getInstance();

        Bundle extra = getIntent().getExtras();
        if (extra!=null) {
            if (extra.get("sourceActivity") != null) {
                if(extra.get("sourceActivity").equals("routeOpt")) {
                    titleString = (String) extra.get("location");
                    DatabaseReference tempRatingRef = myDB.getReference("metaData").child(titleString).child("currentRating");
                    tempRatingRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            try {
                                rating = ((Double) dataSnapshot.getValue()).floatValue();
                            } catch (Exception e) {
                                rating = ((Long) dataSnapshot.getValue()).floatValue();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                } else {
                    try {
                        rating = ((Double) extra.get("averageRating")).floatValue();
                    } catch (Exception e) {
                        rating = ((Long) extra.get("averageRating")).floatValue();
                    }
                    titleString = (String) extra.get("location");
                }
            } else {
                try {
                    rating = ((Double) extra.get("averageRating")).floatValue();
                } catch (Exception e) {
                    rating = ((Long) extra.get("averageRating")).floatValue();
                }
                titleString = (String) extra.get("location");
            }
        }

        TextView title = (TextView) findViewById(R.id.locRevsTextView);
        title.setText(titleString);
        RatingBar thisrating = (RatingBar) findViewById(R.id.loclocratingBar);
        thisrating.setRating(rating);

        setFeedListenerLoc(titleString);
    }

    public void setFeedListenerLoc(final String location) { //set which locaiton to lsiten to
        myDB = FirebaseDatabase.getInstance();
        locationRef = myDB.getReference("feeds").child(location);
//        locationRef.addValueEventListener(new ValueEventListener() {
        locationRef.addListenerForSingleValueEvent(locationRefListen = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                HashMap<String, Object> getVal = (HashMap<String, Object>) dataSnapshot.getValue();
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
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void loadOneImage(final int counter) {
        final String path = locImgURLList[counter];
        final long BYTE_LIMIT = 1024 * 1024;
                            try {
                                if (locImgURLList[counter] != null) {
                                    StorageReference thisImgStored = myStorage.getReference(path);
                                    thisImgStored.getBytes(BYTE_LIMIT).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                                        @Override
                                        public void onSuccess(byte[] bytes) {
                                            Bitmap thisbit = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
//                                            bitmapTempList.add(thisbit);
//                                            bitmapHashMap.put(path, thisbit);
                                            locBitmapList[counter] = thisbit;
                                            refreshFeedList();
                                            if(counter+1<locImgURLList.length) {
                                                loadOneImage(counter+1);
                                            }

                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(getApplicationContext(), "ERO:" + e.getMessage(), Toast.LENGTH_LONG).show();
                                            if(counter+1<locImgURLList.length) {
                                                loadOneImage(counter+1);
                                            }
                                        }
                                    });
                                } else {
                                    if(counter+1<locImgURLList.length) {
                                        loadOneImage(counter+1);
                                    }
                                }

                            } catch (Exception e) {
                                Toast.makeText(getApplicationContext(), "EFRO:" + e.getMessage(), Toast.LENGTH_LONG).show();
                                if(counter+1<locImgURLList.length) {
                                    loadOneImage(counter+1);
                                }
                            }
    }


    public void refreshFeedList() {
        Toast.makeText(this, "updating.."+locNameList.length, Toast.LENGTH_SHORT).show();
        feedslist = (ListView) findViewById(R.id.feedList);
        if(feedslist.getAdapter()==null) {
//            FeedAdapter newAdapt = new FeedAdapter(this, R.layout.feed_item_layout, locNameList, locParaList, locRatingList, locBitmapList);
//            feedslist.setAdapter(newAdapt);
            newAdaper = new FeedStringAdapter(this,  R.layout.list_item_review_feed, locNameList, locParaList, locRatingList, locImgURLList, myStorage);
            feedslist.setAdapter(newAdaper);
        }

        try {
//            FeedStringAdapter newAdaper = feedslist.getAdapter();
            newAdaper.changeData(locNameList, locParaList, locRatingList, locImgURLList, myStorage);
            newAdaper.notifyDataSetChanged();
            Toast.makeText(this, "Changed:"+feedslist.getAdapter().getCount(), Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(this, "ChangeDaTAeRROR:"+e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void goToAddReview(View view) {
        Intent reviewIntent = new Intent(this, AddReview.class);
        reviewIntent.putExtra("location", titleString);
        startActivityForResult(reviewIntent, 333);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        savedBitmap = (Bitmap) data.getExtras().get("data");
//        capturedImg.setImageBitmap(savedBitmap);
//        if(resultCode==1) {
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
