package com.example.android.findmyhcptest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    //String apiUrl = "https://data.cms.gov/resource/ehrv-m9r6.json?provider_state=PA";
    String apiUrlTest = "https://data.cms.gov/resource/ehrv-m9r6.json?provider_city=HUNTINGDON";
    String providerName, address, providerCity, providerState, providerZip;
    String avgTotalPayments, avgCoveredCharges, totalDischarges, avgMedicare;


    TextView titleTextView, categoryTextView1, categoryTextView2, categoryTextView3,
            categoryTextView4, categoryTextView5;
    ProgressDialog progressDialog;
    Button displayData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // get the reference of View's
        titleTextView = (TextView) findViewById(R.id.titleTextView);
        categoryTextView1 = (TextView) findViewById(R.id.categoryTextView1);
        categoryTextView2 = (TextView) findViewById(R.id.categoryTextView2);
        categoryTextView3 = (TextView) findViewById(R.id.categoryTextView3);
        categoryTextView4 = (TextView) findViewById(R.id.categoryTextView4);
        categoryTextView5 = (TextView) findViewById(R.id.categoryTextView5);
        displayData = (Button) findViewById(R.id.displayData);
        //imageView = (ImageView) findViewById(R.id.imageView);
        // implement setOnClickListener event on displayData button
        displayData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // create object of MyAsyncTasks class and execute it
                MyAsyncTasks myAsyncTasks = new MyAsyncTasks();
                myAsyncTasks.execute();
            }
        });
    }

    public class MyAsyncTasks extends AsyncTask<String, String, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // display a progress dialog for good user experience
            progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setMessage("Please Wait");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            // implement API in background and store the response in current variable
            String current = "";
            try {
                URL url;
                HttpURLConnection urlConnection = null;
                try {
                    url = new URL(apiUrlTest);

                    urlConnection = (HttpURLConnection) url
                            .openConnection();

                    InputStream in = urlConnection.getInputStream();

                    InputStreamReader isw = new InputStreamReader(in);

                    int data = isw.read();
                    while (data != -1) {
                        current += (char) data;
                        data = isw.read();
                        //System.out.print(current);
                    }
                    // return the data to onPostExecute method
                    System.out.print(current);
                    return current;

                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (urlConnection != null) {
                        urlConnection.disconnect();
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
                return "Exception: " + e.getMessage();
            }
            return current;
        }

        @Override
        protected void onPostExecute(String s) {

            Log.d("data", s.toString());
            // dismiss the progress dialog after receiving data from API
            progressDialog.dismiss();
            try {
                HcpDbHelper myDbHelper = new HcpDbHelper(getApplicationContext());
                SQLiteDatabase db = myDbHelper.getWritableDatabase();
                ContentValues values = new ContentValues();
                // JSON Parsing of data
                JSONArray jsonArray = new JSONArray(s);
                //JSONObject json = loadJSON

                //JSONObject oneObject = jsonArray.getJSONObject(0);
                //JSONObject oneObject = jsonArray.getJSONObject(0);
                for (int i = 0; i < jsonArray.length(); i++)
                {
                    JSONObject oneHCP = jsonArray.getJSONObject(i);
                    providerName = oneHCP.getString("provider_name");
                    address = oneHCP.getString( "provider_street_address");
                    providerCity = oneHCP.getString("provider_city");
                    providerState = oneHCP.getString("provider_state");
                    providerZip = oneHCP.getString("provider_zip_code");
                    avgCoveredCharges = oneHCP.getString( "average_covered_charges");
                    avgTotalPayments = oneHCP.getString("average_medicare_payments");
                    avgMedicare = oneHCP.getString("average_medicare_payments_2");
                    totalDischarges = oneHCP.getString("total_discharges");

                    values.put(HCPContract.HCPEntry.COLUMN_NAME, providerName);
                    values.put(HCPContract.HCPEntry.COLUMN_ADDRESS, address);
                    values.put(HCPContract.HCPEntry.COLUMN_CITY, providerCity);
                    values.put(HCPContract.HCPEntry.COLUMN_STATE, providerState );
                    values.put(HCPContract.HCPEntry.COLUMN_ZIP, providerZip );
                    values.put(HCPContract.HCPEntry.COLUMN_AVG_COVERED_CHARGES, avgCoveredCharges );
                    values.put(HCPContract.HCPEntry.COLUMN_AVG_TOTAL_PAYMENTS, avgTotalPayments );
                    values.put(HCPContract.HCPEntry.COLUMN_AVG_MEDICARE_PAYMENTS, avgMedicare );
                    values.put(HCPContract.HCPEntry.COLUMN_TOTAL_DISCHARGES, totalDischarges);

                    //insert the values into the database
                    long newRowId = db.insert(
                            HCPContract.HCPEntry.TABLE_NAME,  //table name for insert
                            null,  //null is all columns
                            values);  //values for the insert

                    //set up toast for saved data
                    int duration = Toast.LENGTH_LONG;
                    String result;

                    //check if data was inserted put result into the toast
                    if (newRowId != -1)
                    {
                        result = "Database Inserted!";
                    }

                    else
                    {
                        result = "ERROR";
                    }

                    //show the toast
                    Toast toast = Toast.makeText(getApplicationContext(), result, duration);
                    toast.show();
                }

//                // Pulling items from the array
//                providerName = oneObject.getString("provider_name");
//                providerCity = oneObject.getString("provider_city");
//                address = oneObject.getString( "provider_street_address");
//                averageCoveredCharges = oneObject.getString( "average_covered_charges");
//                avgMedicare = oneObject.getString("average_medicare_payments");
//                totalDischarges = oneObject.getString("total_discharges");

                // display the data in UI
                titleTextView.setText("Name: "+providerName);
                categoryTextView1.setText("City: "+providerCity);
                categoryTextView2.setText("Address: "+address);
                categoryTextView3.setText("AVG Covered Charges: $"+avgCoveredCharges);
                categoryTextView4.setText("AVG Medicare Charges: $"+avgMedicare);
                categoryTextView5.setText("Total Discharges: "+totalDischarges);

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

    }
}