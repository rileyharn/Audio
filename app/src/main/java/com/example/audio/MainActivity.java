package com.example.audio;

import static android.text.InputType.*;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.Manifest;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
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
    private String tempName;
    FirebaseStorage storage;
    StorageReference storageRef;
    FirebaseDatabase database;
    DatabaseReference myRef;
    private UploadTask uploadTask;
    //Misc variables

    private boolean isRecording = false;
    private boolean hasRecorded = false;
    private String username = "testUser";

    //Setting up button variables
    Button recordButton = null;
    Button playButton = null;
    Button stopButton = null;
    Button uploadButton = null;
    Button backButton = null;

    private String m_Text = "";

    //Code to run when screen is opened
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //firebase variables
        storageRef = FirebaseStorage.getInstance().getReference();
        myRef = FirebaseDatabase.getInstance().getReference().child("users/" + username + "/audios");
        //setting buttons from view
        recordButton = findViewById(R.id.recordButton);
        playButton = findViewById(R.id.startPlayback);
        stopButton = findViewById(R.id.stopPlayback);
        uploadButton = findViewById(R.id.uploadButton);
        backButton = findViewById(R.id.backButton);

        //getting current user for filesystem
        username = "testUser"; //((MyApplication) this.getApplication()).getUserName();

        rootPath = new File(getExternalFilesDir(null), username);
        if(!rootPath.exists()) {
            rootPath.mkdirs();
        }
        ((MyApplication) this.getApplication()).setCurDir(rootPath);

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
        uploadButton.setOnClickListener(this::setUploadTask);
        backButton.setOnClickListener(view -> {
            Intent intent = new Intent(getBaseContext(), User_Profile.class);
            startActivity(intent);
        });

        ActivityCompat.requestPermissions(this, permissions, REQUEST_RECORD_AUDIO_PERMISSION);
    }
    private void startRecording(){
        String text;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Name this recording");

        // Set up the input
        final EditText input = new EditText(this);
        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(TYPE_CLASS_TEXT | TYPE_TEXT_VARIATION_NORMAL);
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("OK", (dialog, which) -> {
            m_Text = input.getText().toString();
            tempName = m_Text;
            InputFilter[] filterArray = new InputFilter[1];
            filterArray[0] = new InputFilter.LengthFilter(20);
            input.setFilters(filterArray);
            Toast.makeText(getApplicationContext(), "Recording Started", Toast.LENGTH_LONG).show();
            playButton.setEnabled(false);
            stopButton.setEnabled(false);
            if(player!=null){
                player.release();
                player = null;
            }
            outputFile = new File(rootPath,m_Text.replaceAll(" ","_").replaceAll("[\\\\\\\\/:*?\\\"<>|]","")+".mp4");
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
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> {
            dialog.cancel();
            Toast.makeText(getApplicationContext(), "Recording Canceled", Toast.LENGTH_LONG).show();
        });

        builder.show();
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
        Uri file = Uri.fromFile(new File(outputFile.getAbsolutePath()));
        StorageReference Ref = storageRef.child(username+ "/audios/"+tempName);
        uploadTask = Ref.putFile(file);

// Register observers to listen for when the download is done or if it fails
        uploadTask.addOnFailureListener(exception -> {
            // Handle unsuccessful uploads
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                // ...
            }
        });

        //firebase database side
        myRef.child(tempName).setValue(username+"/audios/"+tempName);
    }
    }

