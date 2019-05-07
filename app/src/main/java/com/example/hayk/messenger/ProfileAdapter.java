package com.example.hayk.messenger;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ProfileAdapter extends  RecyclerView.Adapter<ProfileAdapter.ProfileViewHolder>{

    Context context;

    private List<UserInformation> list;
    private List<UserInformation> result;
    private List<UserInformation> mList;

    private DatabaseReference mRef;

    List<ProfileModel> list_profile_model;
    HashMap<String, ProfileModel> hashMap;
    private DatabaseReference mRef_latest_message;
    private FirebaseAuth mAuth;

    private String uid;
    private String userID;
    private int count=0;

    public ProfileAdapter(Context context) {
        this.context=context;

        mAuth=FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        userID=user.getUid();
        list_profile_model= new ArrayList<>();
        hashMap = new HashMap<>();

        list = new ArrayList<>();
        result = new ArrayList<>();
        mList = new ArrayList<>();

        mRef = FirebaseDatabase.getInstance().getReference("users");
        mRef.addChildEventListener(new MyUsersChildEventListener());

        mRef_latest_message = FirebaseDatabase.getInstance().getReference("latest_message").child(userID);
        mRef_latest_message.addChildEventListener(new MyLatestMessageChildEventListener());
    }

    class MyLatestMessageChildEventListener implements ChildEventListener {

        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            ProfileModel profileModel = dataSnapshot.getValue(ProfileModel.class);
            list_profile_model.add(profileModel);

            hashMap.put(dataSnapshot.getKey(), profileModel);

            Log.wtf("list_chat_model","mtav");
            notifyDataSetChanged();
        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            ProfileModel profileModel = dataSnapshot.getValue(ProfileModel.class);
            list_profile_model.add(profileModel);

            hashMap.put(dataSnapshot.getKey(), profileModel);

            Log.wtf("list_chat_model","mtav");
            notifyDataSetChanged();
        }

        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {

        }

        @Override
        public void onChildMoved(DataSnapshot dataSnapshot, String s) {

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    }

    class MyUsersChildEventListener implements ChildEventListener{
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {

            UserInformation userInformation = dataSnapshot.getValue(UserInformation.class);

            result.add(userInformation);
            uid=result.get(count).getUid();
            if (!uid.equals(userID)) {
                list.add(userInformation);
            }

            else {
                mList.add(userInformation);
            }
            count++;

            notifyDataSetChanged();

        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {

        }

        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {

        }

        @Override
        public void onChildMoved(DataSnapshot dataSnapshot, String s) {

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    }

    @NonNull
    @Override
    public ProfileViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ProfileViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.profile_row,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ProfileViewHolder holder, int position) {
        final ProfileModel user = list_profile_model.get(position);
        final ProfileModel profileModel = hashMap.get(user.getKey());

        holder.tv_username_profile_row.setText(profileModel.getUsername());
        Picasso.with(context).load(profileModel.getDownloadUri()).fit().centerCrop().into(holder.iv_profile_row);

        holder.tv_text_profile_row.setText(profileModel.getText());

        holder.constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,DChatActivity.class);
                intent.putExtra("username",profileModel.username);
                intent.putExtra("uid",profileModel.key);
                intent.putExtra("downloadUri",profileModel.downloadUri);
                if (!mList.isEmpty()){
                    final UserInformation mUser = mList.get(0);
                    intent.putExtra("myUsername", mUser.getUsername());
                    intent.putExtra("myDownloadUri", mUser.getDownloadUri());
                }
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return hashMap.size();
    }

    class ProfileViewHolder extends RecyclerView.ViewHolder{

        TextView tv_username_profile_row;
        TextView tv_text_profile_row;
        ImageView iv_profile_row;
        ConstraintLayout constraintLayout;

        public ProfileViewHolder(View itemView) {
            super(itemView);

            tv_username_profile_row = (TextView) itemView.findViewById(R.id.tv_username_profile_row);
            tv_text_profile_row = (TextView) itemView.findViewById(R.id.tv_text_profile_row);
            iv_profile_row = (ImageView) itemView.findViewById(R.id.iv_profile_row);
            constraintLayout = (ConstraintLayout) itemView.findViewById(R.id.cl_profile_row);
        }
    }

}
