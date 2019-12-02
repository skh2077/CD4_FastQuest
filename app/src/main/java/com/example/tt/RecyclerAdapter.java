package com.example.tt;


import android.animation.ValueAnimator;
import android.content.Context;

import android.content.Intent;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.tt.data.User;
import com.example.tt.model.Data;
import com.example.tt.model.FileINfo;
import com.google.gson.JsonObject;
import com.like.IconType;
import com.like.LikeButton;
import com.like.OnLikeListener;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ItemViewHolder> {

    private ArrayList<Data> listData = new ArrayList<>();
    private Context context;
    private SparseBooleanArray selectedItems = new SparseBooleanArray();
    private int prePosition = -1;
    User user = User.getInstance();
    String writen_id;
    String feed_id;
    ArrayList<String> slike_list_save = new ArrayList<String>();
    url_json read = new url_json();
    String url;
    private JSONObject cat_json = null;
    private JSONArray cat_arr = null;
    JSONObject my_review_json;

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        context = parent.getContext();
        slike_list_save.add("tmp");
        add_feed_id();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        holder.onBind(listData.get(position), position);
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    void addItem(Data data) {
        listData.add(data);
    }


    class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView textView1;
        private TextView textView2;
        private TextView textView3;
        private ImageView imageView1;
        private ImageView imageView2;
        private Button button1;
        private LikeButton likebutton;
        private Data data;
        private int position;

        ItemViewHolder(View itemView) {
            super(itemView);

            textView1 = itemView.findViewById(R.id.textView1);
            textView2 = itemView.findViewById(R.id.textView2);
            textView3 = itemView.findViewById(R.id.content);
            likebutton = itemView.findViewById(R.id.thumb);
            imageView1 = itemView.findViewById(R.id.imageView1);
            imageView2 = itemView.findViewById(R.id.imageView2);
        }

        void onBind(Data data, int position) {
            this.data = data;
            this.position = position;

            textView1.setText(data.getTitle());
            textView2.setText(data.getAuthor());
            writen_id = data.getAuthor();
            feed_id = data.getId();
            textView3.setText(data.getContent());
            imageView1.setImageResource(data.getResId());
            //imageView2.setImageResource(data.getResId());
            Picasso.get().load(data.getUrlImage()).into(imageView2);

            likebutton.setIcon(IconType.Thumb);
            likebutton.setScaleX(1.5f);
            likebutton.setScaleY(1.5f);
            //likebutton.setEnabled(true);
            //사진설정

            changeVisibility(selectedItems.get(position));

            itemView.setOnClickListener(this);
            textView1.setOnClickListener(this);
            textView2.setOnClickListener(this);
            imageView1.setOnClickListener(this);


            likebutton.setOnLikeListener(new OnLikeListener() {
                @Override
                public void liked(LikeButton likeButton) {
                    //생김+1
                    if (!slike_contains(feed_id)) {
                        edit_score(writen_id,1);
                        slike_list_save.add(feed_id);

                        Response.Listener<JSONObject> responseListener = new Response.Listener<JSONObject>() {

                            @Override
                            public void onResponse(JSONObject response) {

                            }
                        };//Response.Listener 완료

                        JSONObject jsonObject = new JSONObject();
                        try {
                            jsonObject.put("username", user.getUsername());
                            jsonObject.put("feed", Integer.parseInt(feed_id));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        review_like_Request review_like_request = new review_like_Request(Request.Method.POST, jsonObject, responseListener, null);
                        RequestQueue queue = Volley.newRequestQueue(context);

                        queue.add(review_like_request);
                    }

                }

                @Override
                public void unLiked(LikeButton likeButton) {
                        //사라짐 -1
                    if (slike_contains(feed_id)) {
                        edit_score(writen_id,-1);
                        slike_list_save.remove(feed_id);

                        Response.Listener<JSONObject> responseListener = new Response.Listener<JSONObject>() {

                            @Override
                            public void onResponse(JSONObject response) {

                            }
                        };//Response.Listener 완료

                        JSONObject jsonObject = new JSONObject();
                        try {
                            jsonObject.put("username", user.getUsername());
                            jsonObject.put("feed", Integer.parseInt(feed_id));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        review_like_Request review_like_request = new review_like_Request(Request.Method.DELETE, jsonObject, responseListener, null);
                        RequestQueue queue = Volley.newRequestQueue(context);

                        queue.add(review_like_request);
                    }
                }
            });
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.linearItem:
                    if (selectedItems.get(position)) {
                        selectedItems.delete(position);
                    } else {
                        selectedItems.delete(prePosition);
                        selectedItems.put(position, true);
                    }
                    if (prePosition != -1) notifyItemChanged(prePosition);
                    notifyItemChanged(position);
                    prePosition = position;
                    break;
                case R.id.textView1:
                    Toast.makeText(context, data.getTitle(), Toast.LENGTH_SHORT).show();
                    break;
                case R.id.textView2:
                    Toast.makeText(context, data.getContent(), Toast.LENGTH_SHORT).show();
                    break;
                case R.id.imageView1:
                    Toast.makeText(context, data.getTitle() + " 이미지 입니다.", Toast.LENGTH_SHORT).show();
                    break;
            }
        }


        private void changeVisibility(final boolean isExpanded) {
            int dpValue = 200;
            float d = context.getResources().getDisplayMetrics().density;
            int height = (int) (dpValue * d);



            ValueAnimator va = isExpanded ? ValueAnimator.ofInt(0, height) : ValueAnimator.ofInt(height, 0);

            va.setDuration(600);
            va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    int value = (int) animation.getAnimatedValue();

                    //imageView2.getLayoutParams().height = value;
                    imageView2.requestLayout();
                    imageView2.setVisibility(isExpanded ? View.VISIBLE : View.GONE);
                    textView3.requestLayout();
                    textView3.setVisibility(isExpanded ? View.VISIBLE : View.GONE);
                    likebutton.requestLayout();
                    likebutton.setVisibility(isExpanded ? View.VISIBLE : View.GONE);
                }
            });

            va.start();
        }
    }

    public void edit_score(String user_id, int score) {
        // 수정하면 유저 id 받으면 통신하는게 완성 됨
        int temp_score = 0;
        com.android.volley.Response.Listener<JSONObject> pjresponseListener = new com.android.volley.Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if(user.getUser_id() == writen_id) {
                        user.setScore(Integer.parseInt(response.get("score").toString()));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        //String URL = "http://52.79.125.108/api/detail/" + user_id;
        String URL = "http://52.79.125.108/api/user/" +  user_id;
        url_json read = new url_json();
        JSONObject jtemp_score = null;
        try {
            jtemp_score = read.readJsonFromUrl(URL);
            JSONObject temp = new JSONObject(jtemp_score.get("temp").toString());
            temp_score = Integer.parseInt(temp.get("score").toString());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JSONObject pointj = new JSONObject();
        try {
            pointj.put("score", temp_score +score);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        addpointRequest preq = new addpointRequest(Request.Method.POST, pointj, URL, pjresponseListener, null);
        RequestQueue pjqueue = Volley.newRequestQueue(context);
        pjqueue.add(preq);
    }

    boolean slike_contains(String temp) {
        for(int j =0; j<slike_list_save.size(); j++) {
            if(slike_list_save.get(j).equals(temp)) {
                return true;
            }
        }
        return false;
    }
    void add_feed_id() {
        url = "http://52.79.125.108/api/likefeed/user/"+ user.getUsername();
        try {
            cat_json = read.readJsonFromUrl(url);
            cat_arr = new JSONArray(cat_json.get("temp").toString());
            for(int j =0; j<cat_arr.length(); j++) {
                my_review_json = (JSONObject) cat_arr.get(j);
                slike_list_save.add(my_review_json.get("feed").toString());
            }

        }
        catch (Exception e) {

        }
    }

}