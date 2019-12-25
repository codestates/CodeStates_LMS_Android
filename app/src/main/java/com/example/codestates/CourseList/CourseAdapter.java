package com.example.codestates.CourseList;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.codestates.R;

import java.util.LinkedList;

public class CourseAdapter extends RecyclerView.Adapter<CourseViewHolder> {

    Context context;
    LinkedList<CourseViewModel> list;
    OnCourseListener onCourseListener;

    public CourseAdapter(Context context, OnCourseListener onCourseListener){
        this.context=context;
        this.list=new LinkedList<CourseViewModel>();
        this.onCourseListener = onCourseListener;
    }

    @NonNull
    @Override
    public CourseViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_course,null);
        return new CourseViewHolder(view, onCourseListener);
    }

    @Override
    public void onBindViewHolder(@NonNull CourseViewHolder courseViewHolder, int i) {
        courseViewHolder.mTitle.setText(list.get(i).getTitle());
        courseViewHolder.mDescription.setText(list.get(i).getDescription());
        courseViewHolder.mImageView.setImageResource(list.get(i).getImg());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public interface OnCourseListener{
        void onCourseClick(int position);
    }


    public LinkedList<CourseViewModel> getList(){
        return list;
    }
    public void setList(LinkedList<CourseViewModel> list){
        this.list = list;
    }
}
