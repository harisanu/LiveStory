package com.happicouch.livestory;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.happicouch.livestory.adapters.StoryListViewAdapter;
import com.happicouch.livestory.models.StoryListView;
import com.happicouch.livestory.viewmodels.AddStoryViewModel;

import java.util.ArrayList;
import java.util.List;

public class AddStoryFragment extends Fragment {

    //Const
    private static final String TAG = "AddStoryFragment";

    //private
    private AddStoryViewModel addStoryViewModel;
    private byte[] bytes;

    //Widgets
    private ListView addStoryListView;
    private Button addStoryCreateButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_story, container, false);

        addStoryListView = view.findViewById(R.id.addStoryListView);
        addStoryCreateButton = view.findViewById(R.id.addStoryCreateButton);

        addStoryViewModel = new ViewModelProvider(this).get(AddStoryViewModel.class);
        getList();

        addStoryCreateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = getArguments();
                if(bundle != null) {
                    Intent intent = new Intent(getActivity(), CreateStoryActivity.class);
                    intent.putExtra("image", String.valueOf(bundle.get("image")));
                    startActivity(intent);
                }
            }
        });

        return view;
    }

    protected void getList(){
        addStoryViewModel.listExists().observe(getActivity(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if(aBoolean){
                    Log.d(TAG, "onChanged: " + aBoolean);
                    List<StoryListView> storyListViewList = new ArrayList<>();
                    //TODO: storyListViewList method to add newest stories in firebase (Viewmodel).
                    StoryListViewAdapter storyListViewAdapter = new StoryListViewAdapter(getContext(), R.layout.listview_story, storyListViewList);
                    addStoryListView.setAdapter(storyListViewAdapter);
                }else{
                    Log.d(TAG, "onChanged: " + aBoolean);
                }
            }
        });
    }
}
