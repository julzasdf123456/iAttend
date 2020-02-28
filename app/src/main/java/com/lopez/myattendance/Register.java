package com.lopez.myattendance;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class Register extends AppCompatActivity {

    public EditText username, password, fullname, email;
    public Button registerButton, loginRegister;
    public Spinner department;

    public FirebaseAuth firebaseAuth;
    public DatabaseReference dr;

    public ArrayList<String> departments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // INITS
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        fullname = (EditText) findViewById(R.id.fullname);
        email = (EditText) findViewById(R.id.email);
        registerButton = (Button) findViewById(R.id.registerButton);
        loginRegister = (Button) findViewById(R.id.loginRegister);
        firebaseAuth = FirebaseAuth.getInstance();
        departments = new ArrayList<>();
        department = (Spinner) findViewById(R.id.department);
        dr = FirebaseDatabase.getInstance().getReference().child("Teachers");

        departments.add("ICT");
        departments.add("EDUCATION");
        departments.add("ACCOUNTANCY");

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(Register.this, android.R.layout.simple_spinner_item, departments);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        department.setAdapter(arrayAdapter);

        // EVENTS
        loginRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Register.this, Login.class));
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String usernameTxt = username.getText().toString();
                String passwordTxt = password.getText().toString();
                String fullnameTxt = fullname.getText().toString();
                String emailTxt = email.getText().toString();

                if (TextUtils.isEmpty(fullnameTxt)) {
                    fullname.setError("Full name is required.");
                    return;
                }

                if (TextUtils.isEmpty(usernameTxt)) {
                    username.setError("Username is required.");
                    return;
                }

                if (TextUtils.isEmpty(passwordTxt)) {
                    password.setError("Password is required.");
                    return;
                } else {
                    if (passwordTxt.length() < 8) {
                        password.setError("Password length must be greater than 8.");
                        return;
                    }
                }

                if (TextUtils.isEmpty(emailTxt)) {
                    email.setError("Email is required.");
                    return;
                }

                dr.push().setValue(new Teachers(emailTxt, fullnameTxt, department.getSelectedItem().toString(), "", passwordTxt, ""));

                firebaseAuth.createUserWithEmailAndPassword(emailTxt, passwordTxt).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(Register.this, "Registration Successful. Welcome to iAttendance!", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(Register.this, MainActivity.class));
                        } else {
                            Toast.makeText(Register.this, "Error creating user!", Toast.LENGTH_SHORT).show();
                            Log.e("ERR", task.getException().getMessage());
                        }
                    }
                });
            }
        });

    }
}
