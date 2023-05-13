package com.example.audio;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class User_Profile extends AppCompatActivity {


    private static final String LOG_TAG = "AudioRecording";
    //Buttons on the userprofile.xml page
    private Button familyGroupsXML;
    private Button joinFamilyGroupXML;
    private Button recordNowXML;
    private Button createFamilyGroupXML;
    private Button signOutXML;
    private Button searchMyRecordingsXML;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.userprofile);

        searchMyRecordingsXML = findViewById(R.id.searchMyRecordingsButton);
        familyGroupsXML = findViewById(R.id.logInButton);
        joinFamilyGroupXML = findViewById(R.id.joinAFamilyGroupButton);
        recordNowXML = findViewById(R.id.recordNowButton);
        createFamilyGroupXML = findViewById(R.id.createAFamilyGroupButton);
        signOutXML = findViewById(R.id.signOutButton);
        familyGroupsXML.setOnClickListener(v -> {
            Intent intent = new Intent(getBaseContext(), viewFamilyGroups.class);
            startActivity(intent);

        });
        joinFamilyGroupXML.setOnClickListener(v -> {
            Intent intent = new Intent(getBaseContext(), joinFamilyGroup.class);
            startActivity(intent);

        });

        recordNowXML.setOnClickListener(v -> {
            Intent intent = new Intent(getBaseContext(), MainActivity.class);
            startActivity(intent);

        });

        createFamilyGroupXML.setOnClickListener(v -> {
            Intent intent = new Intent(getBaseContext(), Create_A_Family.class);
            startActivity(intent);

        });
        signOutXML.setOnClickListener(v -> {
            resetUser();
            Intent intent = new Intent(getBaseContext(), LoginScreen.class);
            startActivity(intent);
        });
        searchMyRecordingsXML.setOnClickListener(v -> {
            Intent intent = new Intent(getBaseContext(), searchMyRecordings.class);
            startActivity(intent);
        });


    }

    public void switchToDataBaseActivity(View v) {
        Intent intent = new Intent(getBaseContext(), Database.class);
        startActivity(intent);

    }

    private void resetUser(){
        ((MyApplication) this.getApplication()).setUserName("");
    }
}



