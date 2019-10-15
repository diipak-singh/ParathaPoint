package com.tecnosols.parathapoint;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class SignupActivity extends AppCompatActivity {
    private TextInputEditText name, mobile, email, city, address;
    private Button saveProfile;
    private ImageView userPic;

    private static final int CHOOSE_IMAGE = 1;
    private Uri imageUrl;

    private StorageReference mStorageRef;
    private DatabaseReference mDatabaseRef;
    private StorageTask mUploadTask;
    String userPhone, ImageUrl = null;
    String name_user;
    FirebaseUser user;
    private ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        initViews();
        progressBar.setVisibility(View.GONE);
        Picasso.get().load(R.drawable.chose_pic).transform(new CircleTransform()).into(userPic);


        user = FirebaseAuth.getInstance().getCurrentUser();
        name_user = user.getEmail();
        email.setText(user.getEmail());
        name.setText(user.getDisplayName());

        mDatabaseRef = FirebaseDatabase.getInstance().getReference().child("user_details");
        mStorageRef = FirebaseStorage.getInstance().getReference("profile_pics");


        userPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFileChoose();
            }
        });

        saveProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                closekeyboard();
                progressBar.setVisibility(View.VISIBLE);

                saveImage();

            }
        });
    }

    private void initViews() {
        name = (TextInputEditText) findViewById(R.id.signup_name);
        mobile = (TextInputEditText) findViewById(R.id.signup_phone);
        email = (TextInputEditText) findViewById(R.id.signup_email);
        city = (TextInputEditText) findViewById(R.id.signup_city);
        address = (TextInputEditText) findViewById(R.id.signup_permanent_address);
        saveProfile = (Button) findViewById(R.id.button_update);
        userPic = (ImageView) findViewById(R.id.signup_pic);
        progressBar=(ProgressBar)findViewById(R.id.progressBar2);
    }

    private void showFileChoose() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, CHOOSE_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CHOOSE_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUrl = data.getData();

            Picasso.get().load(imageUrl).transform(new CircleTransform()).into(userPic);

        }
    }

    private void saveImage() {

        if (imageUrl != null) {

            final StorageReference fileRefrence = mStorageRef.child(name_user + "." + getFileExtension(imageUrl));

            mUploadTask = fileRefrence.putFile(imageUrl)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    // uploadProgress.setProgress(0);
                                }
                            }, 500);

                            fileRefrence.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    ImageUrl = uri.toString();
                                    uploadData();
                                }
                            });


                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                            // uploadProgress.setProgress((int) progress);
                        }
                    });

        } else {
            imageUrl=user.getPhotoUrl();

           // Toast.makeText(getApplicationContext(), "Please choose an image to continue.", Toast.LENGTH_SHORT).show();
        }

    }

    private void uploadData() {



        String username = name.getText().toString().trim();
        String userPhone = mobile.getText().toString().trim();
        String userEmail = email.getText().toString().trim();
        String usercity = city.getText().toString().trim();
        String userAddress = address.getText().toString().trim();

        if (username.isEmpty()) {
            progressBar.setVisibility(View.GONE);
            name.setError("Name can't be Empty");
            name.requestFocus();

            return;
        }
        if (userAddress.isEmpty()) {
            progressBar.setVisibility(View.GONE);
            address.setError("Address can't be Empty");
            address.requestFocus();

            return;
        }
        if (userPhone.isEmpty()) {
            progressBar.setVisibility(View.GONE);
            mobile.setError("Address can't be Empty");
            mobile.requestFocus();

            return;
        }



        String user_id = user.getUid();
        // Log.i("userId",user_id);

        String id = userPhone;

        user_details udt = new user_details(ImageUrl, username, userPhone, userEmail, usercity, userAddress, user_id);
        mDatabaseRef.child(id).setValue(udt).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isComplete()) {
                    //saveUserDetails();
                    //uploadProgress.setVisibility(View.INVISIBLE);
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(), "All Data Saved Successfully", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(SignupActivity.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);

                }
            }
        });


    }

    private String getFileExtension(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private void saveUserDetails() {
       /* DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("AlluserDetailsNew");
        // String phoneNumber = user.getPhoneNumber();
        String iid = user.getUid();
        String userid = user.getUid();

        registered_users_details rud = new registered_users_details(userid, mobile.getText().toString().trim());
        databaseReference.child(iid).setValue(rud).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isComplete()){
                    Toast.makeText(getApplicationContext(),"User Data saved successfully",Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });*/

    }

    private void closekeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

        }
    }
}
