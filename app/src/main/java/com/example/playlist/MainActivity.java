package com.example.playlist;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.widget.ListView;
import android.os.Bundle;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import android.app.Activity;
import android.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Button;



public class MainActivity extends AppCompatActivity {

    private ListView playlist;
    private Button alertbtn;
    private Integer titleNo;
    private String titleEx;
    private String price;
    private List<String> originCountry = new ArrayList<>();

    private static final String TAG = "MainActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        playlist = findViewById(R.id.listView);

        alertbtn = findViewById(R.id.button2);
        alertbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fillDialogBox();
            }
        });

        new AsynDataClass().execute();
    }

    private void fillDialogBox() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        String msg = "No of Titles in playlist : " + titleNo
                + "\nMost Expensive CD Title : " + titleEx
                + "\nMost Expensive CD Price: " + price
                + "\nCountries of Origin : " + originCountry;
        builder.setMessage(msg).setNeutralButton("OK", null);
        AlertDialog alert = builder.create();
        alert.show();
    }

    private class AsynDataClass extends AsyncTask<String, Void, String> {
        HttpURLConnection urlConnection;
        @Override
        protected String doInBackground(String...params) {

            StringBuilder jsonResult = new StringBuilder();

            try{

                URL url = new
                        URL("http://www.papademas.net:81/cd_catalog.json");
                urlConnection = (HttpURLConnection)
                        url.openConnection();
                InputStream in = new
                        BufferedInputStream(urlConnection.getInputStream());


                BufferedReader reader = new BufferedReader(new InputStreamReader(in));

                String line;

                while ((line = reader.readLine()) !=null) {
                    jsonResult.append(line);
                }
                System.out.println("Returned Json url object" + jsonResult.toString());


            } catch (Exception e) {System.out.println("Err: " + e);}
            finally {
                urlConnection.disconnect();
            }
            return jsonResult.toString();
        }
        @Override
        protected void onPreExecute() { }
        @Override
        protected void onPostExecute(String result) {
            System.out.println("Result on post execute: " + result);
            List<ItemObject> parsedObject =
                    returnParsedJsonObject(result);
            CustomAdapter jsonCustomAdapter = new
                    CustomAdapter(MainActivity.this, parsedObject);
            playlist.setAdapter(jsonCustomAdapter);
        }
    }
    private List<ItemObject> returnParsedJsonObject(String result){
        List<ItemObject> jsonObject = new ArrayList<ItemObject>();
        JSONObject resultObject = null;
        JSONArray jsonArray = null;
        ItemObject newItemObject = null; //interior object holder
        try {
            resultObject = new JSONObject(result);
            System.out.println("Preparsed JSON object " +
                    resultObject.toString());
// set up json Array to be parsed
            jsonArray = resultObject.optJSONArray("Bluesy_Music");
        } catch (JSONException e) { e.printStackTrace(); }

        titleNo = jsonArray.length();
        Double maxPrice = 0.0;

        for(int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonChildNode = null;
            try {
                jsonChildNode = jsonArray.getJSONObject(i);
                //get all data from stream
                String songSold =
                        jsonChildNode.getString("SOLD");
                String songTitle =
                        jsonChildNode.getString("TITLE");
                String songArtist =
                        jsonChildNode.getString("ARTIST");
                String songCountry =
                        jsonChildNode.getString("COUNTRY");
                String songCompany =
                        jsonChildNode.getString("COMPANY");
                String songPrice =
                        jsonChildNode.getString("PRICE");
                String songYear =
                        jsonChildNode.getString("YEAR");
               newItemObject = new ItemObject(songTitle, songYear, songArtist);

                jsonObject.add(newItemObject);

                originCountry.add(songCountry);
                Double dprice = Double.parseDouble(songPrice);
                if (dprice > maxPrice) {
                    maxPrice = dprice;
                    price = songPrice;
                    titleEx = songTitle;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
        return jsonObject;
    }

    }


