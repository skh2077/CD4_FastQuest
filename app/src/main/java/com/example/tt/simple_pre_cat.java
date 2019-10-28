package com.example.tt;
import android.widget.ToggleButton;

import androidx.appcompat.app.AppCompatActivity;

public class simple_pre_cat {


    private int user_id;
    private String cat_name;

    public simple_pre_cat(int User_id, String Cat_name) {
        this.user_id = User_id;
        this.cat_name = Cat_name;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getCat_name() {
        return cat_name;
    }

    public void setCat_name(String cat_name) {
        this.cat_name = cat_name;
    }
}
