package com.happicouch.livestory.repositories;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.happicouch.livestory.models.User;

import java.util.HashMap;
import java.util.Map;

public class SignUpRepository{
    private static final String TAG = "SignUpRepository";
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String userUsername;
    private FirebaseUser user;

    public MutableLiveData<User> signUpWithFirebase(String email, String password){
        final MutableLiveData<User> mUser = new MutableLiveData<>();

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            user = mAuth.getCurrentUser();
                            if(user != null){
                                User userObject = new User(userUsername, user.getEmail());
                                mUser.setValue(userObject);
                                saveToDatabase(userObject);
                            }
                        } else {
                            // If sign in fails, display a message to the user.
                        }
                        // ...
                    }
                });

        return mUser;
    }

    //TODO: Do something if this were to fail.
    public void saveToDatabase(User userFirebase){
        db.collection("users").document(user.getUid()).set(userFirebase).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){

                }else{
                    //Failed
                }
            }
        });

        Map<String, Object> usernameDatabase = new HashMap<>();
        usernameDatabase.put(userUsername, true);

        db.collection("usernames").document("unavailable").update(usernameDatabase).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    //Successfull
                }else{
                    //Failed.
                }
            }
        });
    }

    public boolean validateValues(String username, String email, String pass, String repass){
        if(username.equals("") || email.equals("")){
            return false;
        }else{
            if(!pass.equals(repass)){
                return false;
            }
        }
        userUsername = username.toLowerCase();
        return true;
    }

    public MutableLiveData<Boolean> checkUsername(final String username){
        final String usernameLowerCase = username.toLowerCase();
        final MutableLiveData<Boolean> exists = new MutableLiveData<>();

        db.collection("usernames").document("unavailable").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if(documentSnapshot.exists()){
                        Map<String, Object> userDatabase = documentSnapshot.getData();
                        if(userDatabase.containsKey(usernameLowerCase)){
                            exists.setValue(true);
                        }else{
                            exists.setValue(false);
                        }
                    }else{
                        exists.setValue(false);
                    }
                }else{
                    exists.setValue(true);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "onFailure: " + e.getMessage());
            }
        });
        return exists;
    }
}
