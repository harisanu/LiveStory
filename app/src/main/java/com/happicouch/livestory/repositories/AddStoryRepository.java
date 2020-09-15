package com.happicouch.livestory.repositories;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;

import static android.content.ContentValues.TAG;

public class AddStoryRepository {
    private FirebaseFirestore mFirebaseFirestore;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private Calendar date;

    public LiveData<Boolean> ifListExists(){
        final MutableLiveData<Boolean> exists = new MutableLiveData<>();

        mFirebaseFirestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        date = Calendar.getInstance();

        int day;
        int month;
        int year;

        day = date.get(Calendar.DAY_OF_MONTH);
        month = date.get(Calendar.MONTH) + 1;
        year = date.get(Calendar.YEAR);

        StringBuilder dateString = new StringBuilder();
        dateString.append(day).append(".").append(month).append(".").append(year);
        Log.d(TAG, "ifListExists: " + dateString.toString());

        DocumentReference docRef = mFirebaseFirestore.collection(currentUser.getUid()).document(dateString.toString());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if(documentSnapshot.exists()){
                        exists.setValue(true);
                    }else{
                        exists.setValue(false);
                    }
                }

            }
        });
        return exists;
    }
}
