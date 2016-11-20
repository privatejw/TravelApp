package class1.istd.travelapp.UserReviews;

import android.app.Activity;
import android.content.Context;
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
    Context context;
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
        this.context = context;
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

    @Override
    public View getView(int position, View view, ViewGroup parents) {
        ViewHolder holder;

        LayoutInflater mInflater = (LayoutInflater) thisActivity.getSystemService(
                Activity.LAYOUT_INFLATER_SERVICE);

        if (view == null) {
            view = mInflater.inflate(R.layout.list_item_location_review2, null);
            holder = new ViewHolder();
//            holder.imageView = (ImageView) view.findViewById(R.id.locationImage2);
            holder.thisText = (TextView) view.findViewById(R.id.locationName2);
            holder.thisrate = (RatingBar) view.findViewById(R.id.locationRating2);

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

//        holder.imageView.setImageResource(R.drawable.merlion);
        holder.thisText.setText(LocationNames[position]);
        holder.thisrate.setRating(ratings[position]);

        return view;


//        LayoutInflater inflater = thisActivity.getLayoutInflater();
//        View locitem = inflater.inflate(R.layout.list_item_location_review, null, false);
//
//        ImageView imageView = (ImageView) locitem.findViewById(R.id.locationImg);
//        TextView thisText = (TextView) locitem.findViewById(R.id.locationName);
//        RatingBar thisrate = (RatingBar) locitem.findViewById(R.id.locationRating);
//
////        img.setImageBitmap(BitmapFactory.decodeResource(thisActivity.getResources(), imageID[position]));
//        thisText.setText(LocationNames[position]);
//        thisrate.setRating(ratings[position]);
//
//        return locitem;
    }

    private class ViewHolder {
        ImageView imageView;
        TextView thisText;
        RatingBar thisrate;
    }
}
