package com.example.audio;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.Manifest;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    //variables for audio recorder
    private static final String LOG_TAG = "AudioRecordTest";
    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;
    private File outputFile = null;
    private File rootPath = null;
    private String [] permissions = {Manifest.permission.RECORD_AUDIO};

    //Media recorder and player object instantiated as null
    private MediaRecorder recorder = null;
    private MediaPlayer player = null;
    //Firebase Storage variables n stuff
    FirebaseStorage storage;
    StorageReference storageRef;
    private UploadTask uploadTask;
    //Misc variables

    private boolean isRecording = false;
    private boolean hasRecorded = false;
    private String username = "testUser";

    //Setting up button variables
    Button recordButton = null;
    Button playButton = null;
    Button stopButton = null;

    //Code to run when screen is opened
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //firebase setup
        storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();

        //setting buttons from view
        recordButton = findViewById(R.id.recordButton);
        playButton = findViewById(R.id.startPlayback);
        stopButton = findViewById(R.id.stopPlayback);

        //getting current user for filesystem
        //TODO: implement transition of username through activities
        rootPath = new File(getExternalFilesDir(null), username);
        if(!rootPath.exists()) {
            rootPath.mkdirs();
        }

        //disable buttons that cannot be used before recording
        playButton.setEnabled(false);
        stopButton.setEnabled(false);

        //setting onClick functions
        recordButton.setOnClickListener(view -> {
            //If a recording is already in progress, stop. If not, start a new recording
            if(isRecording){
                stopRecording();
            }
            else{
                startRecording();
            }
        });
        playButton.setOnClickListener(view -> {
            playButton.setEnabled(false);
            stopButton.setEnabled(true);
            player = new MediaPlayer();
            try {
                player.setDataSource(outputFile.getAbsolutePath());
                player.prepare();
                player.start();
            } catch (IOException e) {
                Log.e(LOG_TAG, "prepare() failed");
            }
        });
        stopButton.setOnClickListener(view -> {
            playButton.setEnabled(true);
            stopButton.setEnabled(false);
            player.release();
            player = null;
        });
    }
    private void startRecording(){
        playButton.setEnabled(false);
        stopButton.setEnabled(false);
        if(player!=null){
            player.release();
            player = null;
        }
        outputFile = new File(rootPath,"test.mp4");
        ActivityCompat.requestPermissions(this, permissions, REQUEST_RECORD_AUDIO_PERMISSION);
        isRecording = true;
        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        recorder.setOutputFile(outputFile.getAbsolutePath());
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
        try {
            recorder.prepare();
        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare() failed");
        }

        recorder.start();
    }
    private void stopRecording() {
        playButton.setEnabled(true);
        stopButton.setEnabled(false);
        recorder.stop();
        recorder.release();
        recorder = null;
        isRecording = false;
    }

    public void setUploadTask(View v){
        StorageReference audioRef = storageRef.child("audios/"+outputFile.getAbsolutePath().toString());
        uploadTask = audioRef.putFile(Uri.fromFile(new File(outputFile.getAbsolutePath())));
        uploadTask.addOnFailureListener(exception -> {
            Log.d(LOG_TAG, "upload task failed");
            Log.d(LOG_TAG, Log.getStackTraceString(null));
        }).addOnSuccessListener(taskSnapshot -> Log.d(LOG_TAG, "upload task successful"));
    }

    }

