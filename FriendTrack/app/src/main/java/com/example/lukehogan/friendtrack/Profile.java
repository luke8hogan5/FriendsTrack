package com.example.lukehogan.friendtrack;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import java.io.InputStream;

public class Profile extends AppCompatActivity {
    private Button btnSettings;
    private ImageView img;
    private TextView tvEmail, tvName;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private int camReqCode = 0;
    private StorageReference stRef;
    private FirebaseDatabase db;
    private DatabaseReference dbRef;
    private FirebaseUser user;
    private Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        mAuth = FirebaseAuth.getInstance();
        btnSettings = (Button) findViewById(R.id.btnSettings);
        img = (ImageView) findViewById(R.id.img);
        tvName = (TextView) findViewById(R.id.name);
        tvEmail = (TextView) findViewById(R.id.email);
        db = FirebaseDatabase.getInstance();
        dbRef = db.getReference().child("Users");
        user = mAuth.getCurrentUser();
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (user == null) {
                    startActivity(new Intent(Profile.this, LoginPage.class));
                }
            }
        };
        tvEmail.setText(user.getEmail());

        stRef = FirebaseStorage.getInstance().getReference();
        dbRef = FirebaseDatabase.getInstance().getReference().child("Users");
        dbRef.child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String n = dataSnapshot.child("name").getValue().toString();
                tvName.setText(n);
                String imgURL = dataSnapshot.child("url").getValue().toString();
                /*if(imgURL != "default")
                    img.setImageURI(Uri.parse(imgURL));*/
                new getImageURL(img).execute(imgURL);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        btnSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Profile.this, Settings.class));
            }
        });
    }

    public class getImageURL extends AsyncTask<String, Void, Bitmap> {
        ImageView imageView;

        public getImageURL(ImageView imgV) {
            this.imageView = imgV;
        }

        @Override
        protected Bitmap doInBackground(String... strings) {
            String url = strings[0];
            bitmap = null;
            try {
                InputStream srt = new java.net.URL(url).openStream();
                bitmap = BitmapFactory.decodeStream(srt);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            imageView.setImageBitmap(bitmap);
        }
    }
}