package com.hamlet.MrFixer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.Notification;
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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;

public class SignUpActivity extends AppCompatActivity {
    private EditText nameEText;
    private EditText emailEText;
    private EditText passwordEText;
    private Button signUpButton;
    private ImageButton cancelButton;
    private TextView logInTextV;
    private ProgressBar progressBar;

    private FirebaseAuth firebaseAuth;

    private NotificationManager manager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        firebaseAuth = FirebaseAuth.getInstance();
        manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        removeBars();
        initViews();

        logInTextV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUpActivity.this,LogInActivity.class);
                startActivity(intent);
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUpActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                final String email = emailEText.getText().toString().trim();
                final String password = passwordEText.getText().toString().trim();

                if(nameEText.getText().toString().isEmpty()){
                    nameEText.setError("Name required");
                    nameEText.requestFocus();
                    progressBar.setVisibility(View.GONE);
                    return;
                }

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

                Task<AuthResult> signUp = firebaseAuth.createUserWithEmailAndPassword(email, password);
                signUp.addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressBar.setVisibility(View.GONE);
                        if(task.isSuccessful()){
                            Toast.makeText(SignUpActivity.this, "Account created successfully!", Toast.LENGTH_SHORT).show();
                            Task<AuthResult> authResultTask = firebaseAuth.signInWithEmailAndPassword(email, password);
                            authResultTask.addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(task.isSuccessful()){
                                        Toast.makeText(SignUpActivity.this, "user logged in successfuly!", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(SignUpActivity.this,MainActivity.class);
                                        startActivity(intent);
                                        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(),Notifications.CHANNEL_ONE_ID);

                                        builder.setSmallIcon(R.drawable.ic_add)
                                                .setContentTitle("WELCOME")
                                                .setContentText("We'll try our best to serve you!")
                                                .setPriority(NotificationCompat.PRIORITY_HIGH)
                                                .build();
                                        manager.notify(1,builder.build());

                                        finish();
                                    } else {
                                        Toast.makeText(SignUpActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                        } else {
                            if(task.getException() instanceof FirebaseAuthUserCollisionException){
                                Toast.makeText(SignUpActivity.this, "user already have this email", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(SignUpActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
            }
        });

    }

    public void initViews(){
        nameEText = findViewById(R.id.signUpNameText);
        emailEText = findViewById(R.id.signUpEmailText);
        passwordEText = findViewById(R.id.signUpPasswordText);
        signUpButton = findViewById(R.id.signUpSignInButton);
        logInTextV = findViewById(R.id.signUpLogInText);
        logInTextV.setPaintFlags(logInTextV.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        cancelButton = findViewById(R.id.signUpCancelButton);
        progressBar = findViewById(R.id.signUpProgressBar);
        progressBar.setVisibility(View.GONE);
    }

    public void removeBars(){
        getSupportActionBar().hide();
        Window window = getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        //  #00695C
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

}
