package com.example.tt;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.tt.model.Data;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class moim_moim extends Fragment {

    private JSONObject cat_json = null;
    private JSONArray cat_arr = null;
    final url_json read = new url_json();
    private MoimAdapter adapter;
    private RecyclerView recycleView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_moim_moim, container, false);
        adapter = new MoimAdapter();
        recycleView = (RecyclerView) rootView.findViewById(R.id.recycleView);

        int spanCount = 2; // columns
        int spacing = 50; // 50px
        boolean includeEdge = true;

        recycleView.addItemDecoration(new MoimDecoration(spanCount, spacing, includeEdge));
        recycleView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        recycleView.setHasFixedSize(true);
        //recycleView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recycleView.setAdapter(adapter);

        final String url = "http://52.79.125.108/api/assemble";
        final String userUrl = "http://52.79.125.108/users/";
        JSONObject temparr = null;
        try {
            cat_json = read.readJsonFromUrl(url);
            cat_arr = new JSONArray(cat_json.get("temp").toString());
            for (int i = 0; i < cat_arr.length(); i++) {
                JSONObject temp = (JSONObject) cat_arr.get(i);
                Data data = new Data();
                data.setTitle(temp.get("title").toString());
                data.setContent(temp.get("content").toString());
                data.setUrlImage(temp.get("photo").toString());

                String[] temp_array = temp.get("time").toString().split("T");
                String moim_act_time_str = temp_array[0] + "-" + temp_array[1];
                Date moim_act_time = new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss").parse(moim_act_time_str);
                SimpleDateFormat format = new SimpleDateFormat("yyyy년MM월dd일HH시mm분");
                String print =  format.format(moim_act_time);

                data.setDate(print);
                data.setId(temp.get("id").toString());
                JSONObject tempobject = read.readJsonFromUrl(userUrl + temp.get("author").toString());
                temparr = new JSONObject(tempobject.get("temp").toString());
                data.setAuthor(temparr.get("username").toString());
                adapter.addItem(data);
                //data.setId(Integer.parseInt(temp.get("id").toString()));

            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }catch (ParseException e){
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

