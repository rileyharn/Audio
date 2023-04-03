package com.example.audio;

import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Environment;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.view.View;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;
import android.Manifest;

import java.io.File;
import java.io.IOException;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class MainActivity extends AppCompatActivity {
    private Button startBtn, playBtn, stopPlayBtn;
    private MediaRecorder mRecorder;
    private MediaPlayer mPlayer;
    private static final String LOG_TAG = "AudioRecording";
    private static String mFileName = null;
    private UploadTask uploadTask;
    //Cloud storage stuff
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private StorageReference storageRef = storage.getReference();
    private Uri file = Uri.fromFile(new File( Environment.getExternalStorageDirectory() + File.separator
            + Environment.DIRECTORY_DCIM + File.separator + "test.mpeg4"));
    private StorageReference audioRef = storageRef.child("audios/"+file.getLastPathSegment());


    private boolean recording = false;
    public static final int REQUEST_AUDIO_PERMISSION_CODE = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startBtn = findViewById(R.id.recordButton);
        playBtn = findViewById(R.id.startPlayback);
        stopPlayBtn = findViewById(R.id.stopPlayback);
        playBtn.setEnabled(false);
        stopPlayBtn.setEnabled(false);
        mFileName =  Environment.getExternalStorageDirectory() + File.separator
                + Environment.DIRECTORY_DCIM + File.separator + "test.mpeg4";

    }

    public void setUploadTask(View view)
    {
        uploadTask = audioRef.putFile(file);
        Toast.makeText(getApplicationContext(), "UploadStarting", Toast.LENGTH_LONG).show();

    }
    public void startRecording(View view){
        Log.d(LOG_TAG,"Button pressed");
        if(CheckPermissions()) {
            if(!recording) {
                Log.d(LOG_TAG, "Starting recording");
                recording = true;
                playBtn.setEnabled(false);
                stopPlayBtn.setEnabled(false);
                mRecorder = new MediaRecorder();
                mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                mRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
                mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
                mRecorder.setOutputFile(mFileName);
                try {
                    mRecorder.prepare();
                } catch (IOException e) {
                    Log.e(LOG_TAG, "prepare() failed");
                }
                mRecorder.start();
                Toast.makeText(getApplicationContext(), "Recording Started", Toast.LENGTH_LONG).show();
            }
            else{
                recording = false;
                startBtn.setEnabled(true);
                playBtn.setEnabled(true);
                stopPlayBtn.setEnabled(true);
                mRecorder.stop();
                mRecorder.release();
                mRecorder = null;
                Toast.makeText(getApplicationContext(), "Recording Stopped", Toast.LENGTH_LONG).show();
            }
        }
        else
        {
            RequestPermissions();
        }
    }

    public void startPlay(View view){
        startBtn.setEnabled(true);
        playBtn.setEnabled(false);
        stopPlayBtn.setEnabled(true);
        mPlayer = new MediaPlayer();
        try {
            mPlayer.setDataSource(mFileName);
            mPlayer.prepare();
            mPlayer.start();
            Toast.makeText(getApplicationContext(), "Recording Started Playing", Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare() failed");
        }
    }

    public void stopPlay(View view){
        mPlayer.release();
        mPlayer = null;
        startBtn.setEnabled(true);
        playBtn.setEnabled(true);
        stopPlayBtn.setEnabled(false);
        Toast.makeText(getApplicationContext(),"Playing Audio Stopped", Toast.LENGTH_SHORT).show();
    }

    private boolean CheckPermissions() {
        int result = ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE);
        int result1 = ContextCompat.checkSelfPermission(getApplicationContext(), READ_EXTERNAL_STORAGE);
        int result2 = ContextCompat.checkSelfPermission(getApplicationContext(), RECORD_AUDIO);
        int result3 = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_MEDIA_AUDIO);
        return ((result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED)||(result3 == PackageManager.PERMISSION_GRANTED) )&& result2 == PackageManager.PERMISSION_GRANTED;
    }
    private void RequestPermissions() {
        Log.d(LOG_TAG,"Starting request");
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.READ_MEDIA_AUDIO}, REQUEST_AUDIO_PERMISSION_CODE);
        Log.d(LOG_TAG,"Finished request");
    }



}
