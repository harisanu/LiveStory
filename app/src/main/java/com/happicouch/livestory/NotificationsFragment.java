package com.happicouch.livestory;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.happicouch.livestory.models.User;
import com.happicouch.livestory.viewmodels.NotificationsViewModel;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

//PROFILE FRAGMENT
public class NotificationsFragment extends Fragment {

    //Const
    private static final int GALLERY_REQUEST = 1;
    private static final String TAG = "ProfileFragment";

    //Vars
    private NotificationsViewModel notificationsViewModel;

    //Widgets
    private RelativeLayout profileRelativeLayout;
    private TextView profileFollowerCount, profileFollowingCount, profileFullname, profileBio, profileUsername, progressText;
    private ImageView profileImage;
    private Button profileButton;
    private FloatingActionButton profileFloatingButton;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        notificationsViewModel = new ViewModelProvider(requireActivity()).get(NotificationsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_notifications, container, false);

        profileRelativeLayout = root.findViewById(R.id.profile_relative_layout);
        profileFollowerCount = root.findViewById(R.id.profile_followers_count);
        profileFollowingCount = root.findViewById(R.id.profile_following_count);
        profileFullname = root.findViewById(R.id.profile_fullname);
        profileUsername = root.findViewById(R.id.profile_username);
        profileBio = root.findViewById(R.id.profile_bio);
        profileImage = root.findViewById(R.id.edit_profile_pic);
        profileButton = root.findViewById(R.id.profile_button);
        profileFloatingButton = root.findViewById(R.id.profile_floating_button);
        progressText = root.findViewById(R.id.edit_profile_image_text);
        progressBar = root.findViewById(R.id.edit_profile_progress);
        recyclerView = root.findViewById(R.id.profile_recycleview);

        progressBar.setVisibility(View.GONE);
        progressText.setVisibility(View.GONE);

        profileFloatingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, GALLERY_REQUEST);
            }
        });

        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), EditProfileActivity.class);
                startActivity(intent);
            }
        });

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        notificationsViewModel.getUserFromFirebase().observe(requireActivity(), new Observer<User>() {
            @Override
            public void onChanged(User mUser) {
                if(mUser != null){
                    Log.d(TAG, "onChanged: Just fired!");
                    profileUsername.setText(mUser.getUsername());
                    profileFollowingCount.setText(String.valueOf(mUser.getFollowing()));
                    profileFollowerCount.setText(String.valueOf(mUser.getFollowers()));
                    profileBio.setText(mUser.getBio().replaceAll("<br />", "\n"));

                    if(mUser.getFullName() != null){
                        profileFullname.setText(mUser.getFullName() + " | ");
                    }

                    if(mUser.getImageUri() != null){
                        Glide
                                .with(requireActivity())
                                .load(mUser.getImageUri())
                                .placeholder(R.drawable.ic_logo)
                                .into(profileImage);
                    }
                }
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Activity.RESULT_OK)
            switch (requestCode){
                case GALLERY_REQUEST:
                    Uri selectedImage = data.getData();
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedImage);
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 25, baos);
                        byte[] dataByte = baos.toByteArray();
                        updateImage(dataByte);
                    } catch (IOException e) {
                        Log.i(TAG, "Some exception " + e);
                    }
                    break;
            }
    }

    protected void updateImage(byte[] selectedImage){
        progressBar.setVisibility(View.VISIBLE);
        progressText.setVisibility(View.VISIBLE);

        notificationsViewModel.setImage(selectedImage).observe(requireActivity(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if(aBoolean){
                    getImagePath();
                    Log.d(TAG, "onChanged: " + aBoolean);
                }else{
                    Log.d(TAG, "onChanged: " + aBoolean);
                }
            }
        });

        Bitmap bitmap = BitmapFactory.decodeByteArray(selectedImage, 0, selectedImage.length);
        profileImage.setImageBitmap(bitmap);
    }

    protected void getImagePath(){
        notificationsViewModel.imagePath().observe(requireActivity(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                if(s != null){
                    Log.d(TAG, "onChanged: image path retrieved." + s);
                    updateDatabase(s);
                }
            }
        });
    }

    protected void updateDatabase(final String imagePath){
        notificationsViewModel.updateDatabase(imagePath).observe(requireActivity(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if(aBoolean){
                    progressBar.setVisibility(View.GONE);
                    progressText.setVisibility(View.GONE);
                    Glide
                            .with(requireActivity())
                            .load(imagePath)
                            .placeholder(R.drawable.ic_logo)
                            .into(profileImage);
                    Toast.makeText(getActivity(), "Image successfully updated!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}