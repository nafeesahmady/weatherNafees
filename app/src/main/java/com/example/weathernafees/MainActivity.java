package com.example.weathernafees;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    EditText editText;
    Button button;
    ImageView img;
    TextView city, temp, humidity, windSpeed, dateTime, prevCity, weatherType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // id find for all views
        editText = findViewById(R.id.searchCity);
        button = findViewById(R.id.button);
        temp = findViewById(R.id.temp);
        dateTime = findViewById(R.id.dateTime);
        weatherType = findViewById(R.id.weatherType);
        img = findViewById(R.id.img);
        prevCity = findViewById(R.id.prevCity);
        city = findViewById(R.id.city);
        windSpeed = findViewById(R.id.windSpeed);
        humidity = findViewById(R.id.humidity);
        // check existance of previous City search
        checkexist();
        // listener on button for showing weather information and also for save data in shared preference
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = getSharedPreferences("city", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("cityname", editText.getText().toString());
                editor.apply();
                // showing weather information
                findWeather();
            }
        });

    }

    // existance full code
    private void checkexist() {
        SharedPreferences sharedPreferences = getSharedPreferences("city", MODE_PRIVATE);
        if (sharedPreferences.contains("cityname")) {
            editText.setText(sharedPreferences.getString("cityname", "Delhi"));
            prevCity.setText(sharedPreferences.getString("cityname", ""));
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    button.performClick();
                }
            }, 1);
        } else {
            editText.setText("Siwan");
            button.performClick();
            Toast.makeText(this, "Previous Record Not found", Toast.LENGTH_SHORT).show();
        }
    }

    // weather check full code
    public void findWeather() {
        // get city name from edit text
        String city1 = editText.getText().toString();

        // putting weather information url in code
        String url = "https://api.openweathermap.org/data/2.5/weather?q=" + city1 + "&appid=e5ab77f3ce167dc95bf06938ed39ce74";

        // req api for weather information
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //coading api
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    // find country
                    JSONObject object1 = jsonObject.getJSONObject("sys");
                    String country_find = object1.getString("country");

                    //find city
                    String city_find = jsonObject.getString("name");
                    city.setText(country_find + " : " + city_find);

                    //find temprature
                    JSONObject object2 = jsonObject.getJSONObject("main");
                    String temprature_find = object2.getString("temp");
                    temp.setText(temprature_find + "F");

                    //get image
                    JSONArray jsonArray = jsonObject.getJSONArray("weather");
                    JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                    String imgage = jsonObject1.getString("icon");
                    Picasso.get().load("http://openweathermap.org/img/wn/" + imgage + "@2x.png").into(img);

                    //find date and time
                    Calendar calendar = Calendar.getInstance();
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy \n HH:mm:ss");
                    String date1 = simpleDateFormat.format(calendar.getTime());
                    dateTime.setText(date1);

                    //find humidity
                    JSONObject object3 = jsonObject.getJSONObject("main");
                    int humidity_find = object3.getInt("humidity");
                    humidity.setText(humidity_find + " %");

                    // find wind speed
                    JSONObject object4 = jsonObject.getJSONObject("wind");
                    String wind_find = object4.getString("speed");
                    windSpeed.setText(wind_find + " km/h");

                    //weather Type
                    JSONArray jsonArray1 = jsonObject.getJSONArray("weather");
                    JSONObject jsonObject2 = jsonArray.getJSONObject(0);
                    String des = jsonObject2.getString("description");
                    weatherType.setText("Weather Type : " + des);

                } catch (JSONException e) {
                    // we can also write code for handle error
                    throw new RuntimeException(e);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        requestQueue.add(stringRequest);
    }

}