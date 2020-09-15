package com.happicouch.livestory.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.happicouch.livestory.models.User;
import com.happicouch.livestory.repositories.SignUpRepository;

public class SignUpViewModel extends ViewModel {
    private SignUpRepository signUpRepository;
    private MutableLiveData<User> firebaseUser;
    private MutableLiveData<Boolean> usernameCheck;

    public SignUpViewModel() {
        signUpRepository = new SignUpRepository();
    }

    public MutableLiveData<User> signUpWithEmail(String email, String pass){
        firebaseUser = signUpRepository.signUpWithFirebase(email, pass);
        return firebaseUser;
    }

    public boolean validate(String username, String email, String pass, String repass){
        return signUpRepository.validateValues(username, email, pass, repass);
    }

    public MutableLiveData<Boolean> checkUsernameFromFirebase(String username){
        usernameCheck = signUpRepository.checkUsername(username);
        return usernameCheck;
    }
}
