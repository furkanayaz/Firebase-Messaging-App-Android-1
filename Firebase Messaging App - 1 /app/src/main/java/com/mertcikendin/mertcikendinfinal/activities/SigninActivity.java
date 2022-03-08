package com.mertcikendin.mertcikendinfinal.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.MotionEvent;
import android.view.View;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.mertcikendin.mertcikendinfinal.R;
import com.mertcikendin.mertcikendinfinal.databinding.ActivitySigninBinding;

import java.util.Objects;

import maes.tech.intentanim.CustomIntent;

public class SigninActivity extends AppCompatActivity {
    private ActivitySigninBinding activitySigninBinding = null;
    private FirebaseAuth firebaseAuth = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activitySigninBinding = DataBindingUtil.setContentView(SigninActivity.this, R.layout.activity_signin);
        activitySigninBinding.setSigninActivityObject(SigninActivity.this);

        initializeFirebase();
        setOnTouchListeners();
        setOnClickListeners();
    }

    private void initializeFirebase() {
        firebaseAuth = FirebaseAuth.getInstance();
    }

    private void setOnTouchListeners() {
        activitySigninBinding.cvSignin.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        activitySigninBinding.cvTvSignin.setTextColor(getResources().getColor(R.color.white));
                        activitySigninBinding.cvClSignin.setBackgroundColor(getResources().getColor(R.color.purple_700));
                        break;
                    case MotionEvent.ACTION_UP:
                        activitySigninBinding.cvTvSignin.setTextColor(getResources().getColor(R.color.purple_700));
                        activitySigninBinding.cvClSignin.setBackgroundResource(R.drawable.cardviewbackground);
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        activitySigninBinding.cvTvSignin.setTextColor(getResources().getColor(R.color.purple_700));
                        activitySigninBinding.cvClSignin.setBackgroundResource(R.drawable.cardviewbackground);
                        break;
                }

                return false;
            }
        });
    }

    private void setOnClickListeners() {
        activitySigninBinding.ivSigninCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        activitySigninBinding.tvSigninSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toSignupPage();
            }
        });
        activitySigninBinding.cvSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = activitySigninBinding.teSigninEmail.getText().toString().trim();
                String password = activitySigninBinding.teSigninPassword.getText().toString();

                if (Patterns.EMAIL_ADDRESS.matcher(email).matches() && password.length() >= 6) {
                    userController(view, email, password);
                }else {
                    Snackbar.make(view, "Giriş bilgilerinizi kontrol ediniz", Snackbar.LENGTH_LONG).show();
                }
            }
        });
    }

    private void userController(View view, String email, String password) {
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                toMainPage();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Snackbar.make(view, Objects.requireNonNull(e.getMessage()), Snackbar.LENGTH_LONG).show();
            }
        });
    }

    private void disableAnimation() {
        activitySigninBinding.cvTvSignin.setTextColor(getResources().getColor(R.color.purple_700));
        activitySigninBinding.cvClSignin.setBackgroundResource(R.drawable.cardviewbackground);
    }

    private void toSignupPage() {
        Intent intent = new Intent(SigninActivity.this, SignupActivity.class);
        startActivity(intent);
        finish();
        CustomIntent.customType(SigninActivity.this, "fadein-to-fadeout");
    }

    private void toMainPage() {
        disableAnimation();
        Intent intent = new Intent(SigninActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
        CustomIntent.customType(SigninActivity.this, "fadein-to-fadeout");
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}