package com.example.tt;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class moimRequest extends JsonObjectRequest {
    final static private String URL = "http://52.79.125.108/api/assemble/";
    private HashMap<String, String> params;

    public moimRequest(int method, String url, JSONObject jsonRequest, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener){
        super(method, url, jsonRequest, listener, errorListener);
    }

    /*public ReviewRequest(String review_title, String review_content, String photostring, Response.Listener<String> listener){
        //super(Method.POST, URL, listener, null);//해당 URL에 POST방식으로 파마미터들을 전송함
        String photo_string = photostring;
        parameters = new HashMap<>();
        parameters.put("id", "100");
        parameters.put("title", "sdfad");
        parameters.put("content", "dsafaaaaa");
        parameters.put("image", "sdfafasfdfa");
        parameters.put("act", "101");
        parameters.put("author", "102");
    }*/

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        params = new HashMap<>();
        return params;
    }
}





