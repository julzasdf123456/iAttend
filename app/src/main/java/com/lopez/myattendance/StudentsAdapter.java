package com.lopez.myattendance;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class StudentsAdapter extends RecyclerView.Adapter<StudentsAdapter.ViewHolder> {

    private List<Students> studentsList;
    private Context context;
    public DatabaseReference dr;

    public StudentsAdapter(List<Students> students) {
        studentsList = students;
        dr = FirebaseDatabase.getInstance().getReference().child("Students");
    }

    @NonNull
    @Override
    public StudentsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View studentsView = inflater.inflate(R.layout.students_recyclerview_holder, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(studentsView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final StudentsAdapter.ViewHolder holder, final int position) {
        final Students student = studentsList.get(position);

        holder.name.setText(student.getId() + ". " + student.getLastname() + ", " + student.getFirstname() + " " + student.getMiddlename());
        holder.address.setText(student.getAddress());
        holder.contact.setText(student.getParentContact());

        holder.studentOptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //creating a popup menu
                PopupMenu popup = new PopupMenu(context, holder.studentOptions);
                //inflating menu from xml resource
                popup.inflate(R.menu.delete_student_menu);
                //adding click listener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.deleteStudent:
                                dr.child(student.getId()).removeValue();
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
        return studentsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView name, address, contact, studentOptions;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.studentName);
            address = (TextView) itemView.findViewById(R.id.studentAddress);
            contact = (TextView) itemView.findViewById(R.id.studentParentContact);
            studentOptions = (TextView) itemView.findViewById(R.id.studentOptions);
        }
    }
}
