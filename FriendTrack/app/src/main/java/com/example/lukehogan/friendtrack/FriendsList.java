package com.example.lukehogan.friendtrack;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class FriendsList extends AppCompatActivity {

    DatabaseReference myRef;
    FirebaseDatabase myDatabase;
    ListView myListView;
    ArrayList<Users> userList;
    CustomAdapter adapter;
    Users user;
    EditText addTxt;
    Button addBtn;
    FirebaseAuth mAuth;
    FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends_list);
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        myListView = (ListView)findViewById(R.id.myListView);
        myDatabase = FirebaseDatabase.getInstance();
        myRef = myDatabase.getReference().child("Users");
        userList = new ArrayList<Users>();

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    //user.setName(dataSnapshot.getValue(Users.class).getName());
                    //user.setUrl(dataSnapshot.getValue(Users.class).getUrl());

                    Users user = (Users) data.getValue(Users.class);
                    if(!Objects.equals(currentUser.getEmail(), user.getEmail())) {
                        userList.add(user);

                    }
                }

                if (userList.size() > 0){
                    adapter = new CustomAdapter(FriendsList.this, userList);
                    myListView.setAdapter(adapter);
                }
                else{
                    Toast.makeText(FriendsList.this, "No data", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(FriendsList.this, "Database connection failed", Toast.LENGTH_SHORT).show();
            }
        });

        addBtn = findViewById(R.id.addButton);
        addTxt = (EditText) findViewById(R.id.addFriend);

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userList.clear();
                String text = addTxt.getText().toString();
                myRef.child(currentUser.getUid()).child("friends").child(text).setValue("true");

                Toast.makeText(FriendsList.this, "Friend has been added", Toast.LENGTH_SHORT).show();

            }
        });

        Button btn1;
        btn1 = findViewById(R.id.loc);
        btn1.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                         Intent int1 = new Intent(FriendsList.this, YourLoc.class);
                         startActivity(int1);
                    }
                });
        Button btn2;
        btn2 = findViewById(R.id.prof);
        btn2.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent int2 = new Intent(FriendsList.this, Profile.class);
                        startActivity(int2);
                    }
                });


        myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                              @Override
                                              public void onItemClick(AdapterView<?> adapterView, View view, int pos, long id) {
                                                  String friend = String.valueOf(adapterView.getItemAtPosition(pos));
                                                  Intent int1 = new Intent(FriendsList.this, FriendMap.class);
                                                  int1.putExtra("Uid",friend);
                                                  startActivity(int1);
                                              }
                                          }
        );

    }
}



