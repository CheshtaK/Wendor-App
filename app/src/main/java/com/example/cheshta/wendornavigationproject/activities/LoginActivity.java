package com.example.cheshta.wendornavigationproject.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.cheshta.wendornavigationproject.R;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.shobhitpuri.custombuttons.GoogleSignInButton;

import java.util.Arrays;

import mehdi.sakout.fancybuttons.FancyButton;

public class LoginActivity extends AppCompatActivity {

    private TextInputLayout tilYourEmail, tilLoginPassword;
    private Toolbar loginToolbar;
    private Button btnLogin, btnRegister;
    private ProgressDialog loginProgress;

    private FirebaseAuth mAuth;
    private DatabaseReference mUserDatabase;

    //FACEBOOK SIGN IN
    private FancyButton btnFB;
    private CallbackManager callbackManager;

    //GOOGLE SIGN IN
    private static final int RC_SIGN_IN = 1;
    private GoogleSignInButton btnGoogle;
    private GoogleApiClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        tilYourEmail = findViewById(R.id.tilYourEmail);
        tilLoginPassword = findViewById(R.id.tilLoginPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnRegister = findViewById(R.id.btnRegister);

        loginToolbar = findViewById(R.id.loginToolbar);
        setSupportActionBar(loginToolbar);
        getSupportActionBar().setTitle("Login");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        loginProgress = new ProgressDialog(this);

        mAuth = FirebaseAuth.getInstance();
        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users");

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email = tilYourEmail.getEditText().getText().toString();
                String password = tilLoginPassword.getEditText().getText().toString();

                if(!email.isEmpty() && !password.isEmpty()){

                    loginProgress.setTitle("Logging In");
                    loginProgress.setMessage("Please wait while we check your credentials.");
                    loginProgress.setCanceledOnTouchOutside(false);
                    loginProgress.show();
                    LoginUser(email, password);
                }
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent regIntent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(regIntent);
                finish();
            }
        });

        //FACEBOOK SIGN IN
        btnFB = findViewById(R.id.btnFB);
        callbackManager = CallbackManager.Factory.create();

        btnFB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LoginManager.getInstance().logInWithReadPermissions(LoginActivity.this, Arrays.asList("email", "public_profile"));
                LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        loginProgress.setTitle("Logging In");
                        loginProgress.setMessage("Please wait while we login to Facebook!");
                        loginProgress.setCanceledOnTouchOutside(false);
                        loginProgress.show();

                        handleFacebookAccessToken(loginResult.getAccessToken());
                    }

                    @Override
                    public void onCancel() {

                    }

                    @Override
                    public void onError(FacebookException error) {

                    }
                });
            }
        });


        //GOOGLE SIGN IN
        btnGoogle = findViewById(R.id.btnGoogle);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = new GoogleApiClient.Builder(getApplicationContext())
                .enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

                        Toast.makeText(LoginActivity.this, "You got an error :(", Toast.LENGTH_SHORT).show();
                    }
                })
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        btnGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });
    }

    private void LoginUser(String email, String password) {

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful()){
                    loginProgress.dismiss();

                    Intent mainIntent = new Intent(LoginActivity.this, MainActivity.class);
                    mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(mainIntent);
                    finish();
                }
                else {
                    loginProgress.hide();
                    Toast.makeText(LoginActivity.this, "Cannot sign in. Please check the form and try again", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    //FACEBOOK SIGN IN

    private void handleFacebookAccessToken(AccessToken token) {

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Intent mainIntent = new Intent(LoginActivity.this, MainActivity.class);
                            mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(mainIntent);
                            finish();
                        }
                        else {
                            loginProgress.dismiss();
                            Toast.makeText(LoginActivity.this, "Cannot sign in to Facebook", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }


    //GOOGLE SIGN IN
    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleSignInClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Intent mainIntent = new Intent(LoginActivity.this, MainActivity.class);
                            mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(mainIntent);
                            finish();
                        }
                        else {
                            loginProgress.dismiss();
                            Toast.makeText(LoginActivity.this, "Cannot sign in to Google", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);

                loginProgress.setTitle("Logging In");
                loginProgress.setMessage("Please wait while we login to Google!");
                loginProgress.setCanceledOnTouchOutside(false);
                loginProgress.show();

                firebaseAuthWithGoogle(account);
            }
            catch (ApiException e) {
                e.printStackTrace();
            }
        }
        else{
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

}
