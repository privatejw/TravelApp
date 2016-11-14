package class1.istd.travelapp.ItineraryPlanner;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
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
            holder.item_image = (ImageView) view.findViewById(R.id.item_image);
            holder.item_name = (TextView) view.findViewById(R.id.item_name);
            holder.item_description = (TextView) view.findViewById(R.id.item_description);
            holder.toggleBtn = (ToggleButton) view.findViewById(R.id.toggleBtn);

            ItemAttraction row_pos = itemAttractions.get(i);

            holder.item_image.setImageResource(row_pos.getItem_image__id());
            holder.item_name.setText(row_pos.getItem_name());
            holder.item_description.setText(row_pos.getItem_description());
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
        holder.item_image.setImageResource(itemAttraction.getItem_image__id());
        holder.item_name.setText(itemAttraction.getItem_name());
        holder.item_description.setText(itemAttraction.getItem_description());
        holder.toggleBtn.setChecked(itemAttraction.isSelected());
        holder.toggleBtn.setTag(itemAttraction);

        return view;
    }

    private class ViewHolder {
        ImageView item_image;
        TextView item_name;
        TextView item_description;
        ToggleButton toggleBtn;
    }
}
