package class1.istd.travelapp.UserReviews;

import android.app.Activity;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import class1.istd.travelapp.R;

/**
 * Created by Kygrykhon on 11/18/2016.
 */

public class FeedStringAdapter extends ArrayAdapter<String> {
    private String[] revName;
    private String[] revPara;
    private float[] revRating;
    private FirebaseStorage stoRef;
    private ArrayList<Uri> tempUri;
    private ArrayList<Bitmap> tempBitmap;

    private static final String sourceURL = "gs://gola-travelapp.appspot.com/";
//    private Bitmap[] imgBitmap;
    private String[] revURL;
    private Uri[] revUri;
    private Activity thisActivity;

    FeedStringAdapter(Activity context, @LayoutRes int ResID, @NonNull String[] Name, @NonNull String[] Para, @NonNull float[] ratings, @NonNull String[] bitbit, FirebaseStorage thisImgRef) {
        super(context, ResID, R.id.nameTextFiew, Name);
        revName = Name;
        revPara = Para;
        revRating = ratings;
        revURL = bitbit;
        tempUri = new ArrayList<>();
        tempBitmap = new ArrayList<>();
//        Bitmap defBit = BitmapFactory.decodeResource(context.getResources(), R.drawable.no_image);
        for (String rev: revURL) {
            tempUri.add(null);
            tempBitmap.add(null);
        }
//        imgBitmap = bitbit;
        thisActivity = context;
        stoRef = thisImgRef;
//        generateURL();

    }


    public void changeData(@NonNull String[] Name, @NonNull String[] Para, @NonNull float[] ratings, @NonNull String[] bitbit, FirebaseStorage thisImgRef) {
        revName = Name;
        revRating = ratings;
        revPara = Para;
        revURL = bitbit;
        stoRef = thisImgRef;
    }

    public View getView(final int position, View view, ViewGroup parents) {
        LayoutInflater inflater = thisActivity.getLayoutInflater();
        View feedtems = inflater.inflate(R.layout.list_item_review_feed, null, false);

        final ImageView imgview = (ImageView) feedtems.findViewById(R.id.feedImageView);
        TextView nametxtview = (TextView) feedtems.findViewById(R.id.nameTextFiew);
        TextView paratxt = (TextView) feedtems.findViewById(R.id.reviewParaText);
        RatingBar rattee = (RatingBar) feedtems.findViewById(R.id.indivRatingBar);

        if(revName[position].equals("Nobody")) {
            rattee.setVisibility(View.INVISIBLE);
        }


        nametxtview.setText(revName[position]);
        paratxt.setText(revPara[position]);
        rattee.setRating(revRating[position]);
//        imgview.setImageBitmap(tempBitmap.get(position));
//        if(tempUri.get(position) ==null) {
            stoRef.getReference(revURL[position]).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Picasso.with(thisActivity).load(uri).into(imgview);
                    tempUri.add(position, uri);
                    tempBitmap.add(position, imgview.getDrawingCache());
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Picasso.with(thisActivity).load(R.drawable.no_image);
                }
            });

        return feedtems;
    }
}
