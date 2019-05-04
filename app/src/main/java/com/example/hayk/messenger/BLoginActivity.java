package com.example.hayk.messenger;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class BLoginActivity extends AppCompatActivity {
    public static final String CHANNEL_ID = "hayk";
    public static final String CHANNEL_NAME = "channel_name";
    public static final String CHANNEL_DESC = "channel_desc";

    private FirebaseAuth mAuth;

    EditText et_email_login;
    EditText et_password_login;
    Button btn_signIn_login;
    TextView tv_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        et_email_login=(EditText) findViewById(R.id.et_email_login);
        et_password_login=(EditText) findViewById(R.id.et_password_login);
        btn_signIn_login=(Button)findViewById(R.id.btn_signIn_login);
        tv_back=(TextView) findViewById(R.id.tv_back);

        tv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BLoginActivity.this, ARegisterActivity.class);
                startActivity(intent);
            }
        });

        mAuth = FirebaseAuth.getInstance();

        btn_signIn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                singing(et_email_login.getText().toString(), et_password_login.getText().toString());
            }
        });
    }

    public void singing(String email, String password){
        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    if (mAuth.getCurrentUser().isEmailVerified()) {
                        Intent intent = new Intent(BLoginActivity.this, CProfileActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }

                    else {
                        Toast.makeText(BLoginActivity.this,"Please verify your email address",Toast.LENGTH_SHORT).show();
                    }
                }

                else {
                    Toast.makeText(BLoginActivity.this,"Sign in failed",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
