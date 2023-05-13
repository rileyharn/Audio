package com.example.audio;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class viewFamilyGroups extends AppCompatActivity {
    private Button back = null;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.family_group);
        back = findViewById(R.id.backButton2);
        back.setOnClickListener(view -> {
            Intent intent = new Intent(getBaseContext(), User_Profile.class);
            startActivity(intent);
        });
    }
}
