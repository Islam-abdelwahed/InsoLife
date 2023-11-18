package com.example.insulife.Activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.insulife.DataBase;
import com.example.insulife.databinding.ActivityLoginBinding;
import com.google.firebase.auth.FirebaseAuth;

public class Login_activity extends AppCompatActivity {
    private ActivityLoginBinding binding;
    private FirebaseAuth auth;
    private DataBase dataBase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        // checkPermission(new String[]{Manifest.permission.
        //          , Manifest.permission.INTERNET}, 1);

        binding.loginBtn.setOnClickListener(e -> {
        });
        binding.registerTxt.setOnClickListener(l -> {
            Intent intent = new Intent(getApplicationContext(), Register_activity.class);
            startActivity(intent);
        });



        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if(ContextCompat.checkSelfPermission(Login_activity.this, Manifest.permission.POST_NOTIFICATIONS)!= PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(Login_activity.this,new String[]{Manifest.permission.POST_NOTIFICATIONS},101);
            }
        }

        binding.loginBtn.setOnClickListener(l -> {
            String email = binding.loginEmailTextfield.getText().toString() + "";
            String password = binding.loginPasswordTextfield.getText().toString() + "";
            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please Fill all fields", Toast.LENGTH_SHORT).show();
            } else {
                signIn(email, password);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        auth = FirebaseAuth.getInstance();
        dataBase = DataBase.getInstance();
        if (dataBase.getUser() != null) {
            Intent i = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(i);
            finish();
        }
    }

    private void signIn(String email, String password) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {

                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);
                finish();

            } else {
                Toast.makeText(this, "Not valid email or password", Toast.LENGTH_SHORT).show();
            }
        }).addOnCanceledListener(() -> {
            Toast.makeText(this, "some error occurred (CANCELED)", Toast.LENGTH_SHORT).show();
        }).addOnFailureListener(e -> {
            Toast.makeText(this, "some error occurred (FAILED)", Toast.LENGTH_SHORT).show();
        });
    }

    public void checkPermission(String[] permission, int requestCode) {
        ActivityCompat.requestPermissions(Login_activity.this, permission, requestCode);
    }


    @Override
    protected void onPause() {
        super.onPause();
        dataBase.finish();
        dataBase = null;
    }
}