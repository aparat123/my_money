package com.aparat.money;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.jetbrains.annotations.NotNull;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    ImageButton backBtn;
    Button loginBtn, signin;
    TextInputEditText emailEditText, passwordEditText;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailEditText = (TextInputEditText) findViewById(R.id.textEditEmail);
        passwordEditText = (TextInputEditText) findViewById(R.id.textEditPwd);

        mAuth = FirebaseAuth.getInstance();

        signin = (Button) findViewById(R.id.signBtn);
        signin.setOnClickListener(this);
        loginBtn = (Button) findViewById(R.id.loginBtn);
        loginBtn.setOnClickListener(this);
        backBtn = (ImageButton) findViewById(R.id.backBtn);
        backBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.backBtn:
                finish();
                break;
            case R.id.loginBtn:
                userLogin();
                break;
            case R.id.signBtn:
                startActivity(new Intent(getApplicationContext(), SignupActivity.class));
                finish();
                break;
        }
    }
    private void userLogin(){
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        if(email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            emailEditText.setError("Email is required!");
            emailEditText.requestFocus();
        }
        else if(password.isEmpty() || password.length() < 6){
            passwordEditText.setError("Password is required!");
            passwordEditText.requestFocus();
        }

        else {
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull @NotNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                if(user.isEmailVerified()) {
                                    startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                                    finish();
                                }
                                else {
                                    user.sendEmailVerification();
                                    Toast.makeText(getApplicationContext(), "Check your email and try again!", Toast.LENGTH_LONG).show();
                                }
                            }
                            else {
                                Toast.makeText(getApplicationContext(), "Failed to login!", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
            }
        }
    }