package com.lopez.myattendance;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AddStudentToClass extends AppCompatActivity {

    public Toolbar toolbar;
    public TextView title;
    public FloatingActionButton floatingActionButton;
    public RecyclerView recyclerView;

    public Bundle bundle;

    public Spinner filterCourse, filterBlock;
    public RecyclerView.LayoutManager layoutManager;
    public AddStudentToClassAdapter studentsAdapter;
    public List<Students> studentsList;

    public DatabaseReference courseDr, blockDr, studentDr, classStudentsDr;

    public ArrayList<String> courseSelection = new ArrayList<>();
    public ArrayList<String> blockSelection = new ArrayList<>();

    public ArrayList<Blocks> hashedBlocks;
    public String classId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_student_to_class);

        // toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbarAddStudents);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_white_24dp);

        // inits
        bundle = getIntent().getExtras();
        classId = bundle.getString("SUBJECT");
        title = (TextView) findViewById(R.id.addStudentToClassTitle);
        courseDr = FirebaseDatabase.getInstance().getReference().child("Courses");
        blockDr = FirebaseDatabase.getInstance().getReference().child("Blocks");
        studentDr = FirebaseDatabase.getInstance().getReference().child("Students");
        classStudentsDr = FirebaseDatabase.getInstance().getReference().child("ClassStudents").child(classId);
        filterBlock = (Spinner) findViewById(R.id.blockSelectAdd);
        filterCourse = (Spinner) findViewById(R.id.courseSelectAdd);
        floatingActionButton = (FloatingActionButton) findViewById(R.id.addStudentsButton);
        hashedBlocks = new ArrayList<>();
        recyclerView = (RecyclerView) findViewById(R.id.studentsRecyclerViewAdd);
        layoutManager = new LinearLayoutManager(this);
        studentsList = new ArrayList<>();
        studentsAdapter = new AddStudentToClassAdapter(AddStudentToClass.this, studentsList);

        title.setText("Add Students to " + classId);

        // Students displayed thru RecyclerView
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(studentsAdapter);

        // DB events
        courseDr.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                courseSelection.clear();

                for (DataSnapshot courses : dataSnapshot.getChildren()) {
                    Courses courseInstance = courses.getValue(Courses.class);
                    courseSelection.add(courses.getKey().toString() + " - " + courseInstance.getCourse());
                }

                ArrayAdapter<String> arrayAdapterCourse = new ArrayAdapter<String>(AddStudentToClass.this, android.R.layout.simple_spinner_item, courseSelection);
                arrayAdapterCourse.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                filterCourse.setAdapter(arrayAdapterCourse);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        // filter spinner events
        filterCourse.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                blockSelection.clear();
                hashedBlocks.clear();
                Query query = blockDr.orderByChild("courseId").equalTo(filterCourse.getSelectedItem().toString().split(" - ")[0]);
                query.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            for (DataSnapshot blocks : dataSnapshot.getChildren()) {
                                Blocks blocksInstance = blocks.getValue(Blocks.class);
                                blockSelection.add(blocksInstance.getBlock());
                                hashedBlocks.add(new Blocks(blocksInstance.getBlock(), blocksInstance.getCourseId(), blocks.getKey()));
                            }

                            ArrayAdapter<String> arrayAdapterCourse = new ArrayAdapter<String>(AddStudentToClass.this, android.R.layout.simple_spinner_item, blockSelection);
                            arrayAdapterCourse.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            filterBlock.setAdapter(arrayAdapterCourse);
                        } else {
                            filterBlock.setAdapter(null);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        filterBlock.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Query query = studentDr.orderByChild("blockId").equalTo(hashedBlocks.get(filterBlock.getSelectedItemPosition()).getUid());
                query.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        studentsList.clear();
                        if (dataSnapshot.exists()) {
                            for (DataSnapshot studentSnapshots : dataSnapshot.getChildren()) {
                                Students student = studentSnapshots.getValue(Students.class);
                                studentsList.add(student);
                            }
                        }
                        studentsAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AddStudentTask().execute();
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

    public class AddStudentTask extends AsyncTask<Void, Void, Void> {

        ArrayList<ClassStudents> list;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            list = new ArrayList<>();
            if (studentsAdapter.getSelected().size() > 0) {
                for (int i=0; i<studentsAdapter.getSelected().size(); i++) {
                    list.add(new ClassStudents(System.currentTimeMillis() + "" + (int)(Math.random()*500),
                            classId,
                            studentsAdapter.getSelected().get(i).getId(),
                            studentsAdapter.getSelected().get(i).getLastname() + ", " + studentsAdapter.getSelected().get(i).getFirstname() + " " + studentsAdapter.getSelected().get(i).getMiddlename(),
                            true));
                }
            } else {
                Toast.makeText(AddStudentToClass.this, "Select at least student to add.", Toast.LENGTH_LONG).show();
            }
        }

        @Override
        protected Void doInBackground(Void... voids) {

            DatabaseReference studentsSubjects = FirebaseDatabase.getInstance().getReference().child("StudentSubjects");

            for (ClassStudents students : list) {
                classStudentsDr.child(students.getId()).setValue(students);
                studentsSubjects.child(students.getStudentId()).child(classId).setValue(new StudentSubjects(classId, "", bundle.getString("YEAR")));
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Toast.makeText(AddStudentToClass.this, "Students added successfully!", Toast.LENGTH_LONG).show();
            finish();
        }
    }
}
