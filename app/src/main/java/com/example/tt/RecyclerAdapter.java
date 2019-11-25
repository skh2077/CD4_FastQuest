package com.example.tt;


import android.animation.ValueAnimator;
import android.content.Context;

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

import com.example.tt.model.Data;
import com.like.IconType;
import com.like.LikeButton;
import com.like.OnLikeListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ItemViewHolder> {

    private ArrayList<Data> listData = new ArrayList<>();
    private Context context;
    private SparseBooleanArray selectedItems = new SparseBooleanArray();
    private int prePosition = -1;

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        context = parent.getContext();
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
                }

                @Override
                public void unLiked(LikeButton likeButton) {
                        //사라짐 -1
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
}