package class1.istd.travelapp.UserReviews;

import android.app.Activity;
import android.graphics.BitmapFactory;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import class1.istd.travelapp.R;

/**
 * Created by Kygrykhon on 11/19/2016.
 */

public class LocationListAdapter extends ArrayAdapter<String> {
    private String[] LocationNames;
    private float[] ratings;
    private int[] imageID;
    private Activity thisActivity;

    public String[] getLocationNames() {
        return LocationNames;
    }

    public float[] getRatings() {
        return ratings;
    }

    public int[] getImageID() {
        return imageID;
    }

    LocationListAdapter(Activity context, @LayoutRes int resID, @NonNull String[] namelist, @NonNull float[] thisrating, @NonNull int[] imageIDList) {
        super(context, resID, R.id.locationName, namelist);
        LocationNames = namelist;
        ratings = thisrating;
        imageID = imageIDList;
        thisActivity = context;
    }

    public void changeData(@NonNull String[] namelist, @NonNull float[] thisrating, @NonNull int[] imageIDList) {
        LocationNames = namelist;
        ratings = thisrating;
        imageID = imageIDList;
    }

    public View getView(int position, View view, ViewGroup parents) {
        LayoutInflater inflater = thisActivity.getLayoutInflater();
        View locitem = inflater.inflate(R.layout.list_item_location_review, null, false);

        ImageView img = (ImageView) locitem.findViewById(R.id.locationImg);
        TextView thisText = (TextView) locitem.findViewById(R.id.locationName);
        RatingBar thisrate = (RatingBar) locitem.findViewById(R.id.locationRating);

        img.setImageBitmap(BitmapFactory.decodeResource(thisActivity.getResources(), imageID[position]));
        thisText.setText(LocationNames[position]);
        thisrate.setRating(ratings[position]);

        return locitem;
    }
}
