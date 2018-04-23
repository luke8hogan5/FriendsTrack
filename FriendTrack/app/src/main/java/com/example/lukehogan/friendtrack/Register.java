package com.example.lukehogan.friendtrack;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Register extends AppCompatActivity implements View.OnClickListener {
    private Button reg;
    private EditText etEmail, etPass,etName,img;
    private TextView tvLogin;
    private FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        firebaseAuth = FirebaseAuth.getInstance();
        reg = (Button) findViewById(R.id.btnLog);
        etEmail = (EditText) findViewById(R.id.email);
        etPass = (EditText) findViewById(R.id.pass);
        etName = (EditText) findViewById(R.id.username);
        img = (EditText) findViewById(R.id.imgUrl);
        tvLogin = (TextView) findViewById(R.id.tvSignIn);
        reg.setOnClickListener(this);
        tvLogin.setOnClickListener(this);
    }
    private void registerUser(){
        final String email = etEmail.getText().toString().trim();
        final String pass = etPass.getText().toString().trim();
        final String name = etName.getText().toString().trim();
        final String _img = img.getText().toString().trim();
        if(TextUtils.isEmpty(email)){ //Checks if Email is empty
            etEmail.setError("Email must be provided");
            etEmail.requestFocus();
            return;//ends function early
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            etEmail.setError("Invalid email form");
            etEmail.requestFocus();
            return;
        }
        if(TextUtils.isEmpty(pass)){ //Checks if password is empty
                etPass.setError("Password must be provided");
                etEmail.requestFocus();
                return;
        }
        if(pass.length() < 6){ //Checks if password is empty
            etPass.setError("Password must be minimum 6 characters");
            etEmail.requestFocus();
            return;
        }
        //if code reaches here, email & pass fields are not empty
        firebaseAuth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "Register success", Toast.LENGTH_SHORT).show();
                    firebaseAuth.signInWithEmailAndPassword(email,pass);
                    DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference().child("Users");
                    DatabaseReference dbCurrentUsers = dbRef.child(firebaseAuth.getCurrentUser().getUid());
                    dbCurrentUsers.child("name").setValue(name);
                    dbCurrentUsers.child("email").setValue(email);
                    dbCurrentUsers.child("url").setValue(_img);
                    startActivity(new Intent(Register.this, FriendsList.class));

                }else{
                    if(task.getException() instanceof FirebaseAuthUserCollisionException){
                        Toast.makeText(getApplicationContext(),"Email already in use", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(getApplicationContext(),task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
    @Override
    public void onClick(View v) {
        if(v == reg){
            registerUser();
        }else if(v == tvLogin){
            //Sends to login
            startActivity(new Intent(Register.this, LoginPage.class));

        }
    }
}
