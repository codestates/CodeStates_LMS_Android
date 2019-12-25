package com.example.codestates.CourseList;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.codestates.R;

public class CourseViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    ImageView mImageView;
    TextView mTitle,mDescription;
    CourseAdapter.OnCourseListener onCourseListener;

    public CourseViewHolder(@NonNull View itemView, CourseAdapter.OnCourseListener listener) {
        super(itemView);

        mImageView = itemView.findViewById(R.id.course_image);
        mTitle = itemView.findViewById(R.id.course_title);
        mDescription = itemView.findViewById(R.id.course_description);
        onCourseListener = listener;

        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        onCourseListener.onCourseClick(getAdapterPosition());
    }
}
