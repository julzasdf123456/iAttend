package com.lopez.myattendance;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

public class Subject extends AppCompatActivity {

    public Toolbar toolbar;
    public FloatingActionButton newSubject;

    public RecyclerView recyclerView;
    public SubjectsAdapter subjectsAdapter;
    public List<Subjects> subjectsList;
    private RecyclerView.LayoutManager layoutManager;

    public DatabaseReference dr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject);

        // custom toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbarSubjects);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_white_24dp);

        // INITS
        dr = FirebaseDatabase.getInstance().getReference().child("Subjects");
        newSubject = (FloatingActionButton) findViewById(R.id.newSubject);

        recyclerView = (RecyclerView) findViewById(R.id.subjectsRecyclerView);
        subjectsList = new ArrayList<>();
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        subjectsAdapter = new SubjectsAdapter(subjectsList);
        recyclerView.setAdapter(subjectsAdapter);
        // UPDATE LIST
        dr.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if (dataSnapshot.exists()){
                    Subjects subject = dataSnapshot.getValue(Subjects.class);
                    subjectsList.add(new Subjects(subject.getSubjectName(), subject.getSubjectDescription()));
                    subjectsAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        newSubject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater li = LayoutInflater.from(Subject.this);
                View promptsView = li.inflate(R.layout.new_subject, null);

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        Subject.this);

                // set prompts.xml to alertdialog builder
                alertDialogBuilder.setView(promptsView);

                final EditText newSubject = (EditText) promptsView
                        .findViewById(R.id.subjectNameNew);
                final EditText newSubjectDescription = (EditText) promptsView
                        .findViewById(R.id.subjectDescriptionNew);

                // set dialog message
                alertDialogBuilder
                        .setTitle("Add New Subject")
                        .setCancelable(false)
                        .setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {
                                        dr.push().setValue(new Subjects(newSubject.getText().toString(), newSubjectDescription.getText().toString()));
                                        Toast.makeText(Subject.this, "Subject added successfully!", Toast.LENGTH_SHORT).show();
                                    }
                                })
                        .setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {
                                        dialog.cancel();
                                    }
                                });

                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();

                // show it
                alertDialog.show();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
