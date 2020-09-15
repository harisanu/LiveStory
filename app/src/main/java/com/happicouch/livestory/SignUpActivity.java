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
import android.widget.Toast;

import com.happicouch.livestory.models.User;
import com.happicouch.livestory.viewmodels.SignUpViewModel;

public class SignUpActivity extends AppCompatActivity {

    //Vars
    private SignUpViewModel signUpViewModel;

    //Widgets
    private EditText signUpUser, signUpEmail, signUpPass, signUpRePass;
    private Button signUpButton;
    private TextView signInText;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        signUpUser = findViewById(R.id.signup_username);
        signUpEmail = findViewById(R.id.signup_email);
        signUpPass = findViewById(R.id.signup_pass);
        signUpRePass = findViewById(R.id.signup_repass);
        signInText = findViewById(R.id.signup_signin);
        signUpButton = findViewById(R.id.signup_button);
        progressBar = findViewById(R.id.signup_progress);

        progressBar.setVisibility(View.GONE);
        signUpViewModel = new ViewModelProvider(this).get(SignUpViewModel.class);

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUpButton.setEnabled(false);
                progressBar.setVisibility(View.VISIBLE);
                if(signUpViewModel.validate(signUpUser.getText().toString(), signUpEmail.getText().toString(),
                        signUpPass.getText().toString(), signUpRePass.getText().toString())){
                    signUpViewModel.checkUsernameFromFirebase(signUpUser.getText().toString()).observe(SignUpActivity.this, new Observer<Boolean>() {
                        @Override
                        public void onChanged(Boolean aBoolean) {
                            if(aBoolean){
                                progressBar.setVisibility(View.GONE);
                                signUpButton.setEnabled(true);
                                Toast.makeText(SignUpActivity.this, "Pick another one.\nUsername already in use!", Toast.LENGTH_SHORT).show();
                            }else{
                                signInToMainActivity();
                            }
                        }
                    });
                }else{
                    progressBar.setVisibility(View.GONE);
                    signUpButton.setEnabled(true);
                }
            }
        });
    }

    protected void signInToMainActivity(){
        signUpViewModel.signUpWithEmail(signUpEmail.getText().toString(), signUpPass.getText().toString()).observe(SignUpActivity.this, new Observer<User>() {
            @Override
            public void onChanged(User user) {
                if(user != null){
                    Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                    intent.putExtra("user", user);
                    startActivity(intent);
                    finish();
                }else{
                    Toast.makeText(SignUpActivity.this, "Something went wrong! Check your internet connection.", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                    signUpButton.setEnabled(true);
                }
            }
        });
    }

    public void signInPressed(View view) {
        finish();
    }
}