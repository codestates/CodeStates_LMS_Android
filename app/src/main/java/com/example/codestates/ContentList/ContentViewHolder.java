package com.example.codestates.ContentList;

import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.bignerdranch.expandablerecyclerview.ChildViewHolder;
import com.example.codestates.R;

public class ContentViewHolder extends ChildViewHolder {

    private TextView nameText;
    private TextView textText;

    public ContentViewHolder(@NonNull View itemView) {
        super(itemView);

        nameText = itemView.findViewById(R.id.content_title);
    }

    public void bind(ContentViewModel model){
        nameText.setText(model.getName());
    }
}
