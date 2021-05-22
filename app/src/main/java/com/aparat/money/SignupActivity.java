package com.aparat.money;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.aparat.money.Model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class SignupActivity extends AppCompatActivity{
    ImageButton backBtn;
    Button createAccBtn;
    private FirebaseAuth mAuth;
    TextInputEditText nameEditText, emailEditText, passwordEditText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        mAuth = FirebaseAuth.getInstance();
        nameEditText = (TextInputEditText) findViewById(R.id.nameET);
        emailEditText = (TextInputEditText) findViewById(R.id.emailET);
        passwordEditText = (TextInputEditText) findViewById(R.id.passwordET);
        backBtn = (ImageButton) findViewById(R.id.backBtn);
        createAccBtn = (Button) findViewById(R.id.loginBtn);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        createAccBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAccount();
            }
        });
    }


    private void createAccount(){
        String name = nameEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        if(name.isEmpty()){
            nameEditText.setError("Name is empty!");
            nameEditText.requestFocus();

        }
        else if(!isValidEmail(email)){
            emailEditText.setError("Email not valid!");
            emailEditText.requestFocus();
        }
        else if(password.length() < 6){
            passwordEditText.setError("Password must be have 6 and more symbols!");
            passwordEditText.requestFocus();
        }
        else {
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull @org.jetbrains.annotations.NotNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                User user = new User(name, email, password);

                                FirebaseDatabase.getInstance().getReference("Users")
                                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                        .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull @org.jetbrains.annotations.NotNull Task<Void> task) {
                                        if(task.isSuccessful()) {
                                            Toast.makeText(getApplicationContext(), "User has been registered successfully!", Toast.LENGTH_LONG).show();
                                        }
                                        else {
                                            Toast.makeText(getApplicationContext(), "Failed to register! Try again!", Toast.LENGTH_LONG).show();

                                        }
                                    }
                                });
                            }
                            else {
                                Toast.makeText(getApplicationContext(), "Failed to register! Try again!", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
        }

    }

    private boolean isValidEmail(String target) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }
}