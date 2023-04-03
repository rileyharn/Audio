package com.example.audio;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;

public class FileStorageActivity extends AppCompatActivity {

    UploadTask uploadTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_storage);
    }
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReference();
    Uri file = Uri.fromFile(new File( Environment.getExternalStorageDirectory() + File.separator
            + Environment.DIRECTORY_DCIM + File.separator + "test.mpeg4"));
    StorageReference audioRef = storageRef.child("audios/"+file.getLastPathSegment());

    public void setUploadTask()
    {
        uploadTask = audioRef.putFile(file);
    }
    public UploadTask getUploadTasks()
    {

        UploadTask uploadTask = audioRef.putFile(file);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {

            }
        });

        return uploadTask;

    }

}


