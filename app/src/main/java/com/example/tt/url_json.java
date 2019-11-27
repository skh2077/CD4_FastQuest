package com.example.tt;

import android.os.StrictMode;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;

public class url_json {
    public String jsonReadAll(Reader reader) throws IOException {

        StringBuilder sb = new StringBuilder();

        int cp;

        while ((cp = reader.read()) != -1) {

            sb.append((char) cp);

        }
        String temp = sb.toString();

        return temp;

    }

    public JSONObject readJsonFromUrl(String url) throws IOException, JSONException {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        URLConnection t_connection = new URL(url).openConnection();
        InputStream is = t_connection.getInputStream();
        try {

            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));

            String jsonText = jsonReadAll(rd);

            JSONObject json = new JSONObject();
            json.put("temp",jsonText);

            return json;

        } finally {

            is.close();

        }

    }

}
