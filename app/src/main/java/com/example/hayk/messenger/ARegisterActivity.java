package com.example.hayk.messenger;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


public class ARegisterActivity extends AppCompatActivity {
    CallbackManager callbackManager;

    private static final int SELECT_PICTURE = 1;

    EditText et_mail_main;
    EditText et_password_main;
    EditText et_username_main;
    ImageView iv_photo;
    Button btn_reg_main;
    TextView tv;

    private String selectedImagePath;
    private Uri selectedImageUri;
    private Uri downloadUri;

    private FirebaseAuth mAuth;
    private StorageReference storageReference;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        storageReference= FirebaseStorage.getInstance().getReference();
        user = FirebaseAuth.getInstance().getCurrentUser();

        et_mail_main=(EditText) findViewById(R.id.et_email_main);
        et_password_main=(EditText) findViewById(R.id.et_password_main);
        et_username_main=(EditText) findViewById(R.id.et_username_main);
        btn_reg_main=(Button) findViewById(R.id.btn_reg_main);
        tv=(TextView) findViewById(R.id.tv_username_profile_row);
        iv_photo=(ImageView)  findViewById(R.id.iv_photo) ;

        mAuth = FirebaseAuth.getInstance();

        iv_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Select Picture"), SELECT_PICTURE);
            }

        });


        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ARegisterActivity.this,BLoginActivity.class);
                startActivity(intent);
            }
        });


        btn_reg_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registration(et_mail_main.getText().toString(), et_password_main.getText().toString());
            }
        });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode,resultCode,data);
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                selectedImageUri = data.getData();
                selectedImagePath = getPath(selectedImageUri);
                System.out.println("Image Path : " + selectedImagePath);
                iv_photo.setImageURI(selectedImageUri);

                Log.wtf("image",selectedImageUri.toString());
            }
        }
    }

    public String getPath(Uri uri) {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    public void registration(String email, String password){
        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    mAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (selectedImageUri != null && task.isSuccessful()){
                                final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                                final StorageReference ref = storageReference.child(user.getUid());
                                ref.putFile(selectedImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                        ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                            @Override
                                            public void onSuccess(Uri uri) {
                                                downloadUri=uri;
                                                Toast.makeText(ARegisterActivity.this,"Registered successfully. Please check your email for verification",Toast.LENGTH_SHORT).show();

                                                FirebaseDatabase.getInstance().getReference("users").child(user.getUid()).setValue(
                                                        new UserInformation(downloadUri.toString(),user.getUid(),et_username_main.getText().toString()));

                                                Intent intent = new Intent(ARegisterActivity.this,BLoginActivity.class);
                                                startActivity(intent);
                                            }
                                        });
                                    }
                                });
                            }

                            else if (selectedImageUri == null && task.isSuccessful()){
                                final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                                selectedImageUri = Uri.parse("android.resource://com.example.hayk.messenger/drawable/unchnowuser");

                                final StorageReference ref = storageReference.child(user.getUid());
                                ref.putFile(selectedImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                        ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                            @Override
                                            public void onSuccess(Uri uri) {
                                                downloadUri=uri;

                                                Toast.makeText(ARegisterActivity.this,"Registered successfully. Please check your email for verification",Toast.LENGTH_SHORT).show();

                                                FirebaseDatabase.getInstance().getReference("users").child(user.getUid()).setValue(
                                                        new UserInformation(downloadUri.toString(),user.getUid(),et_username_main.getText().toString()));

                                                Intent intent = new Intent(ARegisterActivity.this,BLoginActivity.class);
                                                startActivity(intent);
                                            }
                                        });
                                    }
                                });
                            }

                            else {
                                Toast.makeText(ARegisterActivity.this,task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }

                else {
                    Toast.makeText(ARegisterActivity.this,"Registration failed",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}

