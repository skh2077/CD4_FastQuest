package com.example.tt;

import android.app.Activity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.tt.data.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class Edit_score extends Activity {
    User user;
    Activity activity;

    public Edit_score(Activity activity) {
        this.activity = activity;
    }

    public void edit_score(String user_name, int score) {
        // 수정하면 유저 네임 받으면 통신하는게 완성 됨
        int user_score = 0;
        user = User.getInstance();
        Response.Listener<JSONObject> pjresponseListener = new com.android.volley.Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    user.setScore(Integer.parseInt(response.get("score").toString()));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        String URL = "http://52.79.125.108/api/user/" +  user_name;
        url_json read = new url_json();
        JSONObject add_score = new JSONObject();
        try {
            JSONObject user_info = read.readJsonFromUrl(URL);
            JSONObject temp = new JSONObject(user_info.get("temp").toString());
            user_score = Integer.parseInt(temp.get("score").toString());
            add_score.put("score", user_score +score);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        addpointRequest addpointrequest = new addpointRequest(Request.Method.POST, add_score, URL, pjresponseListener, null);
        RequestQueue pointrequestQueue = Volley.newRequestQueue(this.activity);
        pointrequestQueue.add(addpointrequest);
    }
}
