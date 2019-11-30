package com.example.tt;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class pre_cat_Request extends JsonObjectRequest {
    final static private String URL = "http://52.79.125.108/api/user/precat/";
    private HashMap<String, String> params;

    public pre_cat_Request(int method, JSONObject jsonRequest, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener){
        super(method, URL, jsonRequest, listener, errorListener);
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        params = new HashMap<>();
        return params;
    }
}
