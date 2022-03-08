package com.mertcikendin.mertcikendinfinal.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import com.mertcikendin.mertcikendinfinal.activities.MessageActivity;
import com.mertcikendin.mertcikendinfinal.R;
import com.mertcikendin.mertcikendinfinal.models.User;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import de.hdodenhof.circleimageview.CircleImageView;
import maes.tech.intentanim.CustomIntent;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.CardViewDesignThingsHolder> {
    private Context context;
    private ArrayList<User> userArrayList = new ArrayList<>();

    public ContactAdapter(Context context, ArrayList<User> userArrayList) {
        this.context = context;
        this.userArrayList = userArrayList;
    }

    @NonNull
    @Override
    public CardViewDesignThingsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.cardviewcontact, parent, false);
        return new CardViewDesignThingsHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CardViewDesignThingsHolder holder, int position) {
        User user = userArrayList.get(position);

        holder.cvContactNameLastname.setText(user.getNamelastname());

        Picasso.get().load(Uri.parse(user.getPhoto())).into(holder.cvContactPhoto);

        holder.clCvContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toMessagePage(user);
            }
        });

    }

    @Override
    public int getItemCount() {
        return userArrayList.size();
    }

    private void toMessagePage(User user) {
        Intent intent = new Intent(context, MessageActivity.class);
        intent.putExtra("user", user);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
        CustomIntent.customType(context, "fadein-to-fadeout");
    }

    public static class CardViewDesignThingsHolder extends RecyclerView.ViewHolder {
        private ConstraintLayout clCvContact = null;
        private CircleImageView cvContactPhoto = null;
        private TextView cvContactNameLastname = null;

        public CardViewDesignThingsHolder(@NonNull View itemView) {
            super(itemView);
            clCvContact = itemView.findViewById(R.id.clCvContact);
            cvContactPhoto = itemView.findViewById(R.id.clMessagePhoto);
            cvContactNameLastname = itemView.findViewById(R.id.cvContactNameLastname);
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    public void filterUser(ArrayList<User> userFilterArrayList) {
        userArrayList = userFilterArrayList;
        notifyDataSetChanged();
    }
}
