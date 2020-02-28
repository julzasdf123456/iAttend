package com.lopez.myattendance;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class AddStudentToClassAdapter extends RecyclerView.Adapter<AddStudentToClassAdapter.ViewHolder> {

    private Context context;
    private List<Students> classStudentsList;

    public AddStudentToClassAdapter(Context context, List<Students> classStudentsList) {
        this.context = context;
        this.classStudentsList = classStudentsList;
    }

    @NonNull
    @Override
    public AddStudentToClassAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.attendance_recyclerview_holder, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AddStudentToClassAdapter.ViewHolder holder, int position) {
        holder.bind(classStudentsList.get(position));
    }

    @Override
    public int getItemCount() {
        return classStudentsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView attendanceName;
        public ImageView checker;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            attendanceName = (TextView) itemView.findViewById(R.id.attendanceName);
            checker = (ImageView) itemView.findViewById(R.id.checker);
        }

        public void bind(final Students students) {
            checker.setBackgroundResource(students.isChecked ? R.drawable.ic_check_box_black_24dp : R.drawable.ic_check_box_outline_blank_black_24dp);
            attendanceName.setText(students.getLastname() + ", " + students.getFirstname() + " " + students.getMiddlename());

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    students.setChecked(!students.isChecked());
                    checker.setBackgroundResource(students.isChecked ? R.drawable.ic_check_box_black_24dp : R.drawable.ic_check_box_outline_blank_black_24dp);
                }
            });
        }
    }

    public ArrayList<Students> getSelected() {
        ArrayList<Students> selected = new ArrayList<>();
        for (int i = 0; i < classStudentsList.size(); i++) {
            if (classStudentsList.get(i).isChecked()) {
                selected.add(classStudentsList.get(i));
            }
        }
        return selected;
    }
}
