package com.example.test;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Objects.requireNonNull(getSupportActionBar()).setTitle("Expense Tracker");

        ConnectivityManager connectivityManager =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        boolean isConnected = networkInfo != null && networkInfo.isConnected();

        if (!isConnected) {
            Toast.makeText(this, "No Connection!", Toast.LENGTH_SHORT).show();
        }
        Intent iSignup = new Intent(this, SignupActivity.class);
        Intent iHamster = new Intent(this, HamsterActivity.class);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            startActivity(iHamster);
            finish();
        }





        EditText edt_username = findViewById(R.id.edt_username);
        EditText edt_password = findViewById(R.id.edt_password);


        Button login_button = findViewById(R.id.login_button);
        Button signup_button = findViewById(R.id.signup_button);

        TextView alert = findViewById(R.id.alert_text);
        Animation alert_anim = AnimationUtils.loadAnimation(this, R.anim.alert_animation);


        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = edt_username.getText().toString();
                String password = edt_password.getText().toString();

                iHamster.putExtra("src", "fromLogin");

                if (!username.equals("") && !password.equals("") && isConnected){

                    mAuth.signInWithEmailAndPassword(username, password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        startActivity(iHamster);
                                        finish();

                                    } else {
                                        // If sign in fails, display a message to the user.
                                        edt_password.setText("");
                                        alert.setText(getResources().getText(R.string.alert));
                                        alert.startAnimation(alert_anim);


                                    }
                                }
                            });

                }



            }
        });

        signup_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(iSignup);
            }
        });


    }
}