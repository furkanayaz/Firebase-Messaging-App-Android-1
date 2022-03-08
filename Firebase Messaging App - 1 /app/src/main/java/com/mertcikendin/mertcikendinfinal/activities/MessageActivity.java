package com.mertcikendin.mertcikendinfinal.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mertcikendin.mertcikendinfinal.R;
import com.mertcikendin.mertcikendinfinal.adapters.MessageAdapter;
import com.mertcikendin.mertcikendinfinal.databinding.ActivityMessageBinding;
import com.mertcikendin.mertcikendinfinal.models.Message;
import com.mertcikendin.mertcikendinfinal.models.User;
import com.squareup.picasso.Picasso;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import maes.tech.intentanim.CustomIntent;

public class MessageActivity extends AppCompatActivity {
    private ActivityMessageBinding activityMessageBinding = null;
    private MessageAdapter messageAdapter = null;
    private ArrayList<Message> messageArrayList = null;
    public static User user = null;

    private FirebaseAuth firebaseAuth = null;
    private FirebaseUser firebaseUser = null;
    private FirebaseDatabase firebaseDatabase = null;
    private DatabaseReference databaseReference = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMessageBinding = DataBindingUtil.setContentView(MessageActivity.this, R.layout.activity_message);

        getUserInfo();
        initializeFirebase();
        setLayoutManager();
        setItems();

        activityMessageBinding.ivProfileBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        activityMessageBinding.etClCircleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                sendMessage(view);

            }
        });

    }

    private void sendMessage(View view) {
        String message = activityMessageBinding.etClMessage.getText().toString().trim();

        if (message.length() != 0) {
            messageArrayList = new ArrayList<>();

            @SuppressLint("SimpleDateFormat") String timeStamp = new SimpleDateFormat("dd/MM/yyyy HH:mm").format(Calendar.getInstance().getTime());
            Message messageobj = new Message(message, firebaseUser.getUid(), user.getUid(), timeStamp);

            databaseReference.push().setValue(messageobj).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    messageArrayList.clear();
                    setItems();
                    activityMessageBinding.etClMessage.setText("");
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Snackbar.make(view, "Mesaj gönderimi sırasında hata meydana geldi.", Snackbar.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void initializeFirebase() {
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("CHATS");
    }

    private void setLayoutManager() {
        activityMessageBinding.rvMessage.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MessageActivity.this, LinearLayoutManager.VERTICAL, false);
        linearLayoutManager.setStackFromEnd(true);
        activityMessageBinding.rvMessage.setLayoutManager(linearLayoutManager);
    }

    private void setItems() {
        messageArrayList = new ArrayList<>();

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                messageArrayList.clear();
                for (DataSnapshot d : snapshot.getChildren()) {
                    Message message = d.getValue(Message.class);
                    if (message.getFrom().equals(user.getUid()) && message.getTo().equals(firebaseUser.getUid())) {
                        messageArrayList.add(message);
                    }else if (message.getFrom().equals(firebaseUser.getUid()) && message.getTo().equals(user.getUid())) {
                        messageArrayList.add(message);
                    }
                }
                setAdapter();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void getUserInfo() {
        Intent intent = getIntent();
        user = (User) intent.getSerializableExtra("user");

        Picasso.get().load(Uri.parse(user.getPhoto())).into(activityMessageBinding.clMessagePhoto);

        activityMessageBinding.tvClMessageTitle.setText(user.getNamelastname());
    }

    @SuppressLint("NotifyDataSetChanged")
    private void setAdapter() {
        messageAdapter = new MessageAdapter(MessageActivity.this, messageArrayList);
        messageAdapter.notifyDataSetChanged();
        activityMessageBinding.rvMessage.setAdapter(messageAdapter);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(MessageActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
        CustomIntent.customType(MessageActivity.this, "fadein-to-fadeout");
    }
}