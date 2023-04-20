package com.millivalley.qiblaapp.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.millivalley.qiblaapp.R;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements SensorEventListener {
    private String TAG = "wow123";
    private ImageView campus_img, flag_image, arrow, kaba_image;
    private  SensorManager manager;
    private Vibrator v;
    private  Sensor temperature;
    private float current_degree, current_degree2, current_degree3;
    private ExtendedFloatingActionButton feb;
    private TextView city_text, country_text, degree_text;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private double qiblaDirection = 0.0;
    private float bearTo = 0.0f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        connections();
        clicks();
        getLastLocation();
    }
    private void clicks() {
        flag_image.setOnClickListener(view -> {
            startActivity(new Intent(MainActivity.this, LanguageActivity.class));
            finish();
        });

        feb.setOnClickListener(view -> startActivity(new Intent(MainActivity.this, Active_InactiveActivity.class)));
    }

    private void connections() {
        city_text = findViewById(R.id.city_text);
        country_text = findViewById(R.id.country_text);
        flag_image = findViewById(R.id.flag_image);
        feb = findViewById(R.id.feb);
        arrow = findViewById(R.id.arrow);
        campus_img = findViewById(R.id.campus_img1);
        kaba_image = findViewById(R.id.kaba_image);
        degree_text = findViewById(R.id.degree_text);
        manager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

//        temperature = manager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);

    }

    @Override
    protected void onResume() {
        super.onResume();
        manager.registerListener(this, manager.getDefaultSensor(Sensor.TYPE_ORIENTATION), SensorManager.SENSOR_DELAY_GAME);
//        manager.registerListener(this, temperature, SensorManager.SENSOR_DELAY_FASTEST);
    }

    @Override
    protected void onPause() {
        super.onPause();
        manager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if (sensorEvent.sensor.getType() == Sensor.TYPE_ORIENTATION) {

            float degree2 = Math.round(sensorEvent.values[0]);
            RotateAnimation animation2 = new RotateAnimation(current_degree2, -degree2, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            animation2.setDuration(120);
            animation2.setFillAfter(true);
            arrow.startAnimation(animation2);
            current_degree2 = -degree2 + bearTo;
            Log.d(TAG, "onSensorChanged: current_degree 2: " + current_degree2);

            if (current_degree2 <= 5 && current_degree2 >= -1) {
                arrow.setImageResource(R.drawable.arrow_green_new);
                Log.d(TAG, "onSensorChanged: green arrow apply");
// Vibrate for 500 milliseconds
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                    v.vibrate(VibrationEffect.createOneShot(5000, VibrationEffect.DEFAULT_AMPLITUDE));
//                } else {
//                    deprecated in API 26
                    v.vibrate(5000);
//                }
            } else {
                arrow.setImageResource(R.drawable.arrow_white_new);
//                arrow.setRotation(92);
            }


            float degree = Math.round(sensorEvent.values[0]);
            Log.d(TAG, "onSensorChanged: current_degree: " + current_degree);
            Log.d(TAG, "onSensorChanged: -degree: " + (-degree));
            RotateAnimation animation = new RotateAnimation(current_degree, -degree, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            animation.setDuration(120);
            animation.setFillAfter(true);
            campus_img.startAnimation(animation);
            current_degree = -degree;

            float degree3 = Math.round(sensorEvent.values[0]);
            RotateAnimation animation3 = new RotateAnimation(current_degree3, -degree3, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            animation3.setDuration(120);
            animation3.setFillAfter(true);
            current_degree3 = -degree3 + bearTo;
            kaba_image.startAnimation(animation3);

            float l = arrow.getRotation();
            Log.d(TAG, "onSensorChanged:long duration  " + l);


        }

//        if (sensorEvent.sensor.getType() == Sensor.TYPE_AMBIENT_TEMPERATURE) {
//            float temperature = sensorEvent.values[0];
//            Log.d(TAG, "onSensorChanged: temperature:"+temperature);
//        }else{
//            Log.d(TAG, "onSensorChanged: else");
//        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    private void getLastLocation() {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(location -> {
                if (location != null) {
                    try {
                        Geocoder geocoder = new Geocoder(MainActivity.this, Locale.getDefault());
                        List<Address> addresses = null;
                        addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                        Log.d(TAG, "getLastLocation: " + addresses.get(0).getAddressLine(0));

//                        find city and country name
                        String cityName = addresses.get(0).getLocality();
                        String countryName = addresses.get(0).getCountryName();

                        country_text.setText(countryName);
                        city_text.setText(cityName);

                        Log.d(TAG, "getLastLocation: cityName:" + cityName);
                        Log.d(TAG, "getLastLocation: countryName:" + countryName);
//
                        // Calculate the Qibla direction
//                        double latitude = location.getLatitude();
//                        double longitude = location.getLongitude();
                        Log.d(TAG, "getLastLocation: lat:" + location.getLatitude());
                        Log.d(TAG, "getLastLocation: long:" + location.getLongitude());
                        Log.d(TAG, "getLastLocation: altitude:" + location.getLongitude());

                        double latitude = location.getLatitude();
                        double longitude = location.getLongitude();
                        double altitude = location.getAltitude();

//                        double latitude = 31.582045;
//                        double latitude =  51.509865;
//                        double longitude = 74.329376;
//                        double longitude = 39.826181;
//                        double altitude = 217;
//                        double altitude = 11;
//                        double qiblaLatitude = 21.422524; // Latitude of the Kaaba in Mecca
//                        double qiblaLatitude = 51.509865; // Latitude of the Kaaba in Mecca
//                        double qiblaLongitude = 39.826181; // Longitude of the Kaaba in Mecca
//                        double qiblaLongitude = -0.118092; // Longitude of the Kaaba in Mecca
//
                        Location userLoc = new Location("service Provider");
                        //get longitudeM Latitude and altitude of current location with gps class and  set in userLoc
                        userLoc.setLongitude(longitude);
                        userLoc.setLatitude(latitude);
                        userLoc.setAltitude(altitude);

                        Location destinationLoc = new Location("service Provider");
                        destinationLoc.setLatitude(21.422487); //kaaba latitude setting
                        destinationLoc.setLongitude(39.826206); //kaaba longitude setting
                        bearTo = userLoc.bearingTo(destinationLoc);
                        if (bearTo < 0) {
                            bearTo = bearTo + 360;
                            //bearTo = -100 + 360  = 260;
                        }
                        Log.d(TAG, "getLocationDegree:qiblaDirection " + bearTo);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(this, "Turn On Location", Toast.LENGTH_SHORT).show();
//                    startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                }
            });
        } else {
            Toast.makeText(this, "Permission Not Granted by User", Toast.LENGTH_SHORT).show();
        }
    }
}

//    public void setLanguage (String languageCode) {
//        Log.d("wow123", "setLanguage: "+languageCode);
//        Resources resources = this.getResources();
//        Configuration configuration = resources.getConfiguration();
//        Locale locale = new Locale (languageCode);
//        Locale.setDefault(locale);
//        configuration.setLocale (locale);
//        resources.updateConfiguration (configuration, resources.getDisplayMetrics());
//    }

//    private void getLocationDegree() {
//// Get the user's location
//        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
//            ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION} ,1);
//            return ;
//        }
//        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
////// Calculate the Qibla direction
////        double latitude = location.getLatitude();
////        double longitude = location.getLongitude();
////        double qiblaLatitude = 21.422524; // Latitude of the Kaaba in Mecca
////        double qiblaLongitude = 39.826181; // Longitude of the Kaaba in Mecca
////        double longitudeDifference = qiblaLongitude - longitude;
////        double y = Math.sin(longitudeDifference) * Math.cos(qiblaLatitude);
////        double x = Math.cos(latitude) * Math.sin(qiblaLatitude) - Math.sin(latitude) * Math.cos(qiblaLatitude) * Math.cos(longitudeDifference);
////        double qiblaDirection = (Math.toDegrees(Math.atan2(y, x)) + 360) % 360;
////        Log.d(TAG, "getLocationDegree:qiblaDirection "+qiblaDirection);
//    }
//    private void getTemprature() {
////        manager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
//        sensor = manager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
//        sensor2 = manager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
//        SensorEventListener sensorEventListener = new SensorEventListener() {
//            @Override
//            public void onSensorChanged(SensorEvent event) {
//                if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
//                    float[] magnetic = event.values;
//                    Log.d(TAG, "onSensorChanged: " + magnetic[0]);
//                }
//                if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
//                    float[] gravity = event.values;
//                }
//            }
//
//            @Override
//            public void onAccuracyChanged(Sensor sensor, int i) {
//
//            }
//        };
//    }
//                        double longitudeDifference = qiblaLongitude - longitude;
//                        double y = Math.sin(longitudeDifference) * Math.cos(qiblaLatitude);
//                        double x = Math.cos(latitude) * Math.sin(qiblaLatitude) - Math.sin(latitude)
//                                * Math.cos(qiblaLatitude) * Math.cos(longitudeDifference);
//                        qiblaDirection = (Math.toDegrees(Math.atan2(y, x)) + 360) % 360;
//                        Log.d(TAG, "getLocationDegree:qiblaDirection "+qiblaDirection);
//                        location_text.setText("" + addresses.get(0).getAddressLine(0));