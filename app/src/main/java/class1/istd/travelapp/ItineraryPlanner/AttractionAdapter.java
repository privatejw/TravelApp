package class1.istd.travelapp.ItineraryPlanner;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.util.List;

import class1.istd.travelapp.R;


public class AttractionAdapter extends BaseAdapter {
    Context context;
    List<ItemAttraction> itemAttractions;

    AttractionAdapter(Context context, List<ItemAttraction> itemAttractions) {
        this.context = context;
        this.itemAttractions = itemAttractions;
    }

    @Override
    public int getCount() {
        return itemAttractions.size();
    }

    @Override
    public Object getItem(int i) {
        return itemAttractions.get(i);
    }

    @Override
    public long getItemId(int i) {
        return itemAttractions.indexOf(getItem(i));
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder = null;

        LayoutInflater mInflater = (LayoutInflater) context.getSystemService(
                Activity.LAYOUT_INFLATER_SERVICE);

        if (view == null) {
            view = mInflater.inflate(R.layout.list_item_attraction, null);
            holder = new ViewHolder();
            holder.item_name = (TextView) view.findViewById(R.id.item_name);
            holder.itemAttractionRatingBar = (RatingBar) view.findViewById(R.id.itemAttractionRatingBar);
            holder.toggleBtn = (ToggleButton) view.findViewById(R.id.toggleBtn);

            ItemAttraction row_pos = itemAttractions.get(i);

            holder.item_name.setText(row_pos.getItem_name());
            holder.itemAttractionRatingBar.setRating(row_pos.getItem_rating());
            holder.toggleBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ItemAttraction itemAttraction = (ItemAttraction) ((ToggleButton) view).getTag();
                    itemAttraction.setSelected(((ToggleButton) view).isChecked());
                }
            });
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        ItemAttraction itemAttraction = itemAttractions.get(i);
        holder.item_name.setText(itemAttraction.getItem_name());
        holder.itemAttractionRatingBar.setRating(itemAttraction.getItem_rating());
        holder.toggleBtn.setChecked(itemAttraction.isSelected());
        holder.toggleBtn.setTag(itemAttraction);

        return view;
    }

    private class ViewHolder {
        TextView item_name;
        RatingBar itemAttractionRatingBar;
        ToggleButton toggleBtn;
    }
}
