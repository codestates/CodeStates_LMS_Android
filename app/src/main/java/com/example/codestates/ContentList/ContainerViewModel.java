package com.example.codestates.ContentList;

import com.bignerdranch.expandablerecyclerview.model.Parent;

import java.util.LinkedList;
import java.util.List;

public class ContainerViewModel implements Parent<ContentViewModel> {

    private LinkedList<ContentViewModel> list;
    private String name;
    private int img;

    public ContainerViewModel(String name,LinkedList<ContentViewModel> list){
        this.list = list;
        this.name = name;
    }

    @Override
    public List<ContentViewModel> getChildList() {
        return list;
    }

    @Override
    public boolean isInitiallyExpanded() {
        return false;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }
}
