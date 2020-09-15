package com.happicouch.livestory.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.happicouch.livestory.repositories.AddStoryRepository;

public class AddStoryViewModel extends ViewModel {
    private AddStoryRepository addStoryRepository;

    public AddStoryViewModel() {
        addStoryRepository = new AddStoryRepository();
    }

    public LiveData<Boolean> listExists(){
        return addStoryRepository.ifListExists();
    }
}
