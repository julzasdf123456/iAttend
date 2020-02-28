package com.lopez.myattendance;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class SubjectsAdapter extends RecyclerView.Adapter<SubjectsAdapter.ViewHolder> {

    private List<Subjects> subjectsList;
    private Context context;

    public SubjectsAdapter(List<Subjects> subjectsList) {
        this.subjectsList = subjectsList;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView subjectName, subjectDetails;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            subjectName = (TextView) itemView.findViewById(R.id.subjectTitle);
            subjectDetails = (TextView) itemView.findViewById(R.id.subjectDescription);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View subjectsView = inflater.inflate(R.layout.subjects_recyclerview_holder, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(subjectsView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Subjects subject = subjectsList.get(position);
        TextView subjectName = holder.subjectName;
        TextView subjectDetails = holder.subjectDetails;

        subjectName.setText(subject.getSubjectName());
        subjectDetails.setText(subject.getSubjectDescription());
    }

    @Override
    public int getItemCount() {
        return subjectsList.size();
    }
}
