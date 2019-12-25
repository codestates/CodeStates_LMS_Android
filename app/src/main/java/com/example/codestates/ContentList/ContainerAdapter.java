package com.example.codestates.ContentList;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bignerdranch.expandablerecyclerview.ExpandableRecyclerAdapter;
import com.example.codestates.CourseList.CourseViewModel;
import com.example.codestates.R;

import java.util.LinkedList;
import java.util.List;

public class ContainerAdapter extends ExpandableRecyclerAdapter<ContainerViewModel,ContentViewModel,ContainerViewHolder,ContentViewHolder> {

    private LayoutInflater mInflater;

    public ContainerAdapter(Context context, @NonNull List<ContainerViewModel> parentList) {
        super(parentList);
        mInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ContainerViewHolder onCreateParentViewHolder(@NonNull ViewGroup parentViewGroup, int viewType) {
        View containerView = mInflater.inflate(R.layout.list_parent, parentViewGroup, false);
        return new ContainerViewHolder(containerView);
    }

    @NonNull
    @Override
    public ContentViewHolder onCreateChildViewHolder(@NonNull ViewGroup childViewGroup, int viewType) {
        View contentView = mInflater.inflate(R.layout.list_child,childViewGroup,false);
        return new ContentViewHolder(contentView);
    }

    @Override
    public void onBindParentViewHolder(@NonNull ContainerViewHolder parentViewHolder, int parentPosition, @NonNull ContainerViewModel parent) {
        parentViewHolder.bind(parent);
    }

    @Override
    public void onBindChildViewHolder(@NonNull ContentViewHolder childViewHolder, int parentPosition, int childPosition, @NonNull ContentViewModel child) {
        childViewHolder.bind(child);
    }

}
