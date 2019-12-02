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
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.tt.data.User;
import com.example.tt.model.Data;
import com.like.IconType;
import com.like.LikeButton;
import com.like.OnLikeListener;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

public class MoimAdapter extends RecyclerView.Adapter<MoimAdapter.ItemViewHolder> {

    private ArrayList<Data> listData = new ArrayList<>();
    private Context context;
    private SparseBooleanArray selectedItems = new SparseBooleanArray();
    private int prePosition = -1;
    User user = User.getInstance();
    String writen_id;

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        context = parent.getContext();
       // View view = LayoutInflater.from(parent.getContext()).inflate(R.font.moim_item, parent, false);

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.moim_item, parent, false);
        GridLayoutManager.LayoutParams lp = (GridLayoutManager.LayoutParams) v.getLayoutParams();
        lp.height = parent.getMeasuredHeight() / 2;
        v.setLayoutParams(lp);

        return new ItemViewHolder(v);
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
        private LikeButton likebutton;

        private Data data;
        private int position;

        ItemViewHolder(View itemView) {
            super(itemView);

            textView1 = itemView.findViewById(R.id.textView1);
            textView2 = itemView.findViewById(R.id.textView2);
            textView3 = itemView.findViewById(R.id.content);
            likebutton = itemView.findViewById(R.id.heart);
            imageView1 = itemView.findViewById(R.id.imageView2);
        }

        void onBind(Data data, int position) {
            this.data = data;
            this.position = position;

            textView1.setText(data.getTitle());
            textView2.setText(data.getAuthor());
            textView3.setText(data.getDate());
            writen_id = data.getAuthor();
            //textView3.setText(data.getContent());
            if(data.getUrlImage()!= "null"){
                Picasso.get().load(data.getUrlImage()).into(imageView1);
            }
            likebutton.setIcon(IconType.Heart);
            likebutton.setScaleX(1);
            likebutton.setScaleY(1);
            likebutton.setOnLikeListener(new OnLikeListener() {
                @Override
                public void liked(LikeButton likeButton) {
                    //생김+1
                    edit_score(writen_id, 1);
                }

                @Override
                public void unLiked(LikeButton likeButton) {
                    //사라짐 -1
                    edit_score(writen_id,-1);
                }
            });


            itemView.setOnClickListener(this);
            textView1.setOnClickListener(this);
            textView2.setOnClickListener(this);
            imageView1.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {

            Intent intent = new Intent(context,moim_detail.class);
            intent.putExtra("image",data.getUrlImage());
            //Toast.makeText(context, data.getUrlImage() + " 이미지 입니다.", Toast.LENGTH_SHORT).show();
            intent.putExtra("author",data.getAuthor());
            //Toast.makeText(context, data.getWriter() + " 작성자 입니다.", Toast.LENGTH_SHORT).show();
            intent.putExtra("title",data.getTitle());
            //Toast.makeText(context, data.getTitle() + " 제목 입니다.", Toast.LENGTH_SHORT).show();
            intent.putExtra("content",data.getContent());
           // Toast.makeText(context, data.getContent() + " 내용 입니다.", Toast.LENGTH_SHORT).show();
            intent.putExtra("id", data.getId());
            context.startActivity(intent);

            switch (v.getId()) {

                case R.id.textView1:

                    break;
                case R.id.textView2:

                    break;
                case R.id.imageView1:

                    break;
            }
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
            pointj.put("score", (int)(temp_score +score));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        addpointRequest preq = new addpointRequest(Request.Method.POST, pointj, URL, pjresponseListener, null);
        RequestQueue pjqueue = Volley.newRequestQueue(context);
        pjqueue.add(preq);
    }
}