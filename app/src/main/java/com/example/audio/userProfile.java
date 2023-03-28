package com.example.audio;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class userProfile extends AppCompatActivity {
    private Button recordNowButton_profilePage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_profile);

    }
        public void openActivity2(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        }
    }