package class1.istd.travelapp.ItineraryPlanner;

import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import class1.istd.travelapp.R;

public class DisplayRoute extends AppCompatActivity {
    ListView listRoutes;
    RouteAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_route);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        // row items start
        List<ItemRoute> itemRoutes = (ArrayList<ItemRoute>) getIntent().getExtras().get("itemRoutes");

        listRoutes = (ListView) findViewById(R.id.listRoutes);
        adapter = new RouteAdapter(this, itemRoutes);
        listRoutes.setAdapter(adapter);
        // row items end

        // other ui items
        String overallRouteInfo = getIntent().getExtras().getString("overallRouteInfo");

        TextView txtRouteInfo = (TextView) findViewById(R.id.txtRouteInfo);
        txtRouteInfo.setText(overallRouteInfo);
        // other ui items end
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }
        return super.onOptionsItemSelected(item);
    }
}
