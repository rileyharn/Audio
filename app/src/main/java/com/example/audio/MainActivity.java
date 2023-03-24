package com.example.audio;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.pm.PackageManager;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    //Variables for MediaRecorder (recording audio)
    File recordingFile;
    boolean microphoneRunning = false;
    boolean microphonePermission = false;
    MediaRecorder mediaRecorder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        requestAudioPermissions();
        MediaRecorder mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_2_TS);
        mediaRecorder.setOutputFile(recordingFile.getAbsolutePath());
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
        try {
            mediaRecorder.prepare();
        } catch (IOException e) {
            // Log.e(LOG_TAG, "prepare() failed");
            throw new RuntimeException(e);
        }


    }

    private void requestAudioPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.) != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.RECORD_AUDIO)) {
                Toast.makeText(this, "Please grant permissions to record audio", Toast.LENGTH_LONG).show();
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, 1);
            }
        } else {
            microphonePermission = true;
        }
    }

    public void startMicrophone(View view)
    {

        if(!microphoneRunning)
        {
            mediaRecorder.start();
            microphoneRunning = true;
            Toast toast = Toast.makeText( getApplicationContext(),"Start Microphone", Toast.LENGTH_LONG);
        }
    }

    public void stopMicrophone(View view)
    {
        if(microphoneRunning)
        {
            mediaRecorder.stop();
            microphoneRunning = false;
            Toast toast = Toast.makeText( getApplicationContext(),"Stop Microphone", Toast.LENGTH_LONG);
        }
    }

    public void releaseMicrophone()
    {
        mediaRecorder.release();
    }



}