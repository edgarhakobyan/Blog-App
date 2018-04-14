package com.example.edgar.blog_app.activities;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.edgar.blog_app.constants.Constants;
import com.example.edgar.blog_app.R;
import com.example.edgar.blog_app.adapters.CommentsAdapter;
import com.example.edgar.blog_app.models.Comment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CommentsActivity extends AppCompatActivity {

    private EditText commentField;
    private ImageView postCommentBtn;
    private RecyclerView mCommentListView;

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;

    private String postId;
    private String currentUserId;

    private static ArrayList<Comment> mComments;
    private CommentsAdapter mCommentsAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);

        commentField = findViewById(R.id.comment_field);
        postCommentBtn = findViewById(R.id.send_comment_img);
        mCommentListView = findViewById(R.id.comments_list);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        currentUserId = firebaseAuth.getCurrentUser().getUid();

        mComments = new ArrayList<>();
        mCommentsAdapter = new CommentsAdapter(mComments);
        mCommentListView.setHasFixedSize(true);
        mCommentListView.setLayoutManager(new LinearLayoutManager(this));
        mCommentListView.setAdapter(mCommentsAdapter);

        postId = getIntent().getStringExtra(Constants.POST_ID);

        //RecyclerView Get Comments
        firebaseFirestore.collection(Constants.POSTS + "/" + postId + "/" + Constants.COMMENTS)
                .addSnapshotListener(this, new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(QuerySnapshot queryDocumentSnapshots, FirebaseFirestoreException e) {
                        if (!queryDocumentSnapshots.isEmpty()) {
                            for (DocumentChange doc: queryDocumentSnapshots.getDocumentChanges()) {
                                if (doc.getType() == DocumentChange.Type.ADDED) {
                                    String commentId = doc.getDocument().getId();
                                    Comment comment = doc.getDocument().toObject(Comment.class);
                                    mComments.add(comment);
                                    mCommentsAdapter.notifyDataSetChanged();
                                }
                            }
                        }
                    }
                });

        // Add new comment
        postCommentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String postMessage = commentField.getText().toString();
                if (!postMessage.isEmpty()) {

                    Map<String, Object> commentsMap = new HashMap<>();
                    commentsMap.put(Constants.MESSAGE, postMessage);
                    commentsMap.put(Constants.USER_ID, currentUserId);
                    commentsMap.put(Constants.TIMESTAMP, FieldValue.serverTimestamp());

                    firebaseFirestore.collection(Constants.POSTS + "/" + postId + "/" + Constants.COMMENTS).add(commentsMap)
                            .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentReference> task) {
                                    if (task.isSuccessful()) {
                                        commentField.setText("");
                                    } else {
                                        Toast.makeText(CommentsActivity.this, "Error while posting comments: ", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                }


            }
        });
    }

}
