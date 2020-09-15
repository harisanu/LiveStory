package com.happicouch.livestory.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.happicouch.livestory.R;
import com.happicouch.livestory.models.StoryListView;

import java.util.List;

public class StoryListViewAdapter extends ArrayAdapter<StoryListView> {

    private int layoutResource;

    public StoryListViewAdapter(@NonNull Context context, int resource, List<StoryListView> storyListViews) {
        super(context, resource, storyListViews);
        this.layoutResource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;

        if(view == null){
            LayoutInflater layoutInflater = LayoutInflater.from(getContext());
            view = layoutInflater.inflate(layoutResource, null);
        }

        StoryListView storyListView = getItem(position);

        if(storyListView != null){
            ImageView storyThumbnail = view.findViewById(R.id.storyThumbnail);
            TextView storyTitle = view.findViewById(R.id.storyTitle);

            if(storyThumbnail != null){
                //Add byte[] to imageview.
            }

            if(storyTitle != null){
                storyTitle.setText(storyListView.getStoryTitle());
            }
        }

        return view;
    }
}
