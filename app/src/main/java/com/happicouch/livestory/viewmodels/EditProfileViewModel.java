package com.happicouch.livestory.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.happicouch.livestory.models.User;
import com.happicouch.livestory.repositories.EditProfileRepository;

public class EditProfileViewModel extends ViewModel {
    private EditProfileRepository editProfileRepository;

    public EditProfileViewModel() {
        editProfileRepository = new EditProfileRepository();
    }

    public LiveData<String> fullnameChangeFirebase(String fullname){
        return editProfileRepository.fullnameChangeWithFirebase(fullname);
    }

    public LiveData<String> bioChangeFirebase(String bio){
        return editProfileRepository.bioChangeWithFirebase(bio);
    }

    public LiveData<Boolean> usernameUpdateDatabase(String newUsername, String oldUsername){
        return editProfileRepository.usernameUpdateFirebase(newUsername, oldUsername);
    }

    public LiveData<Boolean> checkUsername(String username){
        return editProfileRepository.checkUsernameAvailability(username);
    }

    public LiveData<Boolean> privacyChangeFirebase(boolean privacy){
        return editProfileRepository.privacyChangeWithFirebase(privacy);
    }

    public LiveData<User> observeUser(){
        return editProfileRepository.getUpdatedUser();
    }

    public LiveData<String> usernameUpdated(String username){
        return editProfileRepository.updateUsernameLast(username);
    }
}
