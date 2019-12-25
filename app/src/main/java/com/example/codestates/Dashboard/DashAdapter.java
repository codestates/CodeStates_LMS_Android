package com.example.codestates.Dashboard;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.codestates.CourseList.CourseAdapter;
import com.example.codestates.CourseList.CourseViewHolder;
import com.example.codestates.CourseList.CourseViewModel;
import com.example.codestates.R;

import java.util.LinkedList;

public class DashAdapter extends RecyclerView.Adapter<DashViewHolder> {

    Context context;
    private LinkedList<DashViewModel> list;
    DashAdapter.OnDashListener onDashListener;


    public DashAdapter(Context context, OnDashListener listener){
        onDashListener = listener;
        this.context=context;
        this.list = new LinkedList<DashViewModel>();
    }


    @NonNull
    @Override
    public DashViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_dashboard,null);
        return new DashViewHolder(view, onDashListener);
    }

    @Override
    public void onBindViewHolder(@NonNull DashViewHolder dashViewHolder, int i) {
        dashViewHolder.firstName.setText(list.get(i).getFirstname());
        dashViewHolder.lastName.setText(list.get(i).getLastname());
        dashViewHolder.email.setText(list.get(i).getEmail());
        dashViewHolder.inviteCode.setText(list.get(i).getInvitecode());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public LinkedList<DashViewModel> getList() {
        return list;
    }

    public void setList(LinkedList<DashViewModel> list) {
        this.list = list;
    }

    public interface OnDashListener{
        void onUserClick(int position);
    }
}
