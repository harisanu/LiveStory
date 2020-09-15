package com.happicouch.livestory.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.happicouch.livestory.models.User;
import com.happicouch.livestory.repositories.NotificationRepository;

public class NotificationsViewModel extends ViewModel {
    //Vars
    private NotificationRepository notificationRepository;

    public NotificationsViewModel() {
        notificationRepository = new NotificationRepository();
    }

    public LiveData<Boolean> setImage(byte[] imageUri){
        return notificationRepository.uploadImageToDatabase(imageUri);
    }

    public LiveData<String> imagePath(){
        return notificationRepository.getFilePath();
    }

    public LiveData<Boolean> updateDatabase(String imagePath){
        return notificationRepository.updateDatabase(imagePath);
    }

    public LiveData<User> getUserFromFirebase(){
        return notificationRepository.getUpdatedUser();
    }
}