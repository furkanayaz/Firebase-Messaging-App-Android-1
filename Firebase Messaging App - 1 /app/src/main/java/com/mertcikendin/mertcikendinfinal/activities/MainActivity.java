package com.mertcikendin.mertcikendinfinal.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mertcikendin.mertcikendinfinal.R;
import com.mertcikendin.mertcikendinfinal.adapters.ContactAdapter;
import com.mertcikendin.mertcikendinfinal.databinding.ActivityMainBinding;
import com.mertcikendin.mertcikendinfinal.models.User;
import com.squareup.picasso.Picasso;

import net.yslibrary.android.keyboardvisibilityevent.util.UIUtil;

import java.util.ArrayList;

import maes.tech.intentanim.CustomIntent;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding activityMainBinding = null;
    private ContactAdapter contactAdapter = null;
    private ArrayList<User> userArrayList = null;

    private FirebaseAuth firebaseAuth = null;
    private FirebaseUser firebaseUser = null;
    private FirebaseDatabase firebaseDatabase = null;
    private DatabaseReference databaseReference = null;
    private FirebaseStorage firebaseStorage = null;
    private StorageReference storageReference = null;

    private BottomSheetDialog bottomSheetDialog = null;

    private String userkey = "";
    private String password = "";
    private int visibilitycontroller = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMainBinding = DataBindingUtil.setContentView(MainActivity.this, R.layout.activity_main);

        initializeFirebase();
        setNavigationItems();
        setLayoutManager();
        setItems();

        activityMainBinding.ivMainMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activityMainBinding.rvMain.setVisibility(View.INVISIBLE);
                activityMainBinding.clMainMenu.setVisibility(View.VISIBLE);
            }
        });

        activityMainBinding.ivMainSearchAndExit.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("UseCompatLoadingForDrawables")
            @Override
            public void onClick(View view) {

                switch (visibilitycontroller % 2) {
                    case 0:
                        UIUtil.showKeyboard(MainActivity.this, activityMainBinding.etMainSearch);
                        activityMainBinding.ivMainSearchAndExit.setImageDrawable(getDrawable(R.drawable.close));
                        activityMainBinding.tvMainHeader.setVisibility(View.INVISIBLE);
                        activityMainBinding.etMainSearch.setVisibility(View.VISIBLE);
                        break;
                    case 1:

                        if (activityMainBinding.etMainSearch.getText().toString().trim().length() != 0) {
                            activityMainBinding.etMainSearch.setText("");
                            userArrayList.clear();
                            setItems();
                        }

                        UIUtil.hideKeyboard(MainActivity.this);
                        activityMainBinding.ivMainSearchAndExit.setImageDrawable(getDrawable(R.drawable.search));
                        activityMainBinding.tvMainHeader.setVisibility(View.VISIBLE);
                        activityMainBinding.etMainSearch.setVisibility(View.INVISIBLE);
                        break;
                }

                visibilitycontroller++;
            }
        });

        activityMainBinding.etMainSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                filterList(editable.toString());
            }
        });

        activityMainBinding.ivMainBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activityMainBinding.rvMain.setVisibility(View.VISIBLE);
                activityMainBinding.clMainMenu.setVisibility(View.INVISIBLE);
            }
        });

        activityMainBinding.clMainProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activityMainBinding.rvMain.setVisibility(View.VISIBLE);
                activityMainBinding.clMainMenu.setVisibility(View.INVISIBLE);

                bottomSheetDialog = new BottomSheetDialog(MainActivity.this);
                View viewprofile = LayoutInflater.from(MainActivity.this).inflate(R.layout.profiledialog, null, false);

                TextInputEditText teProfileNameLastname = viewprofile.findViewById(R.id.teProfileNameLastname);
                TextInputEditText teProfilePassword = viewprofile.findViewById(R.id.teProfilePassword);
                CardView cvProfileSave = viewprofile.findViewById(R.id.cvProfileSave);
                ConstraintLayout clProfileSave = viewprofile.findViewById(R.id.clProfileSave);
                TextView cvTvProfileSave = viewprofile.findViewById(R.id.cvTvProfileSave);

                CardView cvProfileAddPhoto = viewprofile.findViewById(R.id.cvProfileAddPhoto);
                ConstraintLayout clProfileAddPhoto = viewprofile.findViewById(R.id.clProfileAddPhoto);
                TextView cvTvProfileAddPhoto = viewprofile.findViewById(R.id.cvTvProfileAddPhoto);

                bottomSheetDialog.setContentView(viewprofile);

                cvProfileAddPhoto.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View view, MotionEvent motionEvent) {

                        switch (motionEvent.getAction()) {
                            case MotionEvent.ACTION_DOWN:
                                cvTvProfileAddPhoto.setTextColor(getResources().getColor(R.color.white));
                                clProfileAddPhoto.setBackgroundColor(getResources().getColor(R.color.purple_700));
                                break;
                            case MotionEvent.ACTION_UP:
                                cvTvProfileAddPhoto.setTextColor(getResources().getColor(R.color.purple_700));
                                clProfileAddPhoto.setBackgroundResource(R.drawable.cardviewbackground);
                                break;
                            case MotionEvent.ACTION_CANCEL:
                                cvTvProfileAddPhoto.setTextColor(getResources().getColor(R.color.purple_700));
                                clProfileAddPhoto.setBackgroundResource(R.drawable.cardviewbackground);
                                break;
                        }

                        return false;
                    }
                });

                cvProfileSave.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View view, MotionEvent motionEvent) {

                        switch (motionEvent.getAction()) {
                            case MotionEvent.ACTION_DOWN:
                                cvTvProfileSave.setTextColor(getResources().getColor(R.color.white));
                                clProfileSave.setBackgroundColor(getResources().getColor(R.color.purple_700));
                                break;
                            case MotionEvent.ACTION_UP:
                                cvTvProfileSave.setTextColor(getResources().getColor(R.color.purple_700));
                                clProfileSave.setBackgroundResource(R.drawable.cardviewbackground);
                                break;
                            case MotionEvent.ACTION_CANCEL:
                                cvTvProfileSave.setTextColor(getResources().getColor(R.color.purple_700));
                                clProfileSave.setBackgroundResource(R.drawable.cardviewbackground);
                                break;
                        }

                        return false;
                    }
                });

                cvProfileAddPhoto.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        cvProfileAddPhotoAnimation(cvTvProfileAddPhoto, cvProfileAddPhoto);
                        getPhotoIntent();
                    }
                });

                cvProfileSave.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        cvProfileSaveAnimation(cvTvProfileSave, cvProfileSave);
                        updateProfileInformation(teProfileNameLastname.getText().toString().trim(), teProfilePassword.getText().toString());
                    }
                });

                bottomSheetDialog.create();
                bottomSheetDialog.show();

            }
        });

        activityMainBinding.clMainLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (firebaseUser != null) {
                    firebaseAuth.signOut();
                    Intent intent = new Intent(MainActivity.this, SplashActivity.class);
                    startActivity(intent);
                    CustomIntent.customType(MainActivity.this, "fadein-to-fadeout");
                    finish();
                }else {
                    Snackbar.make(view, "Çıkış yapılacak bir oturum bulunamadı", Snackbar.LENGTH_LONG).show();
                }
            }
        });

    }

    private void initializeFirebase() {
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("USERS");
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference("PHOTOS").child(firebaseUser.getUid());
    }

    private void setLayoutManager() {
        activityMainBinding.rvMain.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MainActivity.this, LinearLayoutManager.VERTICAL, false);
        activityMainBinding.rvMain.setLayoutManager(linearLayoutManager);
    }

    private void setNavigationItems() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot d : snapshot.getChildren()) {
                    User user = d.getValue(User.class);

                    if (user != null && user.getUid().equals(firebaseUser.getUid())) {
                        userkey = d.getKey();
                        password = user.getPassword();
                        setUserInfo(user.getPhoto(), user.getNamelastname(), user.getEmail());
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void setUserInfo(String photo, String namelastname, String email) {
        if (Patterns.WEB_URL.matcher(photo).matches()) {
            Picasso.get().load(photo).into(activityMainBinding.ivMainProfile);
        }
        activityMainBinding.textView2.setText(namelastname);
        activityMainBinding.textView3.setText(email);
    }

    private void setItems() {
        userArrayList = new ArrayList<>();

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userArrayList.clear();
                for (DataSnapshot d : snapshot.getChildren()) {
                    User user = d.getValue(User.class);
                    if (user != null  && !user.getEmail().equals(firebaseUser.getEmail())) {
                        userArrayList.add(user);
                    }
                }
                setAdapter();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @SuppressLint("NotifyDataSetChanged")
    private void setAdapter() {
        contactAdapter = new ContactAdapter(MainActivity.this, userArrayList);
        contactAdapter.notifyDataSetChanged();
        activityMainBinding.rvMain.setAdapter(contactAdapter);
    }

    private void filterList(String text) {
        ArrayList<User> userFilterArrayList = new ArrayList<>();

        for (User user : userArrayList
             ) {
            if (user.getNamelastname().toLowerCase().replaceAll(" ", "").contains(text.toLowerCase().replaceAll(" ", ""))) {
                userFilterArrayList.add(user);
            }
        }
        contactAdapter.filterUser(userFilterArrayList);
    }

    private void cvProfileAddPhotoAnimation(TextView cvTvProfileAddPhoto, CardView cvProfileAddPhoto) {
        cvTvProfileAddPhoto.setTextColor(getResources().getColor(R.color.purple_700));
        cvProfileAddPhoto.setBackgroundResource(R.drawable.cardviewbackground);

    }

    private void cvProfileSaveAnimation(TextView cvTvProfileSave, CardView cvProfileSave) {
        cvTvProfileSave.setTextColor(getResources().getColor(R.color.purple_700));
        cvProfileSave.setBackgroundResource(R.drawable.cardviewbackground);
    }

    private void getPhotoIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Fotoğraf seçiniz"), 1);
    }

    private void updateProfilePhoto(Uri imageuri) {

        storageReference.putFile(imageuri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        databaseReference.child(userkey).child("photo").setValue(String.valueOf(uri)).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                if (bottomSheetDialog != null) {
                                    bottomSheetDialog.dismiss();
                                }
                                toSplashPage();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void updateProfileInformation(String namelastname, String password) {
        if (this.password.equals(password)) {
            databaseReference.child(userkey).child("namelastname").setValue(namelastname).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    toSplashPage();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        }else {
            Toast.makeText(MainActivity.this, "Girdiğiniz şifre hatalı olduğundan işlem gerçekleşemedi", Toast.LENGTH_LONG).show();
        }
    }

    private void toSplashPage() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(MainActivity.this, SplashActivity.class);
                startActivity(intent);
                CustomIntent.customType(MainActivity.this, "fadein-to-fadeout");
                finish();
            }
        }, 1000);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            Uri imageuri = data.getData();
            updateProfilePhoto(imageuri);
        }

    }
}