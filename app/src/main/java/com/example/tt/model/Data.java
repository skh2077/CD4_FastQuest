package com.example.tt.model;

import android.graphics.Bitmap;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Data {

    private String title;
    private String content;
    private String author;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {

        this.date = date;
    }

    private String date;
    private int resId;
    private Bitmap image;
    private String urlImage;
    private boolean isliked;
    private String id;


    public boolean getIsliked() {
        return isliked;
    }

    public void setIsliked(boolean isliked) {
        this.isliked = isliked;
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getUrlImage() {
        return urlImage;
    }

    public void setUrlImage(String urlImage) {
        this.urlImage = urlImage;
    }


    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }



    public Bitmap getImage() { return image; }

    public void setImage(Bitmap image) { this.image = image; }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getResId() {
        return resId;
    }

    public void setResId(int resId) {
        this.resId = resId;
    }
}