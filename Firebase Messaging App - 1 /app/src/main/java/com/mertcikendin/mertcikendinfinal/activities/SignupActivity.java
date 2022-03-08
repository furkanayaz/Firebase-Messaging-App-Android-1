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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mertcikendin.mertcikendinfinal.R;
import com.mertcikendin.mertcikendinfinal.databinding.ActivitySignupBinding;

import java.util.HashMap;
import java.util.Objects;

import maes.tech.intentanim.CustomIntent;

public class SignupActivity extends AppCompatActivity {
    private ActivitySignupBinding activitySignupBinding = null;
    private FirebaseAuth firebaseAuth = null;
    private FirebaseDatabase firebaseDatabase = null;
    private DatabaseReference databaseReference = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activitySignupBinding = DataBindingUtil.setContentView(SignupActivity.this, R.layout.activity_signup);

        initializeFirebase();
        setOnTouchListeners();
        setOnClickListeners();
    }

    private void initializeFirebase() {
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("USERS");
    }

    private void setOnTouchListeners() {
        activitySignupBinding.cvSignup.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        activitySignupBinding.cvTvSignup.setTextColor(getResources().getColor(R.color.white));
                        activitySignupBinding.cvClSignup.setBackgroundColor(getResources().getColor(R.color.purple_700));
                        break;
                    case MotionEvent.ACTION_UP:
                        activitySignupBinding.cvTvSignup.setTextColor(getResources().getColor(R.color.purple_700));
                        activitySignupBinding.cvClSignup.setBackgroundResource(R.drawable.cardviewbackground);
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        activitySignupBinding.cvTvSignup.setTextColor(getResources().getColor(R.color.purple_700));
                        activitySignupBinding.cvClSignup.setBackgroundResource(R.drawable.cardviewbackground);
                        break;
                }

                return false;
            }
        });
    }

    private void setOnClickListeners() {
        activitySignupBinding.ivSignupCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        activitySignupBinding.tvSigninSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toSigninPage();
            }
        });
        activitySignupBinding.cvSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String namelastname = activitySignupBinding.teSignupNameLastname.getText().toString().trim();
                String email = activitySignupBinding.teSignupEmail.getText().toString().trim();
                String password = activitySignupBinding.teSignupPassword.getText().toString();

                if (namelastname.length() >= 5 && Patterns.EMAIL_ADDRESS.matcher(email).matches() && password.length() >= 6) {

                    createUser(view, namelastname, email, password);

                }else {
                    Snackbar.make(view, "Giriş bilgilerinizi kontrol ediniz", Snackbar.LENGTH_LONG).show();
                }
            }
        });
    }

    private void createUser(View view, String namelastname, String email, String password) {

        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {

                HashMap<String, String> userMap = new HashMap<>();

                userMap.put("namelastname", namelastname);
                userMap.put("photo", "https://i.hizliresim.com/qh03sqe.png");
                userMap.put("email", email);
                userMap.put("password", password);
                userMap.put("uid", Objects.requireNonNull(authResult.getUser()).getUid());

                databaseReference.push().setValue(userMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        toMainPage();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Snackbar.make(view, Objects.requireNonNull(e.getMessage()), Snackbar.LENGTH_LONG).show();
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Snackbar.make(view, Objects.requireNonNull(e.getMessage()), Snackbar.LENGTH_LONG).show();
            }
        });

    }

    private void disableAnimation() {
        activitySignupBinding.cvTvSignup.setTextColor(getResources().getColor(R.color.purple_700));
        activitySignupBinding.cvClSignup.setBackgroundResource(R.drawable.cardviewbackground);
    }

    private void toSigninPage() {
        Intent intent = new Intent(SignupActivity.this, SigninActivity.class);
        startActivity(intent);
        finish();
        CustomIntent.customType(SignupActivity.this, "fadein-to-fadeout");
    }

    private void toMainPage() {
        disableAnimation();
        Intent intent = new Intent(SignupActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
        CustomIntent.customType(SignupActivity.this, "fadein-to-fadeout");
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}