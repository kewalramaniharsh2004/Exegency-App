package com.example.trial1;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class Server extends AppCompatActivity {

    private String name, phone, message;
    private double latitude, longitude;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_server);

        // Retrieve data from intent extras
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            name = extras.getString("name");
            phone = extras.getString("phone");
            message = extras.getString("message");
            latitude = extras.getDouble("latitude");
            longitude = extras.getDouble("longitude");
        }

        // Set the text of TextView with id textViewName to the value of name
        TextView textViewName = findViewById(R.id.textView5);
        textViewName.setText(name +" needs help.");

        TextView textViewName1 = findViewById(R.id.textView4);
        textViewName1.setText("Phone: "+ phone);

        TextView textViewName2 = findViewById(R.id.textView);
        textViewName2.setText("Longitude: " + longitude);

        TextView textViewName3 = findViewById(R.id.textView9);
        textViewName3.setText("Latitude: " + latitude);

        TextView textViewName4 = findViewById(R.id.textView7);
        textViewName4.setText(message);

        // Set up click listener for the button
        Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle button click to navigate back to MainActivity
                Intent intent = new Intent(Server.this, MainActivity.class);
                startActivity(intent);
                finish(); // Finish the current activity
            }
        });
    }
}
