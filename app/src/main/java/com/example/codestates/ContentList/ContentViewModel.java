package com.example.codestates.ContentList;

public class ContentViewModel {

    private String name;
    private String text;

    public ContentViewModel(String name, String text){
        this.name = name;
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
