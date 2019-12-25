package com.example.codestates.Dashboard;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.codestates.R;

import org.w3c.dom.Text;

public class DashViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    TextView firstName;
    TextView lastName;
    TextView inviteCode;
    TextView email;

    DashAdapter.OnDashListener listener;

    public DashViewHolder(@NonNull View itemView, DashAdapter.OnDashListener listener) {
        super(itemView);

        firstName = itemView.findViewById(R.id.firstname);
        lastName = itemView.findViewById(R.id.lastname);
        inviteCode = itemView.findViewById(R.id.invitecode);
        email = itemView.findViewById(R.id.email);

        this.listener=listener;

        itemView.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        listener.onUserClick(getAdapterPosition());
    }
}
