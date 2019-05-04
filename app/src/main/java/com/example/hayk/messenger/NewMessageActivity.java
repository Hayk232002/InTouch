package com.example.hayk.messenger;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

public class NewMessageActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private UserAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_message);
        getSupportActionBar().setTitle("Select User");

        recyclerView=(RecyclerView) findViewById(R.id.rv);
        LinearLayoutManager lin = new LinearLayoutManager(this);
        lin.setOrientation(LinearLayoutManager.VERTICAL);

        adapter = new UserAdapter(NewMessageActivity.this);
        recyclerView.setLayoutManager(lin);
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
    }

}
