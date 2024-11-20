package com.example.trial1;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    private ImageView imageView;
    private ImageButton backButton;
    private Button button1, button2, button3, button4;
    private double latitude, longitude;

    private FusedLocationProviderClient fusedLocationClient;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1001;

    // Firebase variables
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    // User data
    private String userName;
    private String userPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize Firebase
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        imageView = findViewById(R.id.imageView);
        backButton = findViewById(R.id.backButton);
        button1 = findViewById(R.id.button1);
        button2 = findViewById(R.id.button2);
        button3 = findViewById(R.id.button3);
        button4 = findViewById(R.id.button4);

        // Initialize FusedLocationProviderClient
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        // Request location permissions
        requestLocationPermission();

        // Get user's current location
        getLocation();

        // Retrieve user's name and phone number and display as toast
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            mDatabase.child("users").child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        User user = snapshot.getValue(User.class);
                        if (user != null) {
                            userName = user.getName();
                            userPhone = user.getPhone();

                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // Handle database error
                    Toast.makeText(MainActivity.this, "Failed to retrieve user data", Toast.LENGTH_SHORT).show();
                }
            });
        }

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Change image when button 1 is clicked
                imageView.setImageResource(R.drawable.image1);
                Toast.makeText(MainActivity.this, "Near by hospitals", Toast.LENGTH_SHORT).show();
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Change image when button 2 is clicked
                imageView.setImageResource(R.drawable.image3);
                Toast.makeText(MainActivity.this, "Police will arrive and call you soon", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this, Server.class);
                intent.putExtra("name", userName);
                intent.putExtra("phone", userPhone);
                intent.putExtra("latitude", latitude); // Replace latitude with actual latitude value
                intent.putExtra("longitude", longitude); // Replace longitude with actual longitude value
                intent.putExtra("message", "Police");
                startActivity(intent);
            }
        });

        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Change image when button 3 is clicked
                imageView.setImageResource(R.drawable.image2);
                Toast.makeText(MainActivity.this, "Ambulance will arrive and call you soon", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this, Server.class);
                intent.putExtra("name", userName);
                intent.putExtra("phone", userPhone);
                intent.putExtra("latitude", latitude); // Replace latitude with actual latitude value
                intent.putExtra("longitude", longitude); // Replace longitude with actual longitude value
                intent.putExtra("message", "Ambulance");
                startActivity(intent);
            }
        });

        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Change image when button 4 is clicked
                imageView.setImageResource(R.drawable.image4);
                Toast.makeText(MainActivity.this, "Fire Brigade will arrive and call you soon", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this, Server.class);
                intent.putExtra("name", userName);
                intent.putExtra("phone", userPhone);
                intent.putExtra("latitude", latitude); // Replace latitude with actual latitude value
                intent.putExtra("longitude", longitude); // Replace longitude with actual longitude value
                intent.putExtra("message", "Fire Brigade");
                startActivity(intent);
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearUserCredentials();
                Intent intent = new Intent(MainActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });
    }

    // Request location permission
    private void requestLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    // Request location permission result handler
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, get location
                getLocation();
            } else {
                // Permission denied, show a message or handle accordingly
                Toast.makeText(this, "Location permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Get user's current location
    private void getLocation() {
        // Check if permission is granted
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if (location != null) {
                                latitude = location.getLatitude();
                                longitude = location.getLongitude();
                                // Do something with latitude and longitude (e.g., store in a variable)
                            } else {
                                // Handle case where location is null
                                Toast.makeText(MainActivity.this, "Turn on location icon in your device and restart app", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }

    private void clearUserCredentials() {
        SharedPreferences sharedPref = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.clear(); // Clear all stored data
        editor.apply();
    }
}
