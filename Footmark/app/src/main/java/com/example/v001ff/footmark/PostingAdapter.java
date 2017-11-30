package com.example.v001ff.footmark;

import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import io.realm.OrderedRealmCollection;
import io.realm.RealmBaseAdapter;

/**
 * Created by v001ff on 2017/11/16.
 */

public class PostingAdapter extends RealmBaseAdapter<FootmarkDataTable> {

    private static class ViewHolder {
        TextView PlaceName;
        TextView ReviewBody;
    }

    public PostingAdapter(@Nullable OrderedRealmCollection<FootmarkDataTable> data) {
        super(data);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if(convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(
                    android.R.layout.simple_list_item_2, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.PlaceName =
                    convertView.findViewById(android.R.id.text1);
            viewHolder.ReviewBody =
                    convertView.findViewById(android.R.id.text2);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder)convertView.getTag();
        }

        FootmarkDataTable footmarkDataTable = adapterData.get(position);
        viewHolder.PlaceName.setText(footmarkDataTable.getPlaceName());
        viewHolder.ReviewBody.setText(footmarkDataTable.getReviewBody());
        return convertView;
    }
}
