package it.unimib.pickapp.ui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import it.unimib.pickapp.R;

/**
 * It contains the page to edit the user profile.
 */
public class editProfileActivity extends AppCompatActivity {

    private static String TAG = "editProfileActivity";
    private Button buttonDone;
    private TextView name, surname, bio, changeImage;
    private ImageView imageProfile;

    private FirebaseUser user;
    private DatabaseReference reference;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageRef;
    private String userID;
    private Uri selectedImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        buttonDone = this.findViewById(R.id.EditProfileDone);
        name = this.findViewById(R.id.editProfileName);
        surname = this.findViewById(R.id.editProfileSurname);
        bio = this.findViewById(R.id.editProfileBio);
        imageProfile = this.findViewById(R.id.image_profile);
        changeImage = this.findViewById(R.id.editProfileChangePhoto);

        //firebase
        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        firebaseStorage = FirebaseStorage.getInstance();
        storageRef = firebaseStorage.getReference();

        userID = user.getUid();

        Log.d(TAG, userID);

        displayUserInfo(this);

        changeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //call an intent to the Media (photos on phone)
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 3);
            }
        });

        buttonDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveChanges();
                openPickappActivity();
            }
        });

    }

    private void openPickappActivity() {
        Intent intent = new Intent(this, pickappActivity.class);
        startActivity(intent);
    }

    private void displayUserInfo(Context context){
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                name.setText(snapshot.child(userID).child("name").getValue(String.class));
                surname.setText(snapshot.child(userID).child("surname").getValue(String.class));
                bio.setText(snapshot.child(userID).child("bio").getValue(String.class));
                Glide.with(context)
                        .load(snapshot.child(userID).child("imageurl").getValue(String.class))
                        .circleCrop()
                        .placeholder(R.drawable.ic_baseline_person_24)
                        .into(imageProfile);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void saveChanges(){
        String modname = name.getText().toString();
        String modsurname = surname.getText().toString();
        String modfullname = modname + " " + modsurname;
        String modbio = bio.getText().toString();

        Log.d(TAG, modfullname);

        //create hashmap with name of child as key and value to update as value
        Map<String,Object> update = new HashMap<String, Object>();
        update.put("name", modname);
        update.put("surname", modsurname);
        update.put("bio", modbio);
        update.put("fullname", modfullname);
        if(selectedImage != null) {
            update.put("imageurl", selectedImage.toString());
            savePicture();
        }

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //update the realtime database passing the hashmap previously created
                reference.child(userID).updateChildren(update);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,  @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //load the new image selected from gallery to the imageView
        if(resultCode == RESULT_OK && data != null && data.getData() != null){
            selectedImage = data.getData();
            Log.d(TAG, selectedImage.toString());
            imageProfile.setImageURI(selectedImage);
        }
    }

    private void savePicture(){
        //save picture in firebase storage with a random key associated
        final String randomKey = UUID.randomUUID().toString();
        StorageReference riversRef = storageRef.child("image/" + randomKey);
        riversRef.putFile(selectedImage)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                        Toast.makeText(editProfileActivity.this, "Image Uploaded.", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "Failed to upload", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}