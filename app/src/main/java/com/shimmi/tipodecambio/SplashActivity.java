package com.shimmi.tipodecambio;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nonnull;

public class SplashActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();
        updateUI(currentUser);

//        Map<String, Object> banco = new HashMap<>();
//        banco.put("Nombre", "HSBC");
//        db.collection("banks").document("HSBC").set(banco).addOnSuccessListener(new OnSuccessListener<Void>() {
//            @Override
//            public void onSuccess(Void documentReference) {
//                Log.d("FBSUCCESS", "DocumentSnapshot added with ID: " + documentReference);
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@Nonnull Exception e) {
//                Log.w("FAIL", "Error adding document", e);
//            }
//        });


    }
    private void updateUI(FirebaseUser currentUser) {
        if (currentUser!=null){
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                public void run() {
                    openActivity();
                    finish();
                }
            }, 2000);   //5 seconds

        }else{
            auth.signInAnonymously();
        }
    }

    private void openActivity(){
        Intent main = new Intent(this,MainActivity.class);
        startActivity(main);
    }
}
