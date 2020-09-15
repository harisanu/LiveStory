package com.happicouch.livestory;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.happicouch.livestory.models.User;
import com.happicouch.livestory.viewmodels.EditProfileViewModel;

public class EditProfileActivity extends AppCompatActivity {

    //Const
    private static final String TAG = "EditProfileActivity";

    //Vars
    private EditProfileViewModel editProfileViewModel;
    private User mUser;

    //Widgets
    private TextView toggleText, profileEmail;
    private EditText fullname, username, bio;
    private ToggleButton toggleButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        toggleText = findViewById(R.id.edit_profile_toggle_text);
        profileEmail = findViewById(R.id.edit_profile_info_email);
        fullname = findViewById(R.id.edit_profile_fullname_edit);
        username = findViewById(R.id.edit_profile_username_edit);
        bio = findViewById(R.id.edit_profile_bio_edit);
        toggleButton = findViewById(R.id.edit_profile_toggle);
        toggleText = findViewById(R.id.edit_profile_toggle_text);

        editProfileViewModel = new ViewModelProvider(this).get(EditProfileViewModel.class);
        updateViews();

        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    toggleText.setText("Only your friends can view your stories.");
                }else{
                    toggleText.setText("Everyone can view your stories.");
                }
            }
        });
    }

    protected void updateViews(){
        editProfileViewModel.observeUser().observe(this, new Observer<User>() {
            @Override
            public void onChanged(User user) {
                if(user != null){
                    mUser = user;
                    profileEmail.setText("Email: " + user.getEmail());
                    fullname.setHint(user.getFullName());
                    username.setHint(user.getUsername());
                    bio.setHint(user.getBio().replaceAll("<br />", "\n"));
                    if(user.isPrivacy()){
                        toggleText.setText("Only your friends can view your stories.");
                        toggleButton.setChecked(true);
                    }
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.customappbar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    // handle button activities
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
            case R.id.edit_profile_update:
                checkChanged();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    protected void checkChanged(){
        if(!fullname.getText().toString().trim().equals("")){
            fullnameChanged();
        }

        if(!bio.getText().toString().trim().equals("")){
            bioChanged();
        }

        if(mUser.isPrivacy() != toggleButton.isChecked()){
            privacyChanged();
        }

        if(!username.getText().toString().trim().equals("")){
            usernameChanged();
        }
    }

    protected void fullnameChanged(){
        editProfileViewModel.fullnameChangeFirebase(fullname.getText().toString()).observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                if(s == null){
                    fullname.setText(mUser.getFullName());
                }else{
                    fullname.getText().clear();
                    fullname.setHint(s);
                }
            }
        });
    }

    protected void bioChanged(){
        editProfileViewModel.bioChangeFirebase(bio.getText().toString().replaceAll("\\n", "<br />")).observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                if(s == null){
                    bio.setText(mUser.getBio().replaceAll("<br />", "\n"));
                }else{
                    bio.getText().clear();
                    bio.setHint(s.replaceAll("<br />", "\n"));
                }
            }
        });
    }

    protected void privacyChanged(){
        editProfileViewModel.privacyChangeFirebase(toggleButton.isChecked()).observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                toggleButton.setChecked(aBoolean);
                if(aBoolean){
                    toggleText.setText("Only your friends can view your stories.");
                }else{
                    toggleText.setText("Everyone can view your stories.");
                }
            }
        });
    }

    protected void usernameChanged(){
        editProfileViewModel.checkUsername(username.getText().toString()).observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if(!aBoolean){
                    usernameFirebaseUpdate();
                }else{
                    Toast.makeText(EditProfileActivity.this, "This username already exists! Pick another one.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    protected void usernameFirebaseUpdate(){
        editProfileViewModel.usernameUpdateDatabase(username.getText().toString(), mUser.getUsername()).observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if(aBoolean){
                    usernameUpdated();
                }
            }
        });
    }

    protected void usernameUpdated(){
        editProfileViewModel.usernameUpdated(username.getText().toString()).observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                if(s == null){
                    username.setText(mUser.getUsername());
                }else{
                    username.getText().clear();
                    username.setHint(s);
                }
            }
        });
    }
}