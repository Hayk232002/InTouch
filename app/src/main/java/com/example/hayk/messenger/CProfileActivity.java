package com.example.hayk.messenger;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CProfileActivity extends AppCompatActivity {

    private RecyclerView rv_profile;
    private ProfileAdapter adapter;

    private String userID;
    ArrayList<String> array = new ArrayList<>();
    String username_string;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        rv_profile = (RecyclerView) findViewById(R.id.rv_profile);
        LinearLayoutManager lin = new LinearLayoutManager(this);
        lin.setOrientation(LinearLayoutManager.VERTICAL);

        adapter = new ProfileAdapter(CProfileActivity.this);
        rv_profile.setLayoutManager(lin);
        rv_profile.setAdapter(adapter);
        rv_profile.setHasFixedSize(true);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseDatabase mFirebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference myRef = mFirebaseDatabase.getReference("users");

        FirebaseUser user = mAuth.getCurrentUser();
        userID=user.getUid();

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                showData(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    private void showData(DataSnapshot dataSnapshot) {
        UserInformation uInfo = new UserInformation();
        uInfo.setUsername(dataSnapshot.child(userID).getValue(UserInformation.class).getUsername());
        uInfo.setUid(dataSnapshot.child(userID).getValue(UserInformation.class).getUid());

        array.add(uInfo.getUsername());
        array.add(uInfo.getUid());

        username_string=array.get(0);

        getSupportActionBar().setTitle(username_string);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_profile,menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.item_newMessage: {
                Intent intent = new Intent(CProfileActivity.this,NewMessageActivity.class);
                startActivity(intent);

                break;
            }
            case R.id.item_signOut: {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(this,BLoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);

                break;
            }

        }
        return super.onOptionsItemSelected(item);
    }
}
