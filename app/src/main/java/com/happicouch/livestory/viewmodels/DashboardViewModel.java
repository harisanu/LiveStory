package com.happicouch.livestory.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.happicouch.livestory.repositories.DashboardRepository;

public class DashboardViewModel extends ViewModel {
    private DashboardRepository dashboardRepository;

    public DashboardViewModel() {
        dashboardRepository = new DashboardRepository();
    }

    public LiveData<Boolean> saveToStorage(byte[] bytes){
        return dashboardRepository.saveToStorage(bytes);
    }
}