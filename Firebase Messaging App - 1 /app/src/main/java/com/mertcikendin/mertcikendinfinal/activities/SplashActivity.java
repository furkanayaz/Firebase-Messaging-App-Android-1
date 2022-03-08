package com.mertcikendin.mertcikendinfinal.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.mertcikendin.mertcikendinfinal.R;

import maes.tech.intentanim.CustomIntent;

@SuppressLint("CustomSplashScreen")
public class SplashActivity extends AppCompatActivity {
    private FirebaseAuth firebaseAuth = null;
    private FirebaseUser firebaseUser = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        initializeFirebase();
        delayer();

    }

    private  void initializeFirebase() {
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getInstance().getCurrentUser();
    }

    private void delayer() {
        CountDownTimer countDownTimer = new CountDownTimer(3000, 1000) {
            @Override
            public void onTick(long l) {

            }

            @Override
            public void onFinish() {

                if (firebaseUser != null) {
                    toMainPage();
                }else {
                    toSigninPage();
                }

            }
        }.start();
    }

    private void toMainPage() {
        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
        CustomIntent.customType(SplashActivity.this, "fadein-to-fadeout");
    }

    private void toSigninPage() {
        Intent intent = new Intent(SplashActivity.this, SigninActivity.class);
        startActivity(intent);
        finish();
        CustomIntent.customType(SplashActivity.this, "fadein-to-fadeout");
    }

}