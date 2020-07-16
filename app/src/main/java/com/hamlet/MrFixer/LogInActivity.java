package com.hamlet.MrFixer;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.NotificationManager;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.identity.SignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;

public class LogInActivity extends AppCompatActivity {

    private static final int GOOGLE_REQUEST_CODE = 9001;

    private EditText emailEText;
    private EditText passwordEText;
    private Button logInButton;
    private ImageButton cancelButton;
    private TextView signUpTextV;
    private ProgressBar progressBar;
    private LoginButton fbloginButton;
    private SignInButton googleSignButton;

    private FirebaseAuth firebaseAuth;
    private CallbackManager callbackManager;
    private GoogleSignInClient googleSignInClient;
    private NotificationManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        firebaseAuth = FirebaseAuth.getInstance();
        manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        removeBars();
        initViews();

        fbloginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fbloginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        faceBookCallBackToken(loginResult.getAccessToken());
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
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        googleSignInClient = GoogleSignIn.getClient(this,googleSignInOptions);
        googleSignButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                signIn();
            }
        });

        signUpTextV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LogInActivity.this,SignUpActivity.class);
                startActivity(intent);
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LogInActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        logInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                String email = emailEText.getText().toString().trim();
                String password = passwordEText.getText().toString().trim();

                if(email.isEmpty()){
                    emailEText.setError("Email required");
                    emailEText.requestFocus();
                    progressBar.setVisibility(View.GONE);
                    return;
                }

                if(password.isEmpty()){
                    passwordEText.setError("Passwird required");
                    passwordEText.requestFocus();
                    progressBar.setVisibility(View.GONE);
                    return;
                }

                if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    emailEText.setError("Enter valid email");
                    emailEText.requestFocus();
                    progressBar.setVisibility(View.GONE);
                    return;
                }

                if(password.length()<6){
                    passwordEText.setError("Minimum length should be 6");
                    passwordEText.requestFocus();
                    progressBar.setVisibility(View.GONE);
                    return;
                }

                Task<AuthResult> logIn = firebaseAuth.signInWithEmailAndPassword(email, password);
                logIn.addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressBar.setVisibility(View.GONE);
                        if(task.isSuccessful()){
                            Toast.makeText(LogInActivity.this, "user logged in successfully!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(LogInActivity.this,MainActivity.class);
                            startActivity(intent);
                            setNotification();
                            finish();
                        } else {
                            Toast.makeText(LogInActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

    private void signIn() {
        Intent intent = googleSignInClient.getSignInIntent();
        startActivityForResult(intent,GOOGLE_REQUEST_CODE);
    }

    private void faceBookCallBackToken(AccessToken accessToken) {
        AuthCredential credential = FacebookAuthProvider.getCredential(accessToken.getToken());
        Task<AuthResult> authResultTask = firebaseAuth.signInWithCredential(credential);
        authResultTask.addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(LogInActivity.this, "Logged in successfully", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LogInActivity.this,MainActivity.class);
                    startActivity(intent);
                    setNotification();
                    finish();
                } else {
                    Toast.makeText(LogInActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void setNotification(){
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(),Notifications.CHANNEL_ONE_ID);
        builder.setSmallIcon(R.drawable.ic_add)
                .setContentTitle("WELCOME")
                .setContentText("Nice to see you here again!")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .build();
        manager.notify(1,builder.build());
    }

    public void initViews(){
        emailEText = findViewById(R.id.loginEmailText);
        passwordEText = findViewById(R.id.logInPasswordText);
        logInButton = findViewById(R.id.loginSignInButton);
        signUpTextV = findViewById(R.id.logInSignUpText);
        signUpTextV.setPaintFlags(signUpTextV.getPaintFlags()|Paint.UNDERLINE_TEXT_FLAG);
        cancelButton = findViewById(R.id.logInCancelButton);
        progressBar = findViewById(R.id.logInProgressBar);
        progressBar.setVisibility(View.GONE);
        fbloginButton = findViewById(R.id.logInFacebookButton);
        callbackManager = CallbackManager.Factory.create();
        googleSignButton = findViewById(R.id.loginGoogleButton);
    }
    public void removeBars(){
        getSupportActionBar().hide();
        Window window = getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            #00695C
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        callbackManager.onActivityResult(requestCode,resultCode,data);
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == GOOGLE_REQUEST_CODE){
            Task<GoogleSignInAccount> signedInAccountFromIntent = GoogleSignIn.getSignedInAccountFromIntent(data);

            GoogleSignInAccount result = null;
            try {
                result = signedInAccountFromIntent.getResult(ApiException.class);
                if(result != null){
                    setNotification();
                }
            } catch (ApiException e) {
                e.printStackTrace();
            }
            assert result != null;
            googleFirebaseAuth(result.getIdToken());
        }
    }

    private void googleFirebaseAuth(String result) {
        AuthCredential credential = GoogleAuthProvider.getCredential(result, null);
        Task<AuthResult> authResultTask = firebaseAuth.signInWithCredential(credential);
        authResultTask.addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressBar.setVisibility(View.GONE);
                if(task.isSuccessful()){
                    Toast.makeText(LogInActivity.this, "Logged In successfully!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LogInActivity.this,MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(LogInActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}
