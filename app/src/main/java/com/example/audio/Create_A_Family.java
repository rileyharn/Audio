package com.example.audio;


import static androidx.core.content.PackageManagerCompat.LOG_TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class Create_A_Family extends AppCompatActivity {
    private static final String LOG_TAG = "AudioRecording";
    FirebaseDatabase database;
    DatabaseReference myRef;
    ArrayList keyValues = new ArrayList<String>(10);
    ArrayList objectValues = new ArrayList<String>(10);
    int arraycounter;
    private Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.createafamily);
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("groups");

}

    public void createFamily(){
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
    private void clearArrayLists(){
        keyValues.clear();
        objectValues.clear();
    }

    public void leftButton(View v){
        if (arraycounter > 0) {
            arraycounter--;
        } else {
            Toast.makeText(getApplicationContext(), "You are at the beginning of your family list!", Toast.LENGTH_LONG).show();
        }

        TextView text = (TextView) findViewById(R.id.downloadedFileNameTextView);
        text.setText(objectValues.get(arraycounter).toString());

    }
    @SuppressLint("SetTextI18n")
    public void rightButton(View v){
        if (objectValues.size()-1 > arraycounter) {
            arraycounter++;
        } else {
            Toast.makeText(getApplicationContext(), "You are at the end of your family list!", Toast.LENGTH_LONG).show();
        }

        TextView text = (TextView) findViewById(R.id.selectedGroupName);
        text.setText("Group " + (arraycounter + 1) + "/" + (objectValues.size() + 1) + "Group Selected" + objectValues.get(arraycounter) );
    }

    //end of class
}
