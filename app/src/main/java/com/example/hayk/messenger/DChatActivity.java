package com.example.hayk.messenger;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class DChatActivity extends AppCompatActivity {
    private RecyclerView rv_chat;

    private ChatAdapter adapter;


    private EditText et_chat;
    private ImageButton btn_chat;

    private DatabaseReference reference;
    private FirebaseUser user;

    private String username;
    private String uid;
    private String downloadUri;
    private String myUsername;
    private String myDownloadUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        Intent intent = getIntent();
        username = intent.getStringExtra("username");
        uid = intent.getStringExtra("uid");
        downloadUri = intent.getStringExtra("downloadUri");
        myUsername = intent.getStringExtra("myUsername");
        myDownloadUri = intent.getStringExtra("myDownloadUri");
        getSupportActionBar().setTitle(username);

        reference = FirebaseDatabase.getInstance().getReference("message");
        user = FirebaseAuth.getInstance().getCurrentUser();

        rv_chat = (RecyclerView) findViewById(R.id.rv_chat);
        et_chat = (EditText) findViewById(R.id.et_chat);
        btn_chat = (ImageButton) findViewById(R.id.btn_chat);

        LinearLayoutManager lin = new LinearLayoutManager(this);
        lin.setOrientation(LinearLayoutManager.VERTICAL);
        lin.setStackFromEnd(true);

        rv_chat.setLayoutManager(lin);
        adapter = new ChatAdapter((LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE),uid, downloadUri, DChatActivity.this);
        rv_chat.setAdapter(adapter);
        rv_chat.setHasFixedSize(true);

        btn_chat.setEnabled(false);

        et_chat.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(s.toString().trim().length()==0){
                    btn_chat.setEnabled(false);
                    
                } else {
                    btn_chat.setEnabled(true);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub

            }
        });

        btn_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Log.wtf("click", "userId:"+user.getUid()+" "+"uid:"+uid);

                    String a = et_chat.getText().toString().trim();

                    FirebaseDatabase.getInstance().getReference("message").push().setValue(new ChatModel(user.getUid(), /*et_chat.getText().toString()*/ a, uid));
                    FirebaseDatabase.getInstance().getReference("latest_message").child(user.getUid()).child(uid).setValue(new ProfileModel(downloadUri,user.getUid(),uid,/*et_chat.getText().toString()*/a,uid,username));
                    FirebaseDatabase.getInstance().getReference("latest_message").child(uid).child(user.getUid()).setValue(new ProfileModel(myDownloadUri,user.getUid(),user.getUid(),/*et_chat.getText().toString()*/a,uid,myUsername));
                    rv_chat.scrollToPosition(adapter.getItemCount());
                    et_chat.setText("");
            }
        });
    }
}