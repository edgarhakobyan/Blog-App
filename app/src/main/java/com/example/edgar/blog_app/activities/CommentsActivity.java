package com.example.edgar.blog_app.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.edgar.blog_app.models.Comment;
import com.example.edgar.blog_app.MainActivity;
import com.example.edgar.blog_app.R;
import com.example.edgar.blog_app.adapters.CommentAdapter;

import java.util.ArrayList;

public class CommentsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);

        Intent intent = getIntent();
        final int position = intent.getIntExtra("position", 0);
        ArrayList<Comment> mComments = MainActivity.mPosts.get(position).getComments();

//        Comment mComment1 = new Comment("Edgar", "this is a comment");
//        Comment mComment2 = new Comment("Edgar", "this is a comment");
//        Comment mComment3 = new Comment("Edgar", "this is a comment");
//
//        ArrayList<Comment> mComments = new ArrayList<>();
//        mComments.add(mComment1);
//        mComments.add(mComment2);
//        mComments.add(mComment3);

        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.commentsRecyclerView);

        CommentAdapter mCommentAdapter = new CommentAdapter(mComments);
        mRecyclerView.setAdapter(mCommentAdapter);

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        final EditText newComment = (EditText) findViewById(R.id.new_comment);
        ImageButton sendComment = (ImageButton) findViewById(R.id.send_comment);
        sendComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Comment mComment = new Comment("Edgar", newComment.getText().toString());
                MainActivity.mPosts.get(position).setComments(mComment);
                Intent i = new Intent(CommentsActivity.this, MainActivity.class);
                startActivity(i);
            }
        });
    }
}
