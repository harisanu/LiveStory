package com.happicouch.livestory.repositories;

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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.happicouch.livestory.models.User;

import java.util.Map;

public class EditProfileRepository {
    private static final String TAG = "EditProfileRepository";

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser mUser = mAuth.getCurrentUser();
    private DocumentReference documentReference = db.collection("users").document(mUser.getUid());

    private Map<String, Object> usernames;

    public LiveData<String> fullnameChangeWithFirebase(final String fullname){
        final MutableLiveData<String> mFullname = new MutableLiveData<>();

        documentReference.update("fullName", fullname).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    mFullname.setValue(fullname);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                mFullname.setValue(null);
            }
        });

        return mFullname;
    }

    public LiveData<String> bioChangeWithFirebase(final String bio){
        final MutableLiveData<String> mBio = new MutableLiveData<>();

        documentReference.update("bio", bio).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    mBio.setValue(bio);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                mBio.setValue(null);
            }
        });

        return mBio;
    }

    public LiveData<Boolean> checkUsernameAvailability(final String newUsername){
        final MutableLiveData<Boolean> available = new MutableLiveData<>();

        db.collection("usernames").document("unavailable").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if(documentSnapshot.exists()){
                        usernames = documentSnapshot.getData();
                        if(usernames.containsKey(newUsername)){
                            available.setValue(true);
                        }else{
                            available.setValue(false);
                        }
                    }
                }
            }
        });

        return available;
    }

    public LiveData<Boolean> usernameUpdateFirebase(final String newUsername, String oldUsername){
        final MutableLiveData<Boolean> finished = new MutableLiveData<>();
        usernames.put(oldUsername, FieldValue.delete());
        usernames.put(newUsername, true);

        db.collection("usernames").document("unavailable").update(usernames).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                finished.setValue(true);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                finished.setValue(false);
            }
        });

        return finished;
    }

    public LiveData<Boolean> privacyChangeWithFirebase(final boolean privacy){
        final MutableLiveData<Boolean> mPrivacy = new MutableLiveData<>();

        documentReference.update("privacy", privacy).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    mPrivacy.setValue(privacy);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                mPrivacy.setValue(null);
            }
        });

        return mPrivacy;
    }

    public LiveData<User> getUpdatedUser(){
        final MutableLiveData<User> user = new MutableLiveData<>();

        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
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

    public LiveData<String> updateUsernameLast(final String username){
        final MutableLiveData<String> mUsername = new MutableLiveData<>();

        documentReference.update("username", username).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                mUsername.setValue(username);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                mUsername.setValue(null);
            }
        });

        return mUsername;
    }
}
