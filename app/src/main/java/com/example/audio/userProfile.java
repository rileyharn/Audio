package com.example.audio;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Intent;
import android.view.View;
import android.widget.Button;

public class userProfile {
    public void openRecordingPage(View v){
        startActivity(new Intent(user_profile.this, MainActivity.class));
    }

}
