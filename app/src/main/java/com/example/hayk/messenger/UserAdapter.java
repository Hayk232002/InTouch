package com.example.hayk.messenger;

import android.content.Context;
import android.content.Intent;
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
import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder>{

    private List<UserInformation> list;
    private List<UserInformation> result;
    private List<UserInformation> mList;
    private Context context;

    private DatabaseReference mRef;
    private FirebaseAuth mAuth;

    private String uid;
    private String userID;
    private int count=0;

    public UserAdapter(Context context) {
        this.context=context;

        mAuth=FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        userID=user.getUid();

        list = new ArrayList<>();
        result = new ArrayList<>();
        mList = new ArrayList<>();
        mRef = FirebaseDatabase.getInstance().getReference("users");
        mRef.addChildEventListener(new MyChildEventListener());
    }

    class MyChildEventListener implements ChildEventListener{

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

    @Override
    public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new UserViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_new_message_users, parent, false));
    }

    @Override
    public void onBindViewHolder(final UserViewHolder holder, int position) {
        final UserInformation user = list.get(position);


        holder.tv.setText(user.username);
        Picasso.with(context).load(user.getDownloadUri()).fit().centerCrop().into(holder.iv_new_message);

        Log.wtf("lol",user.getDownloadUri());

        holder.cl_new_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,DChatActivity.class);
                intent.putExtra("username",user.username);
                intent.putExtra("uid",user.uid);
                intent.putExtra("downloadUri",user.downloadUri);
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
        return list.size();
    }

    class UserViewHolder extends RecyclerView.ViewHolder{

        TextView tv;
        ImageView iv_new_message;
        ConstraintLayout cl_new_message;

        public UserViewHolder(View itemView) {
            super(itemView);

            tv=(TextView) itemView.findViewById(R.id.tv_username_profile_row);
            iv_new_message=(ImageView) itemView.findViewById(R.id.iv_profile_row);
            cl_new_message=(ConstraintLayout) itemView.findViewById(R.id.cl_new_message);
        }
    }
}