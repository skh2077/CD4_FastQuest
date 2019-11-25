package com.example.tt;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by kch on 2018. 5. 14..
 */

public class loginRequest extends StringRequest {

    final static private String URL = "http://52.79.125.108/rest-auth/login/";
    private Map<String, String> parameters;

    public loginRequest(String userID, String userPassword, Response.Listener<String> listener){
        super(Method.POST, URL, listener, null);//해당 URL에 POST방식으로 파마미터들을 전송함
        parameters = new HashMap<>();
        parameters.put("username", userID);
        parameters.put("password", userPassword);
        //parameters.put("userGender", userGender);
        //parameters.put("email", userEmail);
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return parameters;
    }
}