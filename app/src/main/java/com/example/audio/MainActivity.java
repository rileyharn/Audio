package com.example.audio;

import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
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
import java.io.IOException;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class MainActivity extends AppCompatActivity {
    private Button startBtn, stopBtn, playBtn, stopPlayBtn;
    private MediaRecorder mRecorder;
    private MediaPlayer mPlayer;
    private static final String LOG_TAG = "AudioRecording";
    private static String mFileName = null;
    public static final int REQUEST_AUDIO_PERMISSION_CODE = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startBtn = findViewById(R.id.recordButton);
        stopBtn = findViewById(R.id.stopRecordButton);
        playBtn = findViewById(R.id.startPlayback);
        stopPlayBtn = findViewById(R.id.stopPlayback);
        stopBtn.setEnabled(false);
        playBtn.setEnabled(false);
        stopPlayBtn.setEnabled(false);
        mFileName = Environment.getExternalStorageDirectory().getAbsolutePath();
        mFileName += "/AudioRecording.3gp";
    }

    public void startRecording(View view){
        if(CheckPermissions()) {
            stopBtn.setEnabled(true);
            startBtn.setEnabled(false);
            playBtn.setEnabled(false);
            stopPlayBtn.setEnabled(false);
            mRecorder = new MediaRecorder();
            mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
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
        else
        {
            RequestPermissions();
        }
    }

    public void stopRecording(View view){
        stopBtn.setEnabled(false);
        startBtn.setEnabled(true);
        playBtn.setEnabled(true);
        stopPlayBtn.setEnabled(true);
        mRecorder.stop();
        mRecorder.release();
        mRecorder = null;
        Toast.makeText(getApplicationContext(), "Recording Stopped", Toast.LENGTH_LONG).show();
    }

    public void startPlay(View view){
        stopBtn.setEnabled(false);
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
        stopBtn.setEnabled(false);
        startBtn.setEnabled(true);
        playBtn.setEnabled(true);
        stopPlayBtn.setEnabled(false);
        Toast.makeText(getApplicationContext(),"Playing Audio Stopped", Toast.LENGTH_SHORT).show();
    }

    private boolean CheckPermissions() {
        int result = ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE);
        int result1 = ContextCompat.checkSelfPermission(getApplicationContext(), READ_EXTERNAL_STORAGE);
        int result2 = ContextCompat.checkSelfPermission(getApplicationContext(), RECORD_AUDIO);
        return result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED && result2 == PackageManager.PERMISSION_GRANTED;
    }
    private void RequestPermissions() {
        System.out.println("Here");
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE}, REQUEST_AUDIO_PERMISSION_CODE);
    }
}
