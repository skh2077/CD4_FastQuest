package com.example.tt.data;

import org.json.JSONException;
import org.json.JSONObject;

public class category {
    public String cat_name;
    public String Lcat_name;
    public float activity_rate;
    public float sociality_rate;
    public float score;

    public String getCat_all() {
        return cat_all;
    }

    public void setCat_all(String cat_all) {
        this.cat_all = cat_all;
    }

    public String cat_all;

    public category() {

    }

    public category(String Cat_name, String lcat_name, float Activity_rate, float Sociality_rate, float Score) {
        this.cat_name = Cat_name;
        this.Lcat_name = lcat_name;
        this.activity_rate = Activity_rate;
        this.sociality_rate = Sociality_rate;
        this.score = Score;
    }

    public category(JSONObject jcat) throws JSONException {
        this.cat_name = jcat.get("cat_name").toString();
        this.Lcat_name = jcat.get("lcat_name").toString();
        String handle = jcat.get("activity_rate").toString();
        if (handle == "null") {
            this.activity_rate = 0;
        } else {
            this.activity_rate = Integer.parseInt(handle);
        }
        handle = jcat.get("sociality_rate").toString();
        if (handle == "null") {
            this.sociality_rate = 0;
        } else {
            this.sociality_rate = Integer.parseInt(handle);
        }
    }
}
