package com.example.tt;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class profileRequest extends StringRequest {
    public static void setURL(String id) {
        profileRequest.URL = profileRequest.URL + id;
    }

    static private String URL = "http://52.79.125.108/api/detail/";
    private Map<String, String> parameters;

    public profileRequest(String name, String nickname, String birth, Response.Listener<String> listener){
        super(Request.Method.POST, URL, listener, null);//해당 URL에 POST방식으로 파마미터들을 전송함
        parameters = new HashMap<>();
        parameters.put("name", name);
        parameters.put("nickname", nickname);
        parameters.put("birth", birth);
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return parameters;
    }
}
