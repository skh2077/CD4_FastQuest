package com.example.tt;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tt.model.Data;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Random;

public class moim_activity extends Fragment {


    public static moim_activity newInstance() {
        return new moim_activity();
    }

    private JSONObject cat_json = null;
    private JSONArray cat_arr = null;
    final url_json read = new url_json();
    private RecyclerAdapter adapter;
    private RecyclerView recycleView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_moim_activity, container, false);
        adapter = new RecyclerAdapter();
        recycleView = (RecyclerView) rootView.findViewById(R.id.recycleView);
        recycleView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recycleView.setAdapter(adapter);

        final String url = "http://52.79.125.108/api/challenge/";

        try {
            cat_json = read.readJsonFromUrl(url);
            cat_arr = new JSONArray(cat_json.get("temp").toString());
            for (int i = 0; i < cat_arr.length(); i++) {
                JSONObject temp = (JSONObject) cat_arr.get(i);
                Data data = new Data();
                data.setTitle(temp.get("cat_name").toString());
                data.setAuthor(temp.get("act_name").toString());
                data.setContent(temp.get("content").toString());
                data.setId(temp.get("id").toString());
                adapter.addItem(data);
                //data.setId(Integer.parseInt(temp.get("id").toString()));

            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        adapter.notifyDataSetChanged();


        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // TODO: Use the ViewModel
    }

}
