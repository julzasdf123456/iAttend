package com.lopez.myattendance;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class ClassesAdapter extends RecyclerView.Adapter<ClassesAdapter.ViewHolder> {

    private List<Classes> classesList;
    private Context context;
    public DatabaseReference dr;

    public ClassesAdapter(List<Classes> classes) {
        classesList = classes;
        dr = FirebaseDatabase.getInstance().getReference().child("Classes");
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View classesView = inflater.inflate(R.layout.classes_recyclerview_holder, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(classesView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        final Classes classes = classesList.get(position);

        holder.classSubjectName.setText(classes.getSubjectId());
        holder.classSchedule.setText(classes.getSchedule());
        holder.classYear.setText(classes.getYear());

        holder.classOptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //creating a popup menu
                PopupMenu popup = new PopupMenu(context, holder.classOptions);
                //inflating menu from xml resource
                popup.inflate(R.menu.delete_student_menu);
                //adding click listener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.deleteStudent:
                                dr.child(classes.getId()).removeValue();
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
        return classesList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView classSubjectName, classSchedule, classYear, classOptions;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            classSubjectName = (TextView) itemView.findViewById(R.id.classSubjectName);
            classSchedule = (TextView) itemView.findViewById(R.id.classSchedule);
            classYear = (TextView) itemView.findViewById(R.id.classYear);
            classOptions = (TextView) itemView.findViewById(R.id.classOptions);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    Intent intent = new Intent(context, Attendance.class);
                    intent.putExtra("TEACHER", classesList.get(position).getTeacherEmail());
                    intent.putExtra("SUBJECT", classesList.get(position).getSubjectId());
                    intent.putExtra("YEAR", classesList.get(position).getYear());
                    context.startActivity(intent);
                }
            });
        }
    }
}
