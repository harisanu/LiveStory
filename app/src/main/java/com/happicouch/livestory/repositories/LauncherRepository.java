package com.happicouch.livestory.repositories;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.happicouch.livestory.models.User;

public class LauncherRepository {

    //Vars
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseUser user;

    public MutableLiveData<FirebaseUser> signInWithFirebase(String email, String password){
        if(email.equals("") || password.equals("")){
            return null;
        }

        final MutableLiveData<FirebaseUser> firebaseUser = new MutableLiveData<>();

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            user = mAuth.getCurrentUser();
                            firebaseUser.setValue(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            firebaseUser.setValue(null);
                        }

                        // ...
                    }
                });

        return firebaseUser;
    }

    public MutableLiveData<User> getUserFromDatabase(){
        final MutableLiveData<User> mUser = new MutableLiveData<>();

        db.collection("users").document(user.getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if(documentSnapshot.exists()){
                        mUser.setValue(documentSnapshot.toObject(User.class));
                    }
                }
            }
        });

        return mUser;
    }

    public boolean validateStrings(String email, String password){
        return !email.equals("") || !password.equals("");
    }
}
