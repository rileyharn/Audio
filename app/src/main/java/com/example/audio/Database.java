package com.example.audio;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.firebase.auth.FirebaseAuth;
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
import java.util.Objects;
import java.util.Random;

public class Database extends AppCompatActivity {
    ArrayList keyValues = new ArrayList<String>(10);
    ArrayList objectValues = new ArrayList<String>(10);
    ArrayList downloadFileName = new ArrayList<String>();
    ArrayList uriFiles = new ArrayList<Uri>();
    private int arraycounter = 0;

    Button playRand = null;

    private static final String LOG_TAG = "AudioRecording";
    //TO-DO: import information from login screen to sort database shenanigans
    private FirebaseAuth mAuth;
    GoogleSignInClient mGoogleSignInClient;
    GoogleSignInAccount account;

    private String emailPath;
    private File outputFile;
    private Uri uriFile;
    private MediaPlayer player = null;
    //
    private DatabaseReference mDatabase;
// ...

    FirebaseDatabase database;
    DatabaseReference myRef;
    FirebaseStorage storage;
    StorageReference storageRef;
    File playDir;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.database_test);
        //emailpath would get the last used email/pull that information from a different activity
        playRand = findViewById(R.id.playRand);
        playRand.setOnClickListener(view -> {
            playRand(view);
        });
        emailPath = "testUser";

        mDatabase = FirebaseDatabase.getInstance().getReference();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("users/audio");

        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();


        downloadFiles();


    }

    public void debugClick(View v){
        Log.d(LOG_TAG, "entering debugClick mehtod");
        EditText edit =  (EditText) findViewById(R.id.DebugTextBox);
        EditText fileName = (EditText) findViewById(R.id.fileName);
        Toast.makeText(getApplicationContext(), edit.getText(), Toast.LENGTH_LONG).show();// Write a message to the database


        myRef.child(fileName.getText().toString()).setValue("audios/" +emailPath +"/" + fileName.getText().toString());

        Log.d(LOG_TAG, "exiting debugClick mehtod");
        downloadFiles();
    }

    public void playRand(View v){
        playDir = ((MyApplication) this.getApplication()).getCurDir();
        File[] files = playDir.listFiles();
        Random rand = new Random();

        File playFile = files[rand.nextInt(files.length)];
        player = new MediaPlayer();
        try {
            player.setDataSource(playFile.getAbsolutePath());
            player.prepare();
            player.start();
        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare() failed");
        }
    }

    public void uploadFiles(){

        //code for setting uploadtask using email path


    }
    public void downloadFiles(){
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
                    downloadFileName.add(emailPath + "/" + childKey);

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

    public void playButton(View v){
        Uri dfile = Uri.fromFile(new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)+"/download.mp4"));
        player = new MediaPlayer();
        StorageReference audioRef = storageRef.child("audios/" + objectValues.get(arraycounter) + ".mp4");
        Log.d(LOG_TAG,audioRef.toString());
        Log.d(LOG_TAG,dfile.toString());
        audioRef.getFile(dfile).addOnSuccessListener(taskSnapshot -> Log.d(LOG_TAG,"download successful")).addOnFailureListener(exception -> Log.d(LOG_TAG,"download failed"));
        try {
            player.setDataSource(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)+"/download.mp4");
            player.prepare();
            player.start();
        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare() failed");
        }
    }

    public void leftButton(View v){

        if (arraycounter > 0) {
            arraycounter--;
        } else {
            Toast.makeText(getApplicationContext(), "You are at the beginning of your audio recordings!", Toast.LENGTH_LONG).show();
        }

        TextView text = (TextView) findViewById(R.id.downloadedFileNameTextView);
        text.setText(objectValues.get(arraycounter).toString());

    }
    public void rightButton(View v){
        if (objectValues.size()-1 > arraycounter) {
            arraycounter++;
        } else {
            Toast.makeText(getApplicationContext(), "You are at the end of your audio recordings!", Toast.LENGTH_LONG).show();
        }

        TextView text = (TextView) findViewById(R.id.downloadedFileNameTextView);
        text.setText(objectValues.get(arraycounter).toString());
    }

    public void backButton(View v){
        player.release();
        Intent intent = new Intent(getBaseContext(), User_Profile.class);
        startActivity(intent);
    }

    private void clearArrayLists(){
        keyValues.clear();
        objectValues.clear();
        downloadFileName.clear();
        uriFiles.clear();
    }

}
