package com.example.edgar.blog_app.activities;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.edgar.blog_app.MainActivity;
import com.example.edgar.blog_app.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.HashMap;
import java.util.Map;

public class PostActivity extends AppCompatActivity {

    private ImageView postImage;
    private EditText postDescription;
    private Button addPostBtn;
    private ProgressBar postProgress;

    private Uri postImageUri;
    private String currentUserId;

    private StorageReference storageReference;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        postImage = (ImageView) findViewById(R.id.post_image);
        postDescription = (EditText) findViewById(R.id.post_description);
        addPostBtn = (Button) findViewById(R.id.add_post_btn);
        postProgress = (ProgressBar) findViewById(R.id.piost_progressbar);

        // Get Firebase data
        storageReference = FirebaseStorage.getInstance().getReference();
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        currentUserId = firebaseAuth.getCurrentUser().getUid();

        postImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setMinCropResultSize(512,512)
                        .setAspectRatio(1,1)
                        .start(PostActivity.this);
            }
        });

        addPostBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String desc = postDescription.getText().toString();
                if (!TextUtils.isEmpty(desc) && postImageUri != null) {
                    postProgress.setVisibility(View.VISIBLE);
                    String randomName = FieldValue.serverTimestamp().toString();
                    StorageReference filePath = storageReference.child("postImages").child(randomName + ".jpg");
                    filePath.putFile(postImageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            if (task.isSuccessful()) {
                                String downloadUri = task.getResult().getDownloadUrl().toString();

                                Map<String, Object> postMap = new HashMap<>();
                                postMap.put("imageUrl", downloadUri);
                                postMap.put("desc", desc);
                                postMap.put("userId", currentUserId);
                                postMap.put("timestamp", FieldValue.serverTimestamp());

                                firebaseFirestore.collection("Posts").add(postMap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentReference> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(PostActivity.this, "Post is added ", Toast.LENGTH_LONG).show();
                                            Intent intent = new Intent(PostActivity.this, MainActivity.class);
                                            startActivity(intent);
                                            finish();
                                        } else {

                                        }
                                        postProgress.setVisibility(View.INVISIBLE);
                                    }
                                });
                            } else {
                                postProgress.setVisibility(View.INVISIBLE);
                            }
                        }
                    });
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                postImageUri = result.getUri();
                postImage.setImageURI(postImageUri);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }
}
