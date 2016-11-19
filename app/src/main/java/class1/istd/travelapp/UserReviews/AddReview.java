package class1.istd.travelapp.UserReviews;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

import class1.istd.travelapp.R;

public class AddReview extends AppCompatActivity {
    private String titleString;
    private ImageView capturedImg;
    private RatingBar ratingBar;
    private EditText reviewPara;
    private EditText nameText;
    private final int CAMERA_REQCODE = 773;
    private DatabaseReference IDRef;
    private ValueEventListener IDRefEvLis;
    private DatabaseReference TotalRatingRef;
    private ValueEventListener TotalRatingEvLis;
    private DatabaseReference NumberRatingRef;
    private ValueEventListener NumberRatingEvLis;
    private int FeedID;
    private int fixedFeedID;
    private float totalRating;
    private int noofrating;

    private String filePath;
    private File tempFile;
    private Uri ImageUri;

    private boolean haventSubmit = true;
    private Bitmap defaultBit;

    private FirebaseDatabase myDB;
    private FirebaseStorage myStorage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_user_review_addreview);
        haventSubmit = true;
        defaultBit = BitmapFactory.decodeResource(getResources(), R.drawable.no_image);
        filePath = Environment.getExternalStorageDirectory()+"/temp.jpg";
        tempFile = new File(filePath);
        try {
            tempFile.createNewFile();
        } catch (Exception e) {
            //lel
        }
        ImageUri = Uri.fromFile(tempFile);


        Bundle extra = getIntent().getExtras();
        if(extra!=null) {
            titleString = (String) extra.get("location");
        }

        myDB = FirebaseDatabase.getInstance();
        myStorage = FirebaseStorage.getInstance();
        IDRef = myDB.getReference("latestID");
        IDRef.addValueEventListener(IDRefEvLis = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                FeedID = ((Long) dataSnapshot.getValue()).intValue();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        TotalRatingRef = myDB.getReference("feeds").child(titleString).child("totalRating");
        TotalRatingRef.addValueEventListener(TotalRatingEvLis = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    totalRating = ((Long) dataSnapshot.getValue()).floatValue();
                } catch (Exception e) {
                    //lol
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        NumberRatingRef = myDB.getReference("feeds").child(titleString).child("totalReviews");
        NumberRatingRef.addValueEventListener(NumberRatingEvLis = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                noofrating = ((Long) dataSnapshot.getValue()).intValue();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        TextView whereis = (TextView) findViewById(R.id.locationNameTextView);
        whereis.setText(titleString);
        capturedImg = (ImageView) findViewById(R.id.captureImgView);
        ratingBar = (RatingBar) findViewById(R.id.thisRateBar);
        reviewPara = (EditText) findViewById(R.id.reviewEditText);
        nameText = (EditText) findViewById(R.id.nameEditText);
    }

    @Override public void onBackPressed() {
        super.onBackPressed();
        Finhis();
        finish();
    }

    public void takePicture(View view) {
        tekPic();
    }

    public void tekPic() {
        Intent cameraInt = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraInt.putExtra(MediaStore.EXTRA_OUTPUT, ImageUri);
        startActivityForResult(cameraInt, CAMERA_REQCODE); //random
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==CAMERA_REQCODE) {
            try {
                Picasso.with(this).load(tempFile).into(capturedImg);
                if(tempFile.exists()) {
                    Toast.makeText(this, "steady", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "nofile :(", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                Toast.makeText(this, "Walao fail sia", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override protected void onDestroy() {


        super.onDestroy();
    }

    public void submitReview(View view) {
        if(haventSubmit) {
            capturedImg.setDrawingCacheEnabled(true);
            capturedImg.buildDrawingCache();
            Bitmap capturedBit = capturedImg.getDrawingCache();
//            Bitmap defaultBit = BitmapFactory.decodeResource(getResources(), R.drawable.no_image);

            if (capturedBit.equals(defaultBit)) {
                //havent taken picture
                Toast.makeText(this, "Please take a picture", Toast.LENGTH_SHORT).show();
            } else if (ratingBar.getRating() == 0) {
                //havent rated
                Toast.makeText(this, "Please give a star rating", Toast.LENGTH_SHORT).show();
            } else if (reviewPara.getText().toString().equals("")) {
                Toast.makeText(this, "Please put a review of the location", Toast.LENGTH_SHORT).show();
            } else if (nameText.getText().toString().equals("")) {
                Toast.makeText(this, "Please put your name", Toast.LENGTH_SHORT).show();
            } else {
                haventSubmit = false;

                DatabaseReference IDRef = myDB.getReference("");

                DatabaseReference meedref = myDB.getReference("feeds").child(titleString).child("reviews");//.child(Integer.toString(FeedID));
                DatabaseReference feedref = meedref.child(Integer.toString(FeedID));
                fixedFeedID = FeedID;

                Map<String, Object> newChild = new HashMap<>();
                newChild.put("Name", nameText.getText().toString());
                newChild.put("Rating", ratingBar.getRating());
                newChild.put("Review", reviewPara.getText().toString());
                newChild.put("ImageURL", "images/" + Integer.toString(fixedFeedID) + ".jpg");
                feedref.updateChildren(newChild);

                Float newCurrenRating = (totalRating + ratingBar.getRating()) / (noofrating + 1);
                DatabaseReference currentRateRef = myDB.getReference("feeds").child(titleString).child("currentRating");
                currentRateRef.setValue(newCurrenRating);
                float newTotalRating = totalRating + ratingBar.getRating();
                TotalRatingRef.setValue(totalRating + ratingBar.getRating());
                NumberRatingRef.setValue(noofrating + 1);
                DatabaseReference newMetaRef = myDB.getReference("metaData").child(titleString);
                Map<String, Object> newMetaList = new HashMap<>();
                newMetaList.put("currentRating", newCurrenRating);
                newMetaList.put("totalReviews", noofrating + 1);
                newMetaRef.updateChildren(newMetaList);


                StorageReference imageStored = myStorage.getReference("images/" + Integer.toString(fixedFeedID) + ".jpg");
                final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
//            capturedBit.getByteCount();
                int BYTE_LIMIT = 1 * 1024 * 1024;
                int compressRate;
                if (capturedBit.getByteCount() > BYTE_LIMIT) {
                    compressRate = 100 * BYTE_LIMIT / capturedBit.getByteCount();
                } else {
                    compressRate = 100;
                }
//                capturedBit.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
                capturedBit.compress(Bitmap.CompressFormat.JPEG, compressRate, outputStream);
                byte[] capturedArray = outputStream.toByteArray();
                try {
                    outputStream.close();
                } catch (Exception e) {
                    //means closed alr :/
                }

                UploadTask uploadTask = imageStored.putBytes(capturedArray);
                uploadTask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "Upload failed", Toast.LENGTH_SHORT).show();
                        haventSubmit = true;
                    }
                });
                uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Toast.makeText(getApplicationContext(), "Review uploaded!", Toast.LENGTH_SHORT).show();
                        capturedImg.destroyDrawingCache();
                        tempFile.delete();
                        Finhis();
                    }
                });
                int newIDRef = fixedFeedID + 1;
                Map<String, Object> newIDref = new HashMap<>();
                newIDref.put("latestID", newIDRef);
                IDRef.updateChildren(newIDref);
//            IDRef.setValue(newIDRef);

            }
        }
    }



    public void Finhis() {
        IDRef.removeEventListener(IDRefEvLis);
        TotalRatingRef.removeEventListener(TotalRatingEvLis);
        NumberRatingRef.removeEventListener(NumberRatingEvLis);
        IDRef = null;
        TotalRatingRef = null;
        NumberRatingRef = null;
        defaultBit = null;
        tempFile = null;
        myStorage = null;
        myDB = null;
        ImageUri = null;
        capturedImg.destroyDrawingCache();

        finish();
    }
}
