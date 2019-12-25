package com.example.codestates.ContentList;

import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bignerdranch.expandablerecyclerview.ParentViewHolder;
import com.example.codestates.R;

public class ContainerViewHolder extends ParentViewHolder {

    private TextView mTextView;
    private ImageView mImageView;

    public ContainerViewHolder(@NonNull View itemView) {
        super(itemView);

        mTextView = itemView.findViewById(R.id.container_title);
        mImageView = itemView.findViewById(R.id.container_icon);
    }


    public void bind(ContainerViewModel model){
        mTextView.setText(model.getName());
        mImageView.setImageResource(model.getImg());
    }
}
