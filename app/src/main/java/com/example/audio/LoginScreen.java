package com.example.audio;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.identity.BeginSignInRequest;
import com.google.android.gms.auth.api.identity.SignInClient;
import com.google.android.gms.auth.api.identity.SignInCredential;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApi;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class LoginScreen extends AppCompatActivity {
    private static final int RC_SIGN_IN = 1;
    private FirebaseAuth mAuth;
    GoogleSignInClient mGoogleSignInClient;
    GoogleSignInAccount account;
    private static final int REQ_ONE_TAP = 2;
    private boolean showOneTapUI = true;
    private static final String LOG_TAG = "AudioRecording";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
      //  findViewById(R.id.googleSignInButton).setOnClickListener((View.OnClickListener) this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_screen);
        mAuth = FirebaseAuth.getInstance();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);


    }

    public void signIn(View v) {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(signInIntent);
        handleSignInResult(task);
        TextView tv1 = (TextView)findViewById(R.id.currentUserText);
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            account = completedTask.getResult(ApiException.class);

            // Signed in successfully, show authenticated UI.
            Toast.makeText(getApplicationContext(), "Sign in successful", Toast.LENGTH_LONG).show();
            TextView tv1 = (TextView)findViewById(R.id.currentUserText);
            tv1.setText("Signed in as:" + account.getEmail());
        } catch (ApiException e) {

            Log.e(LOG_TAG, "signInResult:failed code=" + e.getStatusCode());

        }

    }
    public void signOut(View v){
        mGoogleSignInClient.signOut();

        Toast.makeText(getApplicationContext(), "Sign out successful", Toast.LENGTH_LONG).show();
        TextView tv1 = (TextView)findViewById(R.id.currentUserText);
        tv1.setText("Signed out");
    }

    public void debugEmail(View v){
        Toast.makeText(getApplicationContext(), "email: " + account.getEmail(), Toast.LENGTH_LONG).show();
    }

    public void switchToUserProfilexml(View v){
        Intent intent = new Intent(getBaseContext(), User_Profile.class);
        startActivity(intent);
    }
}
