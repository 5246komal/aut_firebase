package com.example.auth_firebase;

import androidx.appcompat.app.AppCompatActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
public class login extends AppCompatActivity {

    EditText email, pass;
    Button next, signIn;
    FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        email = findViewById(R.id.email);
        pass = findViewById(R.id.password);
        next = findViewById(R.id.signin);
        signIn = findViewById(R.id.signup);
        firebaseAuth =FirebaseAuth.getInstance();
        Intent intent = getIntent();

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(login.this,MainActivity.class);
                startActivity(intent);
            }
        });




        next.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String n = intent.getStringExtra("name");
                String em =intent.getStringExtra("email");
                String pm =intent.getStringExtra("pass");

                String e = email.getText().toString();

                String p = pass.getText().toString();

                if(e.isEmpty()){
                    Toast.makeText(getApplicationContext(), "plaese enter the email", Toast.LENGTH_SHORT).show();
                }
                else if(p.isEmpty()){
                    Toast.makeText(getApplicationContext(), "plaese enter the password", Toast.LENGTH_SHORT).show();
                }
                else {
                    firebaseAuth.signInWithEmailAndPassword(e, p).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                if (firebaseAuth.getCurrentUser().isEmailVerified()) {
                                    Intent intent = new Intent(login.this, Profile.class);
                                    intent.putExtra("name",n);
                                    intent.putExtra("email",em);
                                    intent.putExtra("pass",pm);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    Toast.makeText(getApplicationContext(), "Please Varify Email", Toast.LENGTH_SHORT).show();

                                }
                            }}

                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });




    }



}