package com.example.lukehogan.friendtrack;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Settings extends AppCompatActivity {
    private EditText txtPass,txtName, img;
    private Button btnUpdate, btnLogout, btnDelete,btnUpdate2,button;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseDatabase db;
    private DatabaseReference dbRef;
    private String TAG = "Settings";

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(authStateListener);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        db = FirebaseDatabase.getInstance();
        dbRef = db.getReference();
        img = (EditText) findViewById(R.id.editText);
        txtPass = (EditText) findViewById(R.id.txtPassChange);
        txtName = (EditText) findViewById(R.id.txtNameChange);
        btnUpdate = (Button) findViewById(R.id.btnUpdate);
        btnUpdate2 = (Button) findViewById(R.id.btnUpdate2);
        button = (Button) findViewById(R.id.button);
        btnLogout = (Button) findViewById(R.id.btnLogout);
        btnDelete = (Button) findViewById(R.id.btnDelete);
        mAuth = FirebaseAuth.getInstance();
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser() == null){
                    startActivity(new Intent(Settings.this, LoginPage.class));
                }
            }};
        btnLogout.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                Toast.makeText(getApplicationContext(), "Successfully signed out", Toast.LENGTH_LONG).show();
            }
        });
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if(user != null) {
                    if(txtPass.getText().toString().length() < 6){ //Checks if password is empty
                        txtPass.setError("Password must be minimum 6 characters");
                        txtPass.requestFocus();
                        return;
                    }

                    user.updatePassword(txtPass.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()) {
                                Toast.makeText(getApplicationContext(), "Password successfully updated", Toast.LENGTH_LONG).show();
                                mAuth.signOut();
                                finish();
                                startActivity(new Intent(Settings.this, LoginPage.class));
                            }else{
                                Toast.makeText(getApplicationContext(),task.getException().getMessage(), Toast.LENGTH_SHORT);
                            }
                        }
                    });
                }
            }
        });
        btnUpdate2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG,"onClick: Attempting to add to db");
                String name = txtName.getText().toString();
                if(name.equals("")){
                    txtName.setError("Field cannot be blank");
                    txtName.requestFocus();
                }else{
                    FirebaseUser user = mAuth.getCurrentUser();
                    String userID = user.getUid();
                    dbRef.child("Users").child(userID).child("name").setValue(name);
                    Toast.makeText(getApplicationContext(),"Name updated", Toast.LENGTH_SHORT).show();
                }
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG,"onClick: Attempting to add to db");
                String image = img.getText().toString();
                if(image.equals("") || image.equals("default")){
                    txtName.setError("Field cannot be blank");
                    txtName.requestFocus();
                }else{
                    FirebaseUser user = mAuth.getCurrentUser();
                    String userID = user.getUid();
                    dbRef.child("Users").child(userID).child("url").setValue(image);
                    Toast.makeText(getApplicationContext(),"Image updated", Toast.LENGTH_SHORT).show();
                }
            }
        });
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user != null) {
                    user.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()) {
                                Toast.makeText(getApplicationContext(), "Account Deleted", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(Settings.this,Register.class));
                            }
                        }
                    });
                }
            }
        });

    }

}
