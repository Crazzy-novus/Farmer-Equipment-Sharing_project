package com.example.framerfriend;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;

import android.app.DatePickerDialog;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;

import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;


import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.UploadTask;


import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;


// Main class
public class UserDetailsActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    /* ----------- Declaration  function code section -------------*/

    private static final int REQUEST_IMAGE_PICKER = 1;
     private ImageView imageView_iv;
     MaterialAutoCompleteTextView userId_tv;

    private EditText userDateOfBirth_et, userName_et, sureName_et, phno_et;
    Spinner gender_sp;
    private int year, month, day;
    FusedLocationProviderClient fusedLocationProviderClient;

    private FirebaseFirestore db;
    private EditText latitude_et, longitude_et, city_et, address_et, country_et;
    Button getLocation_bt, submit_bt;
    private final static int REQUEST_CODE = 100;
    Bitmap bitmap;
    Uri imageUri;
    private String _userName_, _sureName_, _userDOB_, _gender_, _latitude_, _longitude_, _city_, _address_, _country_, _phno_, _userId_, _imageUrl_;
  

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details);

        /* ----------- Initialization  code section -------------*/


        // To get userId from Login screen
        SharedPreferences sharedPreferences = getSharedPreferences("my_preferences", Context.MODE_PRIVATE);
        _userId_ = sharedPreferences.getString("userId", null);

        // To upload image into screen
        imageView_iv = findViewById(R.id.imageView);

        // Fire base connection
        db = FirebaseFirestore.getInstance();

        // calender pop up
        userDateOfBirth_et = findViewById(R.id.et_date);
        gender_sp = findViewById(R.id.et_gender);

        // Location attributes
        latitude_et = findViewById(R.id.et_latitude);
        longitude_et = findViewById(R.id.et_longitude);
        address_et = findViewById(R.id.et_address);
        city_et = findViewById(R.id.et_city);
        country_et = findViewById(R.id.et_country);
        getLocation_bt = findViewById(R.id.location_bt);

        // general user information
        userId_tv = findViewById(R.id.et_userid);
        userId_tv.setText(_userId_);
        userName_et = findViewById(R.id.et_username);
        sureName_et = findViewById(R.id.et_surname);
        submit_bt = findViewById(R.id.submit_bt);
        phno_et = findViewById(R.id.et_phnono);

        // Drop down menu
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.genderList, android.R.layout.simple_spinner_item);

        /* =============== Action part of the code begins ===============*/

        // to create drop down menu
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        gender_sp.setAdapter(adapter);
        gender_sp.setOnItemSelectedListener(this);

        // to create calender pop up
        Calendar calendar = Calendar.getInstance();

        /* ----------- on clock listener function code section -------------*/

        // To open calender pop up
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

        // To get current location of the user
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        getLocation_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getLastLocation();
            }
        });

        // To upload details into database
        submit_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                _userName_ = userName_et.getText().toString();
                _sureName_ = sureName_et.getText().toString();
                _phno_ = phno_et.getText().toString();
                _userDOB_ = userDateOfBirth_et.getText().toString();
                _latitude_ = latitude_et.getText().toString();
                _longitude_ = longitude_et.getText().toString();
                _city_ = city_et.getText().toString();
                _country_ = country_et.getText().toString();
                _address_ = address_et.getText().toString();





                FirebaseStorage.getInstance().getReference("users/user_images/"+ _userId_).putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if (task.isSuccessful()) {
                            task.getResult().getStorage().getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {
                                    if (task.isSuccessful()) {
                                        _imageUrl_ = task.getResult().toString();
                                        Map<String, Object> productHolder = new HashMap<>();
                                        productHolder.put("ProfilePhotoURL",_imageUrl_);
                                        productHolder.put("Name",_userName_);
                                        productHolder.put("SureName", _sureName_);
                                        productHolder.put("PhoneNumber", _phno_);
                                        productHolder.put("Gender", _gender_);
                                        productHolder.put("DateOfBirth", _userDOB_);



                                        Map<String, Object> addressMap = new HashMap<>();

                                        addressMap.put("city", _city_);
                                        addressMap.put("country", _country_);
                                        addressMap.put("latitude", _latitude_);
                                        addressMap.put("longitude", _longitude_);
                                        addressMap.put("addressLine", _address_);

                                        productHolder.put("Address", addressMap);

                                        db.collection("product_holders")
                                                .document(_userId_)
                                                .set(productHolder)
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void unused) {
                                                        Toast.makeText(UserDetailsActivity.this, "Data Inserted Successfully"+_userId_, Toast.LENGTH_SHORT).show();
                                                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                                        startActivity(intent);
                                                        finish();
                                                    }
                                                }).addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Toast.makeText(UserDetailsActivity.this, "Data not inserted", Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                    }
                                    Toast.makeText(UserDetailsActivity.this, "Image Inserted Successfully"+_imageUrl_, Toast.LENGTH_SHORT).show();
                                }
                            });

                        }
                        else {
                            Toast.makeText(UserDetailsActivity.this, "Data Inserted Failed"+_imageUrl_, Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(UserDetailsActivity.this, "Image not inserted", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        // To upload image into screen

        imageView_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openImagePicker();
            }
        });
    }


    /* Function used to get current location*/
    private void getLastLocation() {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {

                    if (location != null) {

                        Geocoder geocoder = new Geocoder(UserDetailsActivity.this, Locale.getDefault());
                        List<Address> addresses;

                        try {
                            addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                            assert addresses != null;
                            if (!addresses.isEmpty()) {
                                latitude_et.setText(String.valueOf(addresses.get(0).getLatitude()));
                                longitude_et.setText(String.valueOf(addresses.get(0).getLongitude()));
                                address_et.setText(addresses.get(0).getAddressLine(0));
                                city_et.setText(addresses.get(0).getLocality());
                                country_et.setText(addresses.get(0).getCountryName());
                            } else {
                                Toast.makeText(UserDetailsActivity.this, "Location not found", Toast.LENGTH_SHORT).show();
                            }
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }

                    }
                }
            });

        } else {

            askPermission();
        }
    }

    /* Function used to get current location*/
    private void askPermission() {

        ActivityCompat.requestPermissions(UserDetailsActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
    }

    /* Function used to get current location*/
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == REQUEST_CODE) {

            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                getLastLocation();
            } else {
                Toast.makeText(UserDetailsActivity.this, "Please provide the required permission", Toast.LENGTH_SHORT).show();
            }
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    /* Function used to get date of birth from user*/
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        _gender_ = adapterView.getItemAtPosition(i).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    // To to get image from device

    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        ActivityCompat.startActivityForResult(this, intent, REQUEST_IMAGE_PICKER, null);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE_PICKER && resultCode == RESULT_OK && data != null) {
            imageUri = data.getData();
            try {

                // TO get image in image view
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                imageView_iv.setImageBitmap(bitmap);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}