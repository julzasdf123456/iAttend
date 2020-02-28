package com.lopez.myattendance;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class AttendanceAdapter extends RecyclerView.Adapter<AttendanceAdapter.ViewHolder> {

    private Context context;
    private List<ClassStudents> classStudentsList;
    public DatabaseReference dr;

    public AttendanceAdapter(Context context, List<ClassStudents> classStudentsList, String subject) {
        this.context = context;
        this.classStudentsList = classStudentsList;
        dr = FirebaseDatabase.getInstance().getReference().child("ClassStudents").child(subject);
    }

    @NonNull
    @Override
    public AttendanceAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.attendance_recyclerview_holder, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final AttendanceAdapter.ViewHolder holder, final int position) {
        holder.bind(classStudentsList.get(position));

        holder.attendanceNameOptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //creating a popup menu
                PopupMenu popup = new PopupMenu(context, holder.attendanceNameOptions);
                //inflating menu from xml resource
                popup.inflate(R.menu.delete_student_menu);
                //adding click listener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.deleteStudent:
                                dr.child(classStudentsList.get(position).getId()).removeValue();
                                notifyItemRemoved(position);
                                break;
                        }
                        return false;
                    }
                });
                //displaying the popup
                popup.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return classStudentsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView attendanceName, attendanceNameOptions;
        public ImageView checker;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            attendanceName = (TextView) itemView.findViewById(R.id.attendanceName);
            attendanceNameOptions = (TextView) itemView.findViewById(R.id.attendanceNameOptions);
            checker = (ImageView) itemView.findViewById(R.id.checker);
        }

        public void bind(final ClassStudents students) {
            checker.setBackgroundResource(students.isChecked ? R.drawable.ic_check_box_black_24dp : R.drawable.ic_check_box_outline_blank_black_24dp);
            attendanceName.setText(students.getStudentDisplayName());

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    students.setChecked(!students.isChecked());
                    checker.setBackgroundResource(students.isChecked ? R.drawable.ic_check_box_black_24dp : R.drawable.ic_check_box_outline_blank_black_24dp);
                }
            });
        }
    }

    public ArrayList<ClassStudents> getSelected() {
        ArrayList<ClassStudents> selected = new ArrayList<>();
        for (int i = 0; i < classStudentsList.size(); i++) {
            if (classStudentsList.get(i).isChecked()) {
                selected.add(classStudentsList.get(i));
            }
        }
        return selected;
    }
}
