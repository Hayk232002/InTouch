package com.example.hayk.messenger;


import android.content.Context;
import android.support.annotation.NonNull;
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

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;

    private List<TextTemplate> list;
    private List<ChatModel> list_chat_model;
    private LayoutInflater inflater;

    private final int textOnly=1;
    private final int notTextOnly=0;
    private final int typing=2;

    private DatabaseReference mRef;

    int count=0;

    private String userID;
    private String opponentUid;
    private String downloadUri;

    public ChatAdapter( LayoutInflater inflater, String opponentUid, String downloadUri, Context context) {
        this.inflater = inflater;
        this.opponentUid = opponentUid;
        this.downloadUri = downloadUri;
        this.context = context;

        list = new ArrayList<>();
        list_chat_model = new ArrayList<>();
        mRef = FirebaseDatabase.getInstance().getReference("message");
        mRef.addChildEventListener(new MyChildEventListener());

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        userID=user.getUid();
    }

    class MyChildEventListener implements ChildEventListener{

        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            ChatModel chatModel = dataSnapshot.getValue(ChatModel.class);
            list_chat_model.add(chatModel);

            Log.wtf("list_chat_model" , list_chat_model.get(count).getText() +" "+ list_chat_model.get(count).getToid()+" "+list_chat_model.get(count).getFromid());

            if (list_chat_model.get(count).getFromid().equals(userID) && list_chat_model.get(count).getToid().equals(opponentUid)){
                list.add(new TextTemplate(list_chat_model.get(count).getText(),textOnly));
            }

            else if (list_chat_model.get(count).getFromid().equals(opponentUid) && list_chat_model.get(count).getToid().equals(userID)){
                list.add(new TextTemplate(list_chat_model.get(count).getText(),notTextOnly));
            }

            Log.wtf("key",dataSnapshot.getKey());

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
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder=null;
        inflater = LayoutInflater.from(parent.getContext());


        switch (viewType){
            case textOnly:
            {
                View textOnlyViewHolder = inflater.inflate(R.layout.chat_to_row,parent,false);
                viewHolder = new TextOnly(textOnlyViewHolder);

                break;
            }

            case notTextOnly:
            {
                View notTextOnlyViewHolder = inflater.inflate(R.layout.chat_from_row,parent,false);
                viewHolder = new NotOnlyText(notTextOnlyViewHolder);

                break;
            }

            default:
        }

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()){
            case textOnly:
            {
                TextOnly textOnly = (TextOnly) holder;

                textOnly.description.setText(list.get(position).getMessage());

                break;
            }


            case notTextOnly:
            {
                NotOnlyText notOnlyText = (NotOnlyText) holder;

                notOnlyText.setDescription(list.get(position).getMessage());
                Picasso.with(context).load(downloadUri).fit().centerCrop().into(notOnlyText.iv);

                break;
            }
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public int getItemViewType(int position) {

        if (list.get(position).getText_type() == textOnly){
            return textOnly;
        }

        else if (list.get(position).getText_type() == notTextOnly){
            return notTextOnly;
        }

//        else if ()

        return -1;
    }

    class TextOnly extends RecyclerView.ViewHolder{

        private TextView description;

        public TextOnly(View itemView) {
            super(itemView);
            description = itemView.findViewById(R.id.tv_chat_to_row);
        }

        public void setDescription(String des) {
            description.setText(des);
        }
    }

    class NotOnlyText extends RecyclerView.ViewHolder{

        private TextView description;
        private ImageView iv;

        public NotOnlyText(View itemView) {
            super(itemView);
            description = itemView.findViewById(R.id.tv_chat_from_row);
            iv = itemView.findViewById(R.id.iv_chat_from_row);
        }

        public void setDescription(String des) {
            description.setText(des);
        }
    }

    class Typing extends RecyclerView.ViewHolder{

        private TextView description;

        public Typing(@NonNull View itemView) {
            super(itemView);
            description = itemView.findViewById(R.id.tv_typing);
        }

        public void setDescription(String des) {
            description.setText(des);
        }
    }
}