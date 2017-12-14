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

public class PostRealmAdapter extends RealmRecyclerViewAdapter<FootmarkDataTable, PostRealmAdapter.PostViewHolder> {
    Context context;

    public static class PostViewHolder extends RecyclerView.ViewHolder{
        protected TextView AccountName;
        protected TextView ReviewBody;
        protected TextView ReviewDate;
        protected ImageView AccountImage;

        public PostViewHolder(View itemView){
            super(itemView);
            AccountName = (TextView) itemView.findViewById(R.id.username);
            ReviewBody = (TextView) itemView.findViewById(R.id.spotinfo);
            ReviewDate = (TextView) itemView.findViewById(R.id.date);
            AccountImage = (ImageView) itemView.findViewById(R.id.userphoto);
        }
    }

    public PostRealmAdapter(@NonNull Context context, @Nullable OrderedRealmCollection<FootmarkDataTable>data,
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
        //FootmarkDataTable post = getData().get(position);
        FootmarkDataTable post = getItem(getItemCount()-1-position);
        holder.AccountName.setText(post.AccountName);
        holder.ReviewBody.setText(post.ReviewBody);
        holder.ReviewDate.setText((CharSequence) post.ReviewDate);
        if(post.AccountImage != null && post.AccountImage.length != 0){
            Bitmap bmp = MyUtils.getImageFromByte(post.AccountImage);
            holder.AccountImage.setImageBitmap(bmp);
        }
    }

}