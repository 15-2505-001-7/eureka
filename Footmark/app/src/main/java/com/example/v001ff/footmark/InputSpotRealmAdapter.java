package com.example.v001ff.footmark;

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
import io.realm.internal.Context;

/**
 * Created by v001ff on 2017/12/07.
 */

public class InputSpotRealmAdapter extends RealmRecyclerViewAdapter <FootmarkDataTable,
    InputSpotRealmAdapter.InputSpotViewHolder>{

    Context context;

    public static class InputSpotViewHolder extends RecyclerView.ViewHolder{
        protected TextView title;
        protected TextView bodyText;
        protected ImageView photo;

        public InputSpotViewHolder(View itemView){
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.addPlaceName);
            bodyText = (TextView) itemView.findViewById(R.id.addReview);
            photo = (ImageView) itemView.findViewById(R.id.spot_photo);
        }
    }

    public InputSpotRealmAdapter(@NonNull Context context,
                                 @Nullable OrderedRealmCollection<FootmarkDataTable> data, boolean autoUpdate) {
        super(data, autoUpdate);
        this.context = context;
    }

    @Override
    public InputSpotViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.card_layout,parent,false);
        final InputSpotViewHolder holder = new InputSpotViewHolder(itemView);
        return holder;
    }

    @Override
    public void onBindViewHolder(InputSpotViewHolder holder, int position) {
        FootmarkDataTable footmarkDataTable = getData().get(position);
        holder.title.setText(footmarkDataTable.PlaceName);
        holder.bodyText.setText(footmarkDataTable.ReviewBody);
        if(footmarkDataTable.PlaceImage != null && footmarkDataTable.PlaceImage.length != 0){
            Bitmap bmp = MyUtils.getImageFromByte(footmarkDataTable.PlaceImage);
            holder.photo.setImageBitmap(bmp);
        }
    }

}