package com.happicouch.livestory.repositories;

import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.happicouch.livestory.models.User;

public class NotificationRepository {
    //Const
    private static final String TAG = "NotificationRepository";
    
    //Vars
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser mUser = mAuth.getCurrentUser();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();
    private StorageReference ref;

    public LiveData<Boolean> uploadImageToDatabase(byte[] imageUri){
        ref = mStorageRef.child("profilepictures").child(mUser.getUid());
        final MutableLiveData<Boolean> imageBoolean = new MutableLiveData<>();

        ref.putBytes(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Log.d(TAG, "onSuccess: upload success");
                imageBoolean.setValue(true);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "onFailure: upload failed");
                imageBoolean.setValue(false);
            }
        });

        return imageBoolean;
    }

    public LiveData<String> getFilePath(){
        final MutableLiveData<String> filePath = new MutableLiveData<>();
        ref.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if(task.isSuccessful()){
                    filePath.setValue(task.getResult().toString());
                }
            }
        });

        return filePath;
    }

    public LiveData<Boolean> updateDatabase(String filePath){
        final MutableLiveData<Boolean> databaseBoolean = new MutableLiveData<>();

        db.collection("users").document(mUser.getUid()).update("imageUri", filePath).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                databaseBoolean.setValue(true);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                databaseBoolean.setValue(false);
            }
        });

        return databaseBoolean;
    }

    public LiveData<User> getUpdatedUser(){
        final MutableLiveData<User> user = new MutableLiveData<>();

        db.collection("users").document(mUser.getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                user.setValue(documentSnapshot.toObject(User.class));
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                user.setValue(null);
            }
        });

        return user;
    }
}
