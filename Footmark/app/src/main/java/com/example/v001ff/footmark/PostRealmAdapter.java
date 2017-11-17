package com.example.v001ff.footmark;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import io.realm.OrderedRealmCollection;
import io.realm.RealmRecyclerViewAdapter;

/**
 * Created by enPiT-P7 on 2017/11/16.
 */

public class PostRealmAdapter extends RealmRecyclerViewAdapter<Post, PostRealmAdapter.PostViewHolder> {
    Context context;

    public static class PostViewHolder extends RecyclerView.ViewHolder{
        protected TextView userName;
        protected TextView spotInfo;
        protected TextView date;
        protected ImageView userPhoto;

        public PostViewHolder(View itemView){
            super(itemView);
            userName = (TextView) itemView.findViewById(R.id.username);
            spotInfo = (TextView) itemView.findViewById(R.id.spotinfo);
            date = (TextView) itemView.findViewById(R.id.date);
            userPhoto = (ImageView) itemView.findViewById(R.id.userphoto);
        }
    }

    public PostRealmAdapter(@NonNull Context context, @Nullable OrderedRealmCollection<Post> data,
                            boolean autoUpdate) {
        super(data, autoUpdate);
        this.context = context;
    }

    @Override
    public PostViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_layout, parent, false);
        final PostViewHolder holder = new PostViewHolder(itemView);

        /*holder.itemView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                int position = holder.getAdapterPosition();
                Post post = getData().get(position);
                long postId = post.id;

                Intent intent = new Intent(context, ShowSpotActivity.class);
                intent.putExtra(ShowSpotActivity.DIARY_ID, diaryId);
                context.startActivity(intent);
            }
        });*/
        return holder;
    }

    @Override
    public void onBindViewHolder(PostViewHolder holder, int position){
        Post post = getData().get(position);
        holder.userName.setText(post.userName);
        holder.spotInfo.setText(post.spotInfo);
        holder.date.setText(post.date);
        if(post.userPhoto != null && post.userPhoto.length != 0){
            Bitmap bmp = MyUtils.getImageFromByte(post.userPhoto);
            holder.userPhoto.setImageBitmap(bmp);
        }
    }
}