package com.example.insulife;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;

import java.util.Objects;

/**
 *
 */
public class ImageControl {

    //    private ActivityResultLauncher<Intent> arl;
    protected   AppCompatActivity activity;
    protected  StorageReference storageReference;
    protected StorageTask storageTask;
     Uri uri;

    protected DataBase dataBase;

    public interface ReturnUrl {
        void getUrl(String u);

    }
    public ImageControl(AppCompatActivity activity) {
        this.activity = activity;
        storageReference = FirebaseStorage.getInstance().getReference("uploads");
        dataBase = DataBase.getInstance();
        uri=Uri.parse("");
    }

    public Intent openImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        return intent;
    }

    public void setUri(Uri uri) {
        this.uri = uri;
    }


    public void uploadImage(ReturnUrl ru) {
        if(Objects.equals(uri.getPath(), "")){
            ru.getUrl("");}
        else if (uri != null && (!Objects.equals(uri.getPath(), "")) ) {
            StorageReference fileReference = storageReference.child(System.currentTimeMillis() + "." + getFileExtension(uri));
            storageTask = fileReference.putFile(uri).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    fileReference.getDownloadUrl().addOnCompleteListener(task1 -> {
                        String imageUrl = task1.getResult().toString();
                        ru.getUrl(imageUrl+"");
                    });

                } else {
                    Toast.makeText(activity.getApplicationContext(), "Failed", Toast.LENGTH_SHORT).show();
                }
            });
        }else
            Toast.makeText(activity.getApplicationContext(), "No image Selected", Toast.LENGTH_SHORT).show();

    }

    private String getFileExtension(Uri imageUri) {
        ContentResolver contentResolver = activity.getApplicationContext().getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(imageUri));
    }

    public Uri getImage() {
        return uri;
    }
}
