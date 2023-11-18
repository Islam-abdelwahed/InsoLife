package com.example.insulife.Activities;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Toast;

import com.example.insulife.DataBase;
import com.example.insulife.ImageControl;
import com.example.insulife.R;
import com.example.insulife.databinding.ActivityRegisterBinding;

import java.util.Objects;

public class Register_activity extends AppCompatActivity {
    private ActivityRegisterBinding binding;
    private DataBase database;
    private Dialog dialog;
    private ImageControl imageControl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        imageControl=new ImageControl(this);
        setSupportActionBar(binding.registerToolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setElevation(10);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        dialog=new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.loading_dialog);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);


        binding.registerBtn.setOnClickListener(l->{
            String userName= Objects.requireNonNull(binding.registerUsernameTextfield.getText()).toString();
            String email= Objects.requireNonNull(binding.registerEmailTextfield.getText()).toString();
            String password= Objects.requireNonNull(binding.registerPasswordTextfield.getText()).toString();
            String phone= Objects.requireNonNull(binding.registerPhoneTextfield.getText()).toString();
            if( userName.isEmpty() || email.isEmpty() || password.isEmpty() || phone.isEmpty()){
                Toast.makeText(getApplicationContext(), "Please Fill all fields", Toast.LENGTH_SHORT).show();
            }else {
                dialog.show();
                createUser(email,password,userName,phone);
            }
        });
    }

    private void createUser(String email, String password, String userName, String phone) {

        imageControl.uploadImage(u->{
            database.createUserWithEmailAndPassword(u,email, password, userName, phone,"Admin", s -> {
                Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
                dialog.dismiss();
                if(s.equals("Created Successfully")) {
                    finish();
                }
            });
        });

    }
     ActivityResultLauncher<Intent> arl = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getData() != null && result.getResultCode() == RESULT_OK) {
            imageControl.setUri(result.getData().getData());
            binding.registerImg.setImageURI(imageControl.getImage());
        }
    });


    @Override
    protected void onResume() {
        super.onResume();
        database=DataBase.getInstance();


        binding.registerImg.setOnClickListener(l->{
            arl.launch(imageControl.openImage());
        });

        binding.registerImg.setOnLongClickListener(l->{
            binding.registerImg.setImageResource(R.drawable.camera);
            imageControl.setUri(Uri.parse(""));
            return true;
        });
    }
    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        imageControl=null;
        database.finish();
        database=null;
    }
}