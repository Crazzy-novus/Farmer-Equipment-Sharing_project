package com.example.framerfriend;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;

import android.app.DatePickerDialog;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;


import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

// Main class
public class UserDetailsActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    EditText userDateOfBirth_et;
    Spinner gender_sp;
    int year, month, day;
    FusedLocationProviderClient fusedLocationProviderClient;
    EditText latitude, longitude, city, address, country;
    Button getLocation_bt;
    private final static int REQUEST_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details);



        // to include gps api
        latitude = findViewById(R.id.et_latitude);
        longitude = findViewById(R.id.et_longitude);
        address = findViewById(R.id.et_address);
        city = findViewById(R.id.et_city);
        country = findViewById(R.id.et_country);
        getLocation_bt = findViewById(R.id.location_bt);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        getLocation_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getLastLocation();
            }
        });


        // to create drop down menu
        gender_sp = findViewById(R.id.et_gender);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.genderList,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        gender_sp.setAdapter(adapter);
        gender_sp.setOnItemSelectedListener(this);



        // to create calender pop up
        Calendar calendar = Calendar.getInstance();
        userDateOfBirth_et = findViewById(R.id.et_date);
        userDateOfBirth_et.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                day = calendar.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(UserDetailsActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        userDateOfBirth_et.setText(SimpleDateFormat.getDateInstance().format(calendar.getTime()));
                    }
                }, year, month, day);
                datePickerDialog.show();

            }
        });
    }



    /* Function used to get current location*/
    private void getLastLocation() {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){

            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {

                    if ( location != null) {

                        Geocoder geocoder =new Geocoder(UserDetailsActivity.this, Locale.getDefault());
                        List<Address> addresses;

                        try {
                            addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                            if (!addresses.isEmpty()) {
                                latitude.setText(String.valueOf(addresses.get(0).getLatitude()));
                                longitude.setText(String.valueOf(addresses.get(0).getLongitude()));
                                address.setText(addresses.get(0).getAddressLine(0));
                                city.setText(addresses.get(0).getLocality());
                                country.setText(addresses.get(0).getCountryName());
                            }
                            else {
                                Toast.makeText(UserDetailsActivity.this, "Location not found", Toast.LENGTH_SHORT).show();
                            }
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }

                    }
                }
            });

        }
        else {

            askPermission();
        }
    }
    /* Function used to get current location*/
    private void askPermission() {

        ActivityCompat.requestPermissions(UserDetailsActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_CODE);
    }

    /* Function used to get current location*/
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == REQUEST_CODE) {

            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                getLastLocation();
            }
            else {
                Toast.makeText(UserDetailsActivity.this, "Please provide the required permission", Toast.LENGTH_SHORT).show();
            }
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
    /* Function used to get date of birth from user*/
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        String text = adapterView.getItemAtPosition(i).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        
    }
}