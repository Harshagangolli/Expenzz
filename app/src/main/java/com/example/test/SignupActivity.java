package com.example.test;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class SignupActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        Objects.requireNonNull(getSupportActionBar()).setTitle("Sign Up to Expenzz");


        ConnectivityManager connectivityManager =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        boolean isConnected = networkInfo != null && networkInfo.isConnected();

        if (!isConnected) {
            Toast.makeText(this, "No Connection!", Toast.LENGTH_SHORT).show();
        }

        Intent iHamster = new Intent(this, HamsterActivity.class);


        EditText username = findViewById(R.id.su_username);
        EditText mail = findViewById(R.id.su_mail);
        EditText password = findViewById(R.id.su_password);
        Button signup = findViewById(R.id.su_signupBtn);


        mAuth = FirebaseAuth.getInstance();



        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String input1 = username.getText().toString();
                String input2 = mail.getText().toString();
                String input3 = password.getText().toString();


                if (!input1.isEmpty() && !input2.isEmpty() && !input3.isEmpty()) {


                    mAuth.createUserWithEmailAndPassword(input2, input3)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {

                                        startActivity(iHamster);
                                        finish();

                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Toast.makeText(SignupActivity.this, "Enter Valid Things!", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                }
            }
        });
    }
}