package com.lopez.myattendance;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {

    public Button loginButton, registerLogin;
    public EditText username, password;

    public FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // INITS
        loginButton = (Button) findViewById(R.id.loginButton);
        registerLogin = (Button) findViewById(R.id.registerLogin);
        username = (EditText) findViewById(R.id.usernameLogin);
        password = (EditText) findViewById(R.id.passwordLogin);
        firebaseAuth = FirebaseAuth.getInstance();

        // EVENTS
        registerLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login.this, Register.class);
                startActivity(intent);
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String usernameTxt = username.getText().toString();
                String passwordTxt = password.getText().toString();

                if (TextUtils.isEmpty(usernameTxt)) {
                    username.setError("Username should not be empty!");
                    return;
                }

                if (TextUtils.isEmpty(passwordTxt)) {
                    password.setError("Password should not be empty!");
                    return;
                }

                firebaseAuth.signInWithEmailAndPassword(usernameTxt, passwordTxt).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            startActivity(new Intent(Login.this, MainActivity.class));
                            finish();
                        } else {
                            Toast.makeText(Login.this, "Login failed for user " + usernameTxt + ".", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
}
