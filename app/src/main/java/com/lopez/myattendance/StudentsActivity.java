package com.lopez.myattendance;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class StudentsActivity extends AppCompatActivity {

    public Toolbar toolbar;
    public Spinner filterCourse, filterBlock;
    public FloatingActionButton newStudent;
    public RecyclerView recyclerView;
    public RecyclerView.LayoutManager layoutManager;
    public StudentsAdapter studentsAdapter;
    public List<Students> studentsList;

    public DatabaseReference courseDr, blockDr, studentDr;
    public FirebaseFirestore firestore;

    public ArrayList<String> courseSelection = new ArrayList<>();
    public ArrayList<String> blockSelection = new ArrayList<>();

    public long maxCourse = 0, maxBlock = 0, maxStudent = 0;
    public ArrayList<Blocks> hashedBlocks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_students);

        //custom toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbarStudents);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_white_24dp);

        // inits
        firestore = FirebaseFirestore.getInstance();
        courseDr = FirebaseDatabase.getInstance().getReference().child("Courses");
        blockDr = FirebaseDatabase.getInstance().getReference().child("Blocks");
        studentDr = FirebaseDatabase.getInstance().getReference().child("Students");
        filterBlock = (Spinner) findViewById(R.id.blockSelect);
        filterCourse = (Spinner) findViewById(R.id.courseSelect);
        newStudent = (FloatingActionButton) findViewById(R.id.newStudentButton);
        hashedBlocks = new ArrayList<>();
        recyclerView = (RecyclerView) findViewById(R.id.studentsRecyclerView);
        layoutManager = new LinearLayoutManager(this);
        studentsList = new ArrayList<>();
        studentsAdapter = new StudentsAdapter(studentsList);

        // Students displayed thru RecyclerView
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(studentsAdapter);

        // DB events
        courseDr.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                courseSelection.clear();
                maxCourse = (dataSnapshot.getChildrenCount());

                for (DataSnapshot courses : dataSnapshot.getChildren()) {
                    Courses courseInstance = courses.getValue(Courses.class);
                    courseSelection.add(courses.getKey().toString() + " - " + courseInstance.getCourse());
                }

                ArrayAdapter<String> arrayAdapterCourse = new ArrayAdapter<String>(StudentsActivity.this, android.R.layout.simple_spinner_item, courseSelection);
                arrayAdapterCourse.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                filterCourse.setAdapter(arrayAdapterCourse);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        blockDr.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                maxBlock = (dataSnapshot.getChildrenCount());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        Query queryStudent = studentDr.orderByKey().limitToLast(1);
        queryStudent.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot studentSnap : dataSnapshot.getChildren()) {
                        Students student = studentSnap.getValue(Students.class);
                        maxStudent = Integer.valueOf(student.getId());
                    }

                } else {
                    maxStudent = 0;
                }
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

                            ArrayAdapter<String> arrayAdapterCourse = new ArrayAdapter<String>(StudentsActivity.this, android.R.layout.simple_spinner_item, blockSelection);
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

        // button events
        newStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addNewStudent();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.students_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        } else if (item.getItemId() == R.id.addCourse) {
            addCourse();
        } else if (item.getItemId() == R.id.addBlock) {
            addBlock();
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Adding course via Alert dialog
     * @returns NULL
     */
    public void addCourse() {
        try {
            LayoutInflater li = LayoutInflater.from(StudentsActivity.this);
            View promptsView = li.inflate(R.layout.new_course, null);

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                    StudentsActivity.this);

            // set prompts.xml to alertdialog builder
            alertDialogBuilder.setView(promptsView);

            final EditText newCourseName = (EditText) promptsView
                    .findViewById(R.id.newCourseName);
            final EditText newCourseDescription = (EditText) promptsView
                    .findViewById(R.id.newCourseDescription);
            final Spinner newCourseYear = (Spinner) promptsView
                    .findViewById(R.id.newCourseYear);

            ArrayList<String> years = new ArrayList<>();
            years.add("1");
            years.add("2");
            years.add("3");
            years.add("4");
            years.add("5");
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(StudentsActivity.this, android.R.layout.simple_spinner_item, years);
            arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            newCourseYear.setAdapter(arrayAdapter);

            // set dialog message
            alertDialogBuilder
                    .setTitle("Add Course")
                    .setCancelable(false)
                    .setPositiveButton("OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    Courses course = new Courses(newCourseName.getText().toString(), newCourseDescription.getText().toString(), newCourseYear.getSelectedItem().toString());
                                    courseDr.child(String.valueOf(maxCourse+1)).setValue(course);
                                    Toast.makeText(StudentsActivity.this, "Course " + newCourseName.getText().toString() + " added successfully!", Toast.LENGTH_SHORT).show();
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
        } catch (Exception e) {
            Log.e("ERR", e.getMessage());
        }
    }

    /**
     * Adding block via Alert dialog
     * @returns NULL
     */
    public void addBlock() {
        try {
            LayoutInflater li = LayoutInflater.from(StudentsActivity.this);
            View promptsView = li.inflate(R.layout.new_block, null);

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                    StudentsActivity.this);

            // set prompts.xml to alertdialog builder
            alertDialogBuilder.setView(promptsView);

            final EditText newBlockName = (EditText) promptsView
                    .findViewById(R.id.newBlockName);
            final Spinner newBlockCourse = (Spinner) promptsView
                    .findViewById(R.id.newBlockCourse);

            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(StudentsActivity.this, android.R.layout.simple_spinner_item, courseSelection);
            arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            newBlockCourse.setAdapter(arrayAdapter);

            // set dialog message
            alertDialogBuilder
                    .setTitle("Add Block")
                    .setCancelable(false)
                    .setPositiveButton("OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    Blocks blocks = new Blocks(newBlockName.getText().toString(), newBlockCourse.getSelectedItem().toString().split(" - ")[0]);
                                    blockDr.child(String.valueOf(maxBlock+1)).setValue(blocks);
                                    Toast.makeText(StudentsActivity.this, "Block " + newBlockName.getText().toString() + " added successfully!", Toast.LENGTH_SHORT).show();
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
        } catch (Exception e) {
            Log.e("ERR", e.getMessage());
        }
    }

    /**
     * Adding studeng via Alert dialog
     */
    public void addNewStudent() {
        try {
            LayoutInflater li = LayoutInflater.from(StudentsActivity.this);
            View promptsView = li.inflate(R.layout.new_student, null);

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                    StudentsActivity.this);

            alertDialogBuilder.setView(promptsView);

            final EditText firstnameSlot = (EditText) promptsView
                    .findViewById(R.id.firstname);
            final EditText middlenameSlot = (EditText) promptsView
                    .findViewById(R.id.middlename);
            final EditText lastnameSlot = (EditText) promptsView
                    .findViewById(R.id.lastname);
            final EditText addressSlot = (EditText) promptsView
                    .findViewById(R.id.address);
            final EditText parentContactSlot = (EditText) promptsView
                    .findViewById(R.id.parentContact);

            // set dialog message
            alertDialogBuilder
                    .setTitle("New Student for " + filterCourse.getSelectedItem().toString().split(" - ")[1] + " - " + filterBlock.getSelectedItem().toString() + " (" + hashedBlocks.get(filterBlock.getSelectedItemPosition()).getUid() + ")")
                    .setCancelable(false)
                    .setPositiveButton("OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    Students student = new Students(firstnameSlot.getText().toString(),
                                                                    middlenameSlot.getText().toString(),
                                                                    lastnameSlot.getText().toString(),
                                                                    addressSlot.getText().toString(),
                                                                    parentContactSlot.getText().toString(),
                                                                    hashedBlocks.get(filterBlock.getSelectedItemPosition()).getUid(),
                                                                    String.valueOf(maxStudent+1));
                                    studentDr.child(String.valueOf(maxStudent+1)).setValue(student);
                                    Toast.makeText(StudentsActivity.this, "New student added successfully!", Toast.LENGTH_SHORT).show();
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
        } catch (Exception e) {
            Log.e("ERR", e.getMessage());
        }
    }
}
