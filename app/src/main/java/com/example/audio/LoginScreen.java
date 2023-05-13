package com.example.audio;

import static android.text.InputType.TYPE_CLASS_TEXT;
import static android.text.InputType.TYPE_TEXT_VARIATION_NORMAL;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

public class LoginScreen extends AppCompatActivity {
    GoogleSignInClient mGoogleSignInClient = null;
    GoogleSignInAccount account = null;

    Button logInButton = null;

    private String m_Text = "";
    //protected final BetterActivityResult<Intent, ActivityResult> activityLauncher = BetterActivityResult.registerActivityForResult(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_screen);
        logInButton = findViewById(R.id.familyGroupsButton);
        logInButton.setOnClickListener(view -> {
            signIn();
        });
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        account = GoogleSignIn.getLastSignedInAccount(this);
        //updateUI(account);
    }

    private void signIn() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enter username");

        // Set up the input
        final EditText input = new EditText(this);
        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(TYPE_CLASS_TEXT | TYPE_TEXT_VARIATION_NORMAL);
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("OK", (dialog, which) -> {
            m_Text = input.getText().toString();
            if(m_Text.length()<=10) {
                ((MyApplication) this.getApplication()).setUserName(m_Text.replaceAll("[\\\\\\\\/:*?\\\"<>|]","").toUpperCase());

                Toast.makeText(getApplicationContext(), "Sign-In Successful", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getBaseContext(), User_Profile.class);
                startActivity(intent);
            }
            else{
                Toast.makeText(getApplicationContext(), "Username too long", Toast.LENGTH_LONG).show();
            }
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> {
            dialog.cancel();
            Toast.makeText(getApplicationContext(), "Sign-In Canceled", Toast.LENGTH_LONG).show();
        });

        builder.show();
    }
}
