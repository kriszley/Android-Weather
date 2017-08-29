package com.example.krisley3094.launchpad_weather;

import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.HttpAuthHandler;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {

    private TextView tempText;
    private String temp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tempText = (TextView)findViewById(R.id.tempText);

    }

    public void showWeather(View view){
        new GetWeather().execute();
    }

    private class GetWeather extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(getApplicationContext(), "Json Data is downloading", Toast.LENGTH_SHORT).show();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            String url_str = "http://api.openweathermap.org/data/2.5/weather?id=4699066&appid=b400a9e7088294d1fcde3dfeda6e794b";
            String jsonStr = null;

            try {
                URL url = new URL(url_str);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                // read the response
                InputStream in = new BufferedInputStream(conn.getInputStream());

                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                StringBuilder sb = new StringBuilder();

                String line;
                try {
                    while ((line = reader.readLine()) != null) {
                        sb.append(line).append('\n');
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        in.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                jsonStr = sb.toString();

                Log.e("HTTP response", "Response from url : " + jsonStr);


                if (jsonStr != null) {
                    try {
                        JSONObject jsonObj = new JSONObject(jsonStr);

                        // Getting JSON Array node
                        JSONObject main = jsonObj.getJSONObject("main");
                        temp = main.getString("temp");

                    } catch (final JSONException e) {
                    }
                }

            } catch (IOException e) {
                Toast.makeText(getApplicationContext(), "ERROR", Toast.LENGTH_SHORT).show();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            double tempK = Double.parseDouble(temp);
            double tempC = tempK - 273.15;
            DecimalFormat df = new DecimalFormat("#.##");
            tempC = Double.valueOf(df.format(tempC));

            tempText.setText(String.valueOf(tempC) + "Celsius");

        }

    }

}
