package com.lopez.myattendance;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;

public class Attendance extends AppCompatActivity {

    public Toolbar toolbar;
    public TextView attendanceTitlebar, attendanceDay;
    public Bundle bundle;

    public FloatingActionButton floatingActionButton;

    public RecyclerView recyclerView;
    public RecyclerView.LayoutManager layoutManager;
    public AttendanceAdapter attendanceAdapter;
    public ArrayList<ClassStudents> classStudentsArrayList;

    public DatabaseReference classStudentsDr, attendanceDr;
    public String classId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance);

        //custom toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbarAttendance);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_white_24dp);

        // INITS
        attendanceTitlebar = (TextView) findViewById(R.id.attendanceTitleBar);
        bundle = getIntent().getExtras();
        classId = bundle.getString("SUBJECT");
        attendanceDay = (TextView) findViewById(R.id.attendanceDate);
        floatingActionButton = (FloatingActionButton) findViewById(R.id.addToAttendanceButton);
        recyclerView = (RecyclerView) findViewById(R.id.attendanceRecyclerView);
        layoutManager = new LinearLayoutManager(Attendance.this);
        classStudentsArrayList = new ArrayList<>();
        attendanceAdapter = new AttendanceAdapter(Attendance.this, classStudentsArrayList, classId);
        classStudentsDr = FirebaseDatabase.getInstance().getReference().child("ClassStudents").child(classId);
        attendanceDr = FirebaseDatabase.getInstance().getReference().child("Attendance").child(classId).child(getDateToday());

        // setting title
        attendanceTitlebar.setText(bundle.getString("SUBJECT") + " Attendance");
        recyclerView.setAdapter(attendanceAdapter);
        recyclerView.setLayoutManager(layoutManager);
        attendanceDay.setText(getDateToday());

        classStudentsDr.addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                classStudentsArrayList.clear();
                if (dataSnapshot.exists()) {
                    for (DataSnapshot studentsSnapshot : dataSnapshot.getChildren()) {
                        ClassStudents students = studentsSnapshot.getValue(ClassStudents.class);
                        classStudentsArrayList.add(students);
                    }
                    classStudentsArrayList.sort(new Comparator<ClassStudents>() {
                        @Override
                        public int compare(ClassStudents classStudents, ClassStudents t1) {
                            return classStudents.getStudentDisplayName().compareTo(t1.getStudentDisplayName());
                        }
                    });
                    attendanceAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (attendanceAdapter.getSelected().size() > 0) {
                    new InsertAttendance().execute();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.attendance_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        } else if (item.getItemId() == R.id.students) {
            startActivity(new Intent(Attendance.this, StudentsActivity.class));
        } else if (item.getItemId() == R.id.addAttendanceStudents) {
            Intent intent = new Intent(Attendance.this, AddStudentToClass.class);
            intent.putExtra("SUBJECT", bundle.getString("SUBJECT"));
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    public String getDateToday() {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("MMMM dd, yyyy");
            Date today = new Date();
            return sdf.format(today);
        } catch (Exception e) {
            Log.e("ERR", e.getMessage());
            return null;
        }
    }

    public class InsertAttendance extends AsyncTask<Void, Void, Void> {

        ArrayList<Attendances> list;
        String today;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            list = new ArrayList<>();
            today = getDateToday();
            if (attendanceAdapter.getSelected().size() > 0) {
                for (int i=0; i<attendanceAdapter.getSelected().size(); i++) {
                    list.add(new Attendances(System.currentTimeMillis() + "" + (int)(Math.random()*500),
                            classId,
                            classStudentsArrayList.get(i).getStudentId(),
                            today,
                            true));
                }
            } else {
                Toast.makeText(Attendance.this, "Select at least student to add.", Toast.LENGTH_LONG).show();
            }
        }

        @Override
        protected Void doInBackground(Void... voids) {
            for (Attendances attendances : list) {
                attendanceDr.child(attendances.getId()).setValue(attendances);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Toast.makeText(Attendance.this, "Attendance flushed successfully", Toast.LENGTH_LONG).show();
            finish();
        }
    }
}
