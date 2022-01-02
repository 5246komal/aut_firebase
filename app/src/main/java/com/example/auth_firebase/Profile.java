package com.example.auth_firebase;

import androidx.appcompat.app.AppCompatActivity;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


public class Profile extends AppCompatActivity {

    EditText about;
    ImageView img;

    Button btn;
    ActivityResultLauncher<String> mget;
    Uri path;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        about = findViewById(R.id.about);
        img= findViewById(R.id.setImage);
        btn = findViewById(R.id.setNext);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        String a = about.getText().toString();
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mget.launch("image/*");

            }
        });

        mget = registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
            @Override
            public void onActivityResult(Uri result) {
                path = result;
                img.setImageURI(result);


            }
        });
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (about.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(), "plaese enter About", Toast.LENGTH_SHORT).show();
                }
                else if(path==null){
                    Toast.makeText(getApplicationContext(), "Please Select an Image", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(getApplicationContext(), "set profile successfully", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Profile.this, Home.class);
                    startActivity(intent);
                    finish();
                    sendstorage();



                }

            }
        });






    }

    private void sendstorage() {
        Bitmap bitmap = null;
        try {
            bitmap=  MediaStore.Images.Media.getBitmap(getContentResolver(),path);
        }
        catch (IOException e){
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,25,byteArrayOutputStream);
        byte[] data = byteArrayOutputStream.toByteArray();

        StorageReference storageReference = FirebaseStorage.getInstance().getReference();
        StorageReference uploader = storageReference.child("SetImage").child(firebaseAuth.getCurrentUser().getUid());
        uploader.putBytes(data).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                uploader.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Toast.makeText(getApplicationContext(), "stoarge done", Toast.LENGTH_SHORT).show();
                        sendreal(uri);

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });









    }



    private void sendreal(Uri uri) {
        String ab = about.getText().toString();
        String ur = uri.toString();
        String id = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Intent intent = getIntent();
        String n = intent.getStringExtra("name");
        String e =intent.getStringExtra("email");
        String p =intent.getStringExtra("pass");

        HashMap<String , Object> hashMap = new HashMap<>();

        hashMap.put("image",ur);
        hashMap.put("about",ab);
        hashMap.put("name",n);
        hashMap.put("email",e);
        hashMap.put("pass",p);
        hashMap.put("id",id);
        firebaseDatabase.getReference().getRoot().child("Authentication").child(firebaseAuth.getCurrentUser().getUid()).setValue(hashMap);


        Toast.makeText(getApplicationContext(), "realtime done", Toast.LENGTH_SHORT).show();

    }


}