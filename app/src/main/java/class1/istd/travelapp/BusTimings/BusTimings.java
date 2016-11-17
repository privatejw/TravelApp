package class1.istd.travelapp.BusTimings;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import class1.istd.travelapp.BaseActivity;
import class1.istd.travelapp.R;

public class BusTimings extends BaseActivity {
    Button getTime;
    EditText busStop;
    EditText bus;
    String API;
    TextView showTime;
    String timing = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus_timings);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setLogo(R.drawable.ic_menu_send);
            getSupportActionBar().setDisplayUseLogoEnabled(true);
            getSupportActionBar().hide();
            getSupportActionBar().show();
        } else {
            Log.i("No action bar", "oh dear");
        }

        getTime = (Button) findViewById(R.id.getTime);
        getTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                busStop = (EditText) findViewById(R.id.busStop);
                bus = (EditText) findViewById(R.id.bus);
                showTime = (TextView) findViewById(R.id.showTime);
                if (busStop.getText().toString().equals("")){
                    Toast.makeText(getApplication(), "Enter Bus Stop Number",Toast.LENGTH_SHORT).show();
                }
                else if (bus.getText().toString().equals("")){
                    Toast.makeText(getApplication(), "Enter Bus Number",Toast.LENGTH_SHORT).show();
                }
                else{
                    API = "http://datamall2.mytransport.sg/ltaodataservice/BusArrival?BusStopID=" + busStop.getText().toString() + "&ServiceNo=" + bus.getText().toString() + "&SST=True";
                    new DoGetProduct().execute();
                    showTime.setText(timing);}
            }
        });

    }

    class DoGetProduct extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            try {
                URL url = new URL(API);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setRequestProperty("AccountKey", "AzseK0zyRw+sXxF5HOo77w==");
                connection.connect();
                InputStream is = connection.getInputStream();
                int byteChar;
                String result = "";
                while ((byteChar = is.read()) != -1) {
                    result += (char) byteChar;
                }

                JSONObject jObject = new JSONObject(result);
                JSONArray jArray = jObject.getJSONArray("Services");

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.ENGLISH);
                String curTiming = sdf.format(Calendar.getInstance().getTime());
                Pattern pattern = Pattern.compile(".*([0-9][0-9]) ([0-9][0-9]):([0-9][0-9]):([0-9][0-9])");
                Matcher matcher = pattern.matcher(curTiming);
                int curTime = 0;
                int curDate = 0;
                if (matcher.find())
                {
                    curDate = (Integer.parseInt(matcher.group(1)));
                    curTime += (Integer.parseInt(matcher.group(2)) * 60 * 60);
                    curTime += (Integer.parseInt(matcher.group(3)) * 60);
                    curTime += Integer.parseInt(matcher.group(4));
                }

                if (jArray.length() == 0) {
                    timing = "Invalid bus stop number or bus number";
                } else {
                    try {
                        JSONObject oneObject = jArray.getJSONObject(0);
                        timing = "Status: " + oneObject.getString("Status") + "\n";
                        if (oneObject.getJSONObject("NextBus").getString("EstimatedArrival").equals("")) {
                            timing += "Next Bus: No timing available";
                        } else {
                            String busTiming = oneObject.getJSONObject("NextBus").getString("EstimatedArrival");
                            Pattern pattern1 = Pattern.compile(".*([0-9][0-9])T([0-9][0-9]):([0-9][0-9]):([0-9][0-9])");
                            Matcher matcher1 = pattern1.matcher(busTiming);
                            int busTime = 0;
                            int busDate = 0;
                            if (matcher1.find())
                            {
                                busDate = (Integer.parseInt(matcher1.group(1)));
                                busTime += (Integer.parseInt(matcher1.group(2)) * 60 * 60);
                                busTime += (Integer.parseInt(matcher1.group(3)) * 60);
                                busTime += Integer.parseInt(matcher1.group(4));
                            }
                            if (busDate == curDate){
                                int waitTime = Math.abs(busTime - curTime) / 60;
                                if (waitTime != 0){
                                    timing += "Next Bus: " + waitTime + " minute(s)\n";}
                                else{
                                    timing += "Next Bus: Arriving\n";
                                }
                            }
                            else{
                                int waitTime = Math.abs(busTime - (curTime - 24*60*60)) / 60;
                                if (waitTime != 0){
                                    timing += "Next Bus: " + waitTime + " minute(s)\n";}
                                else{
                                    timing += "Next Bus: Arriving\n";
                                }
                            }
                        }
                        if (oneObject.getJSONObject("SubsequentBus").getString("EstimatedArrival").equals("")) {
                            timing += "Subsequent Bus: No timing available\n";
                        } else {
                            String busTiming = oneObject.getJSONObject("SubsequentBus").getString("EstimatedArrival");
                            Pattern pattern2 = Pattern.compile(".*([0-9][0-9])T([0-9][0-9]):([0-9][0-9]):([0-9][0-9])");
                            Matcher matcher2 = pattern2.matcher(busTiming);
                            int busTime = 0;
                            int busDate = 0;
                            if (matcher2.find())
                            {
                                busDate = (Integer.parseInt(matcher2.group(1)));
                                busTime += (Integer.parseInt(matcher2.group(2)) * 60 * 60);
                                busTime += (Integer.parseInt(matcher2.group(3)) * 60);
                                busTime += Integer.parseInt(matcher2.group(4));
                            }
                            if (busDate == curDate){
                                int waitTime = Math.abs(busTime - curTime) / 60;
                                if (waitTime != 0){
                                    timing += "Subsequent Bus: " + waitTime + " minute(s)\n";}
                                else{
                                    timing += "Subsequent Bus: Arriving\n";
                                }
                            }
                            else{
                                int waitTime = Math.abs(busTime - (curTime - 24*60*60)) / 60;
                                if (waitTime != 0){
                                    timing += "Subsequent Bus: " + waitTime + " minute(s)\n";}
                                else{
                                    timing += "Subsequent Bus: Arriving\n";
                                }
                            }
                        }
                    } catch (Exception e) {
                        Log.d("error1", e.toString());
                    }
                }
            } catch (Exception e) {
                Log.d("error2", e.toString());
            }
            return null;
        }
    }
}
