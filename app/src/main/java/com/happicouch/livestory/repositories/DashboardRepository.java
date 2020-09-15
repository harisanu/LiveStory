package com.happicouch.livestory.repositories;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DashboardRepository {
    private StorageReference mStorageRef;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;

    //TODO: Get the downloadLink and upload to User.
    public LiveData<Boolean> saveToStorage(byte[] bytes){
        final MutableLiveData<Boolean> storageSaveStatus = new MutableLiveData<>();
        String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());

        mStorageRef = FirebaseStorage.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        StorageReference savePath = mStorageRef.child(currentUser.getUid()).child("IMG_" + timeStamp);
        savePath.putBytes(bytes).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                storageSaveStatus.setValue(true);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                storageSaveStatus.setValue(false);
            }
        });

        return storageSaveStatus;
    }
}
