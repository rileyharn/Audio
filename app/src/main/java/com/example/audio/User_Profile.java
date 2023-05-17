package com.example.audio;


import static android.text.InputType.TYPE_CLASS_TEXT;
import static android.text.InputType.TYPE_TEXT_VARIATION_NORMAL;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import java.io.File;

public class User_Profile extends AppCompatActivity {

    private String username = /*((MyApplication) this.getApplication()).getUserName()*/ "testUser";
    private static final String LOG_TAG = "AudioRecording";
    //Buttons on the userprofile.xml page

    private Button joinFamilyGroupXML;
    private Button recordNowXML;
    private Button createFamilyGroupXML;
    private Button signOutXML;
    private Button searchMyRecordingsXML;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.userprofile);

        searchMyRecordingsXML = findViewById(R.id.searchMyRecordingsButton);
        joinFamilyGroupXML = findViewById(R.id.joinAFamilyGroupButton);
        recordNowXML = findViewById(R.id.recordNowButton);
        createFamilyGroupXML = findViewById(R.id.createAFamilyGroupButton);
        signOutXML = findViewById(R.id.signOutButton);

        ((MyApplication) this.getApplication()).setCurDir(new File(getExternalFilesDir(null), ((MyApplication) this.getApplication()).getUserName()));


        joinFamilyGroupXML.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                joinFamilyPopUp();

            }
        });

        recordNowXML.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), MainActivity.class);
                startActivity(intent);

            }
        });

        createFamilyGroupXML.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), Create_A_Family.class);
                startActivity(intent);

            }
        });
        signOutXML.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), LoginScreen.class);
                startActivity(intent);

            }
        });
        searchMyRecordingsXML.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), searchMyRecordings.class);
                startActivity(intent);

            }
        });


    }

   private void joinFamilyPopUp(){
       AlertDialog.Builder builder = new AlertDialog.Builder(this);
       builder.setTitle("Enter a 6-letter code");

       final EditText input = new EditText(this);
       // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
       input.setInputType(TYPE_CLASS_TEXT | TYPE_TEXT_VARIATION_NORMAL);
       input.setHint("don't have a code? Create your own!");
       builder.setView(input);
//       final TextView text = new TextView(this);
//       text.setText("don't have a code? Get your code from someone in an existing group or create your own!");
//       builder.setView(text);
       // Set up the buttons
       builder.setPositiveButton("OK", (dialog, which) -> {
           Log.d(LOG_TAG, input.getText().toString());
           String m_Text = input.getText().toString();
           Log.d(LOG_TAG, m_Text);
           ArrayList keyValues = new ArrayList<String>();
           ArrayList objectValues = new ArrayList<String>();
           if(m_Text.length() ==6) {
               FirebaseDatabase database;
               DatabaseReference myRef2;
               database = FirebaseDatabase.getInstance();
               myRef2 = database.getReference("families");
              myRef2.addListenerForSingleValueEvent(new ValueEventListener() {
                  @Override
                  public void onDataChange(DataSnapshot dataSnapshot) {

                      for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                          String childKey = childSnapshot.getKey();
                          Object childValue = childSnapshot.getValue();
                          // keyValues contain codes for family groups
                          keyValues.add(childKey);
                         // Log.d(LOG_TAG, childKey);
                          objectValues.add(childKey);
                      }
                  }
                  @Override
                  public void onCancelled(DatabaseError databaseError) {
                      // Handle error
                      Log.d(LOG_TAG, "on data change method was cancelled");
                  }
              });

              boolean found = false;
                for (int i = 0; i<keyValues.size();i++){
                    Log.d(LOG_TAG, keyValues.get(i).toString() + "==" + m_Text);
                    if(keyValues.get(i).toString().equals(m_Text)){

                        FirebaseDatabase.getInstance().getReference("users/"+username+"/groups/" + keyValues.get(i)).setValue(objectValues.get(i));
                        i = keyValues.size() + 1;
                        found = true;
                        Toast.makeText(getApplicationContext(), "Family successfully joined", Toast.LENGTH_LONG).show();
                    }


                }
                if (!found){
                    Toast.makeText(getApplicationContext(), "Family doesn't exist", Toast.LENGTH_LONG).show();
                }


           }
           else{
               Toast.makeText(getApplicationContext(), "Invalid Code", Toast.LENGTH_LONG).show();
           }
       });
       builder.setNegativeButton("Cancel", (dialog, which) -> {
           dialog.cancel();
           Toast.makeText(getApplicationContext(), "Join a Family Canceled", Toast.LENGTH_LONG).show();
       });

       builder.show();
   }



   //end of main
}



