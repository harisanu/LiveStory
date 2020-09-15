package com.happicouch.livestory.viewmodels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseUser;
import com.happicouch.livestory.models.User;
import com.happicouch.livestory.repositories.LauncherRepository;

public class LauncherViewModel extends ViewModel {

    private LauncherRepository launcherRepository;

    public LauncherViewModel() {
        launcherRepository = new LauncherRepository();
    }

    public MutableLiveData<FirebaseUser> signInWithEmail(String email, String password){
        return launcherRepository.signInWithFirebase(email, password);
    }

    public MutableLiveData<User> getUserDatabase(){
        return launcherRepository.getUserFromDatabase();
    }

    public boolean validate(String email, String password){
        return launcherRepository.validateStrings(email, password);
    }

}
