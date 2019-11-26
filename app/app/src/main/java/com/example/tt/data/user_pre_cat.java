package com.example.tt.data;
import android.widget.ToggleButton;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

public class user_pre_cat {


    private String user_id;
    private String cat_name;

    public user_pre_cat() {

    }

    public user_pre_cat(String User_id, String Cat_name) {
        this.user_id = User_id;
        this.cat_name = Cat_name;
    }

    public user_pre_cat(JSONObject user_precat) throws JSONException {
        this.user_id = user_precat.get("user_id").toString();
        this.cat_name = user_precat.get("cat_name").toString();
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getCat_name() {
        return cat_name;
    }

    public void setCat_name(String cat_name) {
        this.cat_name = cat_name;
    }
}
