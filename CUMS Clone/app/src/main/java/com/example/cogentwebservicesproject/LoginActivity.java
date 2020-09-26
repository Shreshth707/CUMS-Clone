package com.example.cogentwebservicesproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    private Button mLogin;
    private EditText mEmail, mPassword;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        databaseReference = FirebaseDatabase.getInstance().getReference("users");
        final ProgressBar progressBar = new ProgressBar(LoginActivity.this);

        mLogin = findViewById(R.id.login);

        mEmail =  findViewById(R.id.email);
        mPassword = findViewById(R.id.password);

        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean connected = false;
                ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
                if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() != NetworkInfo.State.CONNECTED &&
                        connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() != NetworkInfo.State.CONNECTED) {
                    //we are not connected to a network
                    Toast.makeText(LoginActivity.this, "Check Internet Connection and Try Again", Toast.LENGTH_SHORT).show();
                    return;
                }

                final String email = mEmail.getText().toString();
                final String password = mPassword.getText().toString();


                if(email.length()==0){
                    mEmail.requestFocus();
                    mEmail.setError("Roll Number cannot be empty");
                    //progressBar.dismissDialog();
                    return;
                }
                if(password.length()==0) {
                    mPassword.requestFocus();
                    mPassword.setError("Please Enter Password");
                    //progressBar.dismissDialog();
                    return;
                }

                progressBar.startLoading();
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.dismissDialog();
                    }
                },5000);

                databaseReference.child(email).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                            String pass = dataSnapshot.child("pass").getValue().toString();
                            if(pass.equals(password)){
                                Intent intent = new Intent(getApplicationContext(),VerificationSuccessful.class);
                                progressBar.dismissDialog();
                                startActivity(intent);
                            }else{
                                mPassword.requestFocus();
                                mPassword.setError("Incorrect Password");
                                progressBar.dismissDialog();
                                return;
                            }
                        }else {
                            mEmail.requestFocus();
                            mEmail.setError("Roll Number does not Exists");
                            progressBar.dismissDialog();
                            return;
                        }



                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });
    }
}
