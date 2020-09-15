package com.happicouch.livestory;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseUser;
import com.happicouch.livestory.models.User;
import com.happicouch.livestory.viewmodels.LauncherViewModel;

public class LauncherActivity extends AppCompatActivity {

    //Vars
    private LauncherViewModel launcherViewModel;

    //Widgets
    private TextView signUp, forgotPass;
    private Button logInButton;
    private EditText email, password;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);

        signUp = findViewById(R.id.launcher_signup);
        forgotPass = findViewById(R.id.launcher_forgot);
        email = findViewById(R.id.launcher_email);
        password = findViewById(R.id.launcher_password);
        logInButton = findViewById(R.id.launcher_button);
        progressBar = findViewById(R.id.edit_profile_progress);

        launcherViewModel = new ViewModelProvider(this).get(LauncherViewModel.class);

        progressBar.setVisibility(View.GONE);
        logInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(launcherViewModel.validate(email.getText().toString(), password.getText().toString())){
                    progressBar.setVisibility(View.VISIBLE);
                    logInButton.setEnabled(false);
                    launcherViewModel.signInWithEmail(email.getText().toString(), password.getText().toString()).observe(LauncherActivity.this, new Observer<FirebaseUser>() {
                        @Override
                        public void onChanged(FirebaseUser user) {
                            if(user != null){
                                startNewActivity();
                            }else{
                                progressBar.setVisibility(View.GONE);
                                logInButton.setEnabled(true);
                            }
                        }
                    });
                }
            }
        });
    }

    protected void startNewActivity(){
        launcherViewModel.getUserDatabase().observe(LauncherActivity.this, new Observer<User>() {
            @Override
            public void onChanged(User user) {
                if(user != null){
                    progressBar.setVisibility(View.GONE);
                    Intent intent = new Intent(LauncherActivity.this, MainActivity.class);
                    intent.putExtra("user", user);
                    startActivity(intent);
                    finish();
                }else{
                    logInButton.setEnabled(true);
                }
            }
        });
    }

    public void signUpPressed(View view) {
        Intent intent = new Intent(LauncherActivity.this, SignUpActivity.class);
        startActivity(intent);
    }

    public void forgotPassPressed(View view) {
        //forgotPassPressed
    }
}