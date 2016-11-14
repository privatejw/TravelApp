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

public class RouteAdapter extends BaseAdapter {
    Context context;
    List<ItemRoute> itemRoutes;

    RouteAdapter(Context context, List<ItemRoute> itemRoutes) {
        this.context = context;
        this.itemRoutes = itemRoutes;
    }

    @Override
    public int getCount() {
        return itemRoutes.size();
    }

    @Override
    public long getItemId(int i) {
        return itemRoutes.indexOf(getItem(i));
    }

    @Override
    public Object getItem(int i) {
        return itemRoutes.get(i);
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder = null;

        LayoutInflater mInflater = (LayoutInflater) context.getSystemService(
                Activity.LAYOUT_INFLATER_SERVICE);

        if (view == null) {
            view = mInflater.inflate(R.layout.list_item_route, null);
            holder = new RouteAdapter.ViewHolder();
            holder.txtLocation = (TextView) view.findViewById(R.id.txtLocation);
            holder.imagePathType = (ImageView) view.findViewById(R.id.imagePathType);
            holder.txtRouteInfo = (TextView) view.findViewById(R.id.txtRouteInfo);

            ItemRoute row_pos = itemRoutes.get(i);

            holder.txtLocation.setText(row_pos.getLocation());
            holder.imagePathType.setImageResource(row_pos.getImagePathType());
            holder.txtRouteInfo.setText(row_pos.getRouteInfo());
        } else {
            holder = (RouteAdapter.ViewHolder) view.getTag();
        }

        ItemRoute itemRoute = itemRoutes.get(i);
        holder.txtLocation.setText(itemRoute.getLocation());
        holder.imagePathType.setImageResource(itemRoute.getImagePathType());
        holder.txtRouteInfo.setText(itemRoute.getRouteInfo());

        return view;
    }

    private class ViewHolder {
        TextView txtLocation;
        ImageView imagePathType;
        TextView txtRouteInfo;
    }
}
