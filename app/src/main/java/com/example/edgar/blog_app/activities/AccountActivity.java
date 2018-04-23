package com.example.edgar.blog_app.activities;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.edgar.blog_app.R;
import com.example.edgar.blog_app.adapters.PostAdapter;
import com.example.edgar.blog_app.constants.Constants;
import com.example.edgar.blog_app.models.Post;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class AccountActivity extends AppCompatActivity {

    private static ArrayList<Post> mPosts;
    private PostAdapter mPostAdapter;
    private RecyclerView mPostListView;

    private FirebaseAuth mAuth;
    private FirebaseFirestore mFirebaseFirestore;

    private CircleImageView accountImage;
    private TextView accountName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        mAuth = FirebaseAuth.getInstance();
        mFirebaseFirestore = FirebaseFirestore.getInstance();

        mPosts = new ArrayList<>();
        mPostAdapter = new PostAdapter(mPosts);

        mPostListView = findViewById(R.id.account_posts);
        mPostListView.setLayoutManager(new LinearLayoutManager(this));
        mPostListView.setAdapter(mPostAdapter);

        if (mAuth.getCurrentUser() != null) {

            String currentUserId = mAuth.getCurrentUser().getUid();

            //Get user name and image
            mFirebaseFirestore.collection(Constants.USERS).document(currentUserId).get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                String userName = task.getResult().getString(Constants.NAME);
                                String userImage = task.getResult().getString(Constants.IMAGE);
                                setUserName(userName);
                                setUserImage(userImage);
                            }
                        }
                    });


            // Get posts by order timestamp and user
            Query query = mFirebaseFirestore.collection(Constants.POSTS)
                    .orderBy(Constants.TIMESTAMP, Query.Direction.DESCENDING)
                    .whereEqualTo(Constants.USER_ID, currentUserId);

            query.addSnapshotListener(this, new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(QuerySnapshot queryDocumentSnapshots, FirebaseFirestoreException e) {

                    if (queryDocumentSnapshots != null && !queryDocumentSnapshots.isEmpty()) {
                        for (DocumentChange doc : queryDocumentSnapshots.getDocumentChanges()) {
                            if (doc.getType() == DocumentChange.Type.ADDED) {

                                String postId = doc.getDocument().getId();
                                Post post = doc.getDocument().toObject(Post.class).withId(postId);

                                mPosts.add(post);

                                mPostAdapter.notifyDataSetChanged();

                            }
                        }
                    }

                }
            });
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void setUserName(String name) {
        accountName = findViewById(R.id.account_name);
        accountName.setText(name);
    }

    @SuppressLint("CheckResult")
    public void setUserImage(String imagePath) {
        accountImage = findViewById(R.id.account_image);

        RequestOptions placeholderOption = new RequestOptions();
        placeholderOption.placeholder(R.drawable.profile_placeholder);

        Glide.with(this).applyDefaultRequestOptions(placeholderOption).load(imagePath).into(accountImage);
    }

}
