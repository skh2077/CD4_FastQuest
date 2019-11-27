package com.example.tt;


import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

/**
 * Created by kch on 2018. 5. 14..
 */

public class ValidateRequest extends StringRequest {

    final static private String URL = "http://52.79.125.108/api/profiles";

    public ValidateRequest(String userID, Response.Listener<String> listener){
        super(Method.GET, URL, listener, null);//해당 URL에 POST방식으로 파마미터들을 전송함
    }

}
