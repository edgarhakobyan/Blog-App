package com.example.edgar.blog_app.activities;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;

import com.example.edgar.blog_app.R;
import com.example.edgar.blog_app.adapters.PostAdapter;
import com.example.edgar.blog_app.constants.Constants;
import com.example.edgar.blog_app.models.Post;
import com.example.edgar.blog_app.models.User;
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
import java.util.List;

public class AccountActivity extends AppCompatActivity {

    private List<Post> postList;
    private List<User> userList;

    private PostAdapter mPostAdapter;
    private RecyclerView mPostListView;

    private FirebaseAuth mAuth;
    private FirebaseFirestore mFirebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        mAuth = FirebaseAuth.getInstance();
        mFirebaseFirestore = FirebaseFirestore.getInstance();

        postList = new ArrayList<>();
        userList = new ArrayList<>();
        mPostAdapter = new PostAdapter(postList, userList);

        mPostListView = findViewById(R.id.account_posts);
        mPostListView.setLayoutManager(new LinearLayoutManager(this));
        mPostListView.setAdapter(mPostAdapter);

        if (mAuth.getCurrentUser() != null) {

            String currentUserId = mAuth.getCurrentUser().getUid();

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
                                final Post post = doc.getDocument().toObject(Post.class).withId(postId);

                                String blogUserId = doc.getDocument().getString(Constants.USER_ID);
                                assert blogUserId != null;
                                mFirebaseFirestore.collection(Constants.USERS).document(blogUserId).get()
                                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                                                if (task.isSuccessful()) {
                                                    User user = task.getResult().toObject(User.class);

                                                    postList.add(post);
                                                    userList.add(user);

                                                    mPostAdapter.notifyDataSetChanged();
                                                }
                                            }
                                        });

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

}
