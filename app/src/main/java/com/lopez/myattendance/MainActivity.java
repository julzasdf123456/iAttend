package com.lopez.myattendance;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public Toolbar toolbar;
    public FloatingActionButton addClass;

    public DatabaseReference dr;
    public FirebaseAuth firebaseAuth;
    public ArrayList<String> subjectsFinal = new ArrayList<>();
    public long maxClass = 0;

    public RecyclerView recyclerView;
    public ClassesAdapter classesAdapter;
    public List<Classes> classesList;
    public RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // FIREBASE INIT
        firebaseAuth = FirebaseAuth.getInstance();
        dr = FirebaseDatabase.getInstance().getReference().child("Classes");
        subjectsForSelection();

        // custom toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbarMain);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        // INITS
        addClass = (FloatingActionButton) findViewById(R.id.newClass);
        recyclerView = (RecyclerView) findViewById(R.id.classesRecyclerView);
        classesList = new ArrayList<>();
        layoutManager = new GridLayoutManager(MainActivity.this, 2);
        recyclerView.setLayoutManager(layoutManager);
        classesAdapter = new ClassesAdapter(classesList);
        recyclerView.setAdapter(classesAdapter);

        refreshClasses();

        dr.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                maxClass = (dataSnapshot.getChildrenCount());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        addClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                LayoutInflater li = LayoutInflater.from(MainActivity.this);
                View promptsView = li.inflate(R.layout.new_class, null);

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        MainActivity.this);

                // set prompts.xml to alertdialog builder
                alertDialogBuilder.setView(promptsView);

                final EditText newClassSchedule = (EditText) promptsView
                        .findViewById(R.id.newClassSchedule);
                final EditText newClassYear = (EditText) promptsView
                        .findViewById(R.id.newClassYear);
                final Spinner subjectSelect = (Spinner) promptsView.findViewById(R.id.subjectSelect);

                // ADDING ITEMS TO SPINNER

                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_spinner_item, subjectsFinal);
                arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                subjectSelect.setAdapter(arrayAdapter);

                // set dialog message
                alertDialogBuilder
                        .setTitle("Add New Class")
                        .setCancelable(false)
                        .setPositiveButton("ADD",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {
                                        dr.child(String.valueOf(maxClass+1)).setValue(new Classes(String.valueOf(maxClass+1),
                                                                    subjectSelect.getSelectedItem().toString(),
                                                                    newClassYear.getText().toString(),
                                                                    newClassSchedule.getText().toString(),
                                                                    firebaseAuth.getCurrentUser().getEmail()));
                                        Toast.makeText(MainActivity.this, "Class added successfully!", Toast.LENGTH_SHORT).show();
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
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.logout) {
            firebaseAuth.signOut();
            startActivity(new Intent(MainActivity.this, Login.class));
            finish();
        } else if (item.getItemId() == R.id.subjects) {
            startActivity(new Intent(MainActivity.this, Subject.class));
        }
        return super.onOptionsItemSelected(item);
    }

    public void subjectsForSelection() {
        DatabaseReference subjectsDr = FirebaseDatabase.getInstance().getReference().child("Subjects");
        Query query = subjectsDr.orderByChild("subjectName");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot subjects : dataSnapshot.getChildren()) {
                        Subjects subject = subjects.getValue(Subjects.class);
                        subjectsFinal.add(subject.getSubjectName());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public void refreshClasses() {
        try {

            Query query = dr.orderByChild("teacherEmail").equalTo(firebaseAuth.getCurrentUser().getEmail());
            query.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        classesList.clear();
                        for (DataSnapshot classSnap : dataSnapshot.getChildren()) {
                            Classes classes = classSnap.getValue(Classes.class);
                            classesList.add(new Classes(classes.getId(), classes.getSubjectId(), classes.getYear(), classes.getTeacherEmail(), classes.getYear()));
                        }

                        classesAdapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(MainActivity.this, "No subjects recorded yet", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        } catch (Exception e) {
            Log.e("ERR", e.getMessage());
        }
    }
}
