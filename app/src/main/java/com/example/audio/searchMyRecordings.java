package com.example.audio;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class searchMyRecordings extends AppCompatActivity {

    ArrayList keyValues = new ArrayList<String>(10);
    ArrayList objectValues = new ArrayList<String>(10);
    private int arraycounter = 0;

    Button playRand = null;

    private static final String LOG_TAG = "AudioRecording";
    //TO-DO: import information from login screen to sort database shenanigans

    private String username;
    private MediaPlayer player = null;
    //
    private DatabaseReference mDatabase;
// ...

    FirebaseDatabase database;
    DatabaseReference myRef;
    FirebaseStorage storage;
    StorageReference storageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_my_recordings);

        username = "testUser";


        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("users/" + username + "/audios");

        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();

        downloadFiles();
    }

    @SuppressLint("SetTextI18n")
    public void leftButton(View v){
    downloadFiles();
        if (arraycounter > 0) {
            arraycounter--;
        } else {
            Toast.makeText(getApplicationContext(), "You are at the beginning of your audio recordings!", Toast.LENGTH_LONG).show();
        }

        TextView text = (TextView) findViewById(R.id.nameOfSelectedAudio);
        text.setText("Name of selected audio:       "+ keyValues.get(arraycounter).toString());

    }
    @SuppressLint("SetTextI18n")
    public void rightButton(View v){
        downloadFiles();
        if (objectValues.size()-1 > arraycounter) {
            arraycounter++;
        } else {
            Toast.makeText(getApplicationContext(), "You are at the end of your audio recordings!", Toast.LENGTH_LONG).show();
        }

        TextView text = (TextView) findViewById(R.id.nameOfSelectedAudio);
        text.setText("Name of selected audio: "+ keyValues.get(arraycounter).toString());
    }

    private void downloadFiles(){
        //Make sure variable myRef is set to account after debugging
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                clearArrayLists();
                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                    String childKey = childSnapshot.getKey();
                    Object childValue = childSnapshot.getValue();
                    // Do something with childKey and childValue


                    keyValues.add(childKey);
                    objectValues.add(childValue.toString());


                    //key value is the name of the file,


                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle error
                Log.d(LOG_TAG, "on data change method was cancelled");
            }
        });

    }


    public void playAudio(View v){

        Uri dfile = Uri.fromFile(new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)+"/download.mp4"));
        player = new MediaPlayer();
        StorageReference audioRef = storageRef.child("testUser").child("audios").child("test.mp4");
        Log.d(LOG_TAG, audioRef.toString());
        audioRef.getFile(dfile).addOnSuccessListener(taskSnapshot -> Log.d(LOG_TAG,"download successful")).addOnFailureListener(exception -> Log.d(LOG_TAG,"download failed"));
        try {
            player.setDataSource(dfile.getPath());
            player.prepare();
            player.start();
        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare() failed");
        }

    }
    public void stpoAudio(View v){
        player.release();
        player = null;
    }

    public void backButton(View v){
        Intent intent = new Intent(getBaseContext(), User_Profile.class);
        startActivity(intent);
    }

    private void clearArrayLists(){
        keyValues.clear();
        objectValues.clear();
    }
}
