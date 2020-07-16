package com.hamlet.MrFixer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FeedbackActivity extends AppCompatActivity {

    private EditText nameText;
    private EditText emailText;
    private EditText subjectText;
    private EditText feedbackText;
    private Button button;

    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        initViews();
        databaseReference = FirebaseDatabase.getInstance().getReference("feedback");

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name = nameText.getText().toString();
                String email = emailText.getText().toString();
                String subject = subjectText.getText().toString();
                String feedback = feedbackText.getText().toString();

                FeedBack feedBack = new FeedBack(name,email,subject,feedback);

                if(name.isEmpty()){
                    nameText.setError("Name required");
                    emailText.requestFocus();
                    return;
                }
                if(email.isEmpty()){
                    emailText.setError("Email required");
                    emailText.requestFocus();
                    return;
                }

                if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    emailText.setError("Please enter valid email");
                    emailText.requestFocus();
                    return;
                }

                if(subject.isEmpty()){
                    subjectText.setError("Subject required");
                    subjectText.requestFocus();
                    return;
                }
                if(feedback.isEmpty()){
                    feedbackText.setError("Feedback required");
                    feedbackText.requestFocus();
                    return;
                }

                Task<Void> task = databaseReference.child(name).setValue(feedBack);
                task.addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Snackbar.make(findViewById(android.R.id.content),"Thanks for your feedback!", Snackbar.LENGTH_SHORT).setAction("Action",null).show();
                            clearEdits();
                        } else {
                            Toast.makeText(FeedbackActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }


    public void initViews(){
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Feedback");

        nameText = findViewById(R.id.feedbackName);
        emailText = findViewById(R.id.feedbackEmail);
        subjectText = findViewById(R.id.feedbackSubject);
        feedbackText = findViewById(R.id.feedbackFeedback);
        button = findViewById(R.id.feedbackSendButton);
    }

    public void clearEdits(){
        nameText.setText("");
        emailText.setText("");
        subjectText.setText("");
        feedbackText.setText("");
    }

}
