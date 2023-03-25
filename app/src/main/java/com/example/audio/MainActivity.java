package com.example.audio;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.pm.PackageManager;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Toast;
import android.Manifest;

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
        mediaRecorder = new MediaRecorder();
        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO},0);

        }
        else {
            mediaRecorder = new MediaRecorder();
            mediaRecorder.reset();
            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
            mediaRecorder.setOutputFile(getFilePath());
        }
        try {
            mediaRecorder.prepare();
        } catch (IOException e) {
            // Log.e(LOG_TAG, "prepare() failed");
            throw new RuntimeException(e);
        }
    }

    private void requestAudioPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {

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
            toast.show();
        }
    }

    public void stopMicrophone(View view)
    {
        if(microphoneRunning)
        {
            mediaRecorder.stop();
            microphoneRunning = false;
            mediaRecorder.reset();
            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
            mediaRecorder.setOutputFile(getFilePath());
            Toast toast = Toast.makeText( getApplicationContext(),"Stop Microphone", Toast.LENGTH_LONG);
            toast.show();
        }
    }

    private String getFilePath() {

        String filepath = getFilesDir().getPath();
        File file = new File(filepath, "MediaRecorderSample");

        if (!file.exists())
            file.mkdirs();

        return (file.getAbsolutePath() + "/" + "test.mp3");
    }
}