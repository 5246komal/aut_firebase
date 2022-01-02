package com.example.auth_firebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;



import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
public class MainActivity extends AppCompatActivity {

    EditText name,email,pass;
    Button signup,signIn;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        name = findViewById(R.id.Name);
        email = findViewById(R.id.email);
        pass= findViewById(R.id.pass);
        signup = findViewById(R.id.done);
        signIn = findViewById(R.id.signInBtnin);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        progressDialog =new ProgressDialog(this);
        progressDialog.setMessage("plaese wait");
        progressDialog.setTitle("creating your Account");

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,login.class);
                startActivity(intent);
            }
        });


        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(email.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(), "please enter name", Toast.LENGTH_SHORT).show();
                }
                else if(pass.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(), "please enter email", Toast.LENGTH_SHORT).show();

                }
                else if(name.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(), "please enter password", Toast.LENGTH_SHORT).show();

                }
                else {
                    String n = name.getText().toString();
                    String e = email.getText().toString();
                    String p =pass.getText().toString();

                    progressDialog.show();
                    firebaseAuth.createUserWithEmailAndPassword(e,p).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                firebaseAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

                                        if (task.isSuccessful()){

                                            progressDialog.dismiss();
                                            Toast.makeText(getApplicationContext(), "Email send successfully ", Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(MainActivity.this,login.class);
                                            intent.putExtra("name",n);
                                            intent.putExtra("email",e);
                                            intent.putExtra("pass",p);

                                            startActivity(intent);
                                            finish();


//                                            HashMap<String , Object> hashMap = new HashMap<>();
//                                            hashMap.put("name",n);
//                                            hashMap.put("email",e);
//                                            hashMap.put("passsword",p);
//                                            hashMap.put("image","image");
//                                            hashMap.put("about","about");
//
//                                            firebaseDatabase.getReference().getRoot().child("Authentication").child(firebaseAuth.getCurrentUser().getUid()).setValue(hashMap);
                                            Toast.makeText(getApplicationContext(), "send data ", Toast.LENGTH_SHORT).show();
                                        }



                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }

                        }
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
    @Override
    protected void onStart() {
        super.onStart();
        if (firebaseAuth.getCurrentUser()!=null){
            Intent intent = new Intent(MainActivity.this, login.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
    }
}