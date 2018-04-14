package com.example.edgar.blog_app.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.edgar.blog_app.R;

public class UserActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

//        String nameSurname = "Edgar Hakobyan";
//
//        TextView userTextView = (TextView) findViewById(R.id.name_surname);
//
//        userTextView.setText(nameSurname);
//
//
//        ArrayList<Post> mPosts = new ArrayList<>();
//
//        Comment mComment1 = new Comment("Edgar", "this is a comment 1");
//        Comment mComment2 = new Comment("Edgar", "this is a comment 2");
//
//        Post post1 = new Post("First Post", "This is first card", R.drawable.food);
//        post1.setComments(mComment1);
//        post1.setComments(mComment2);
//        Post post2 = new Post("Second Post", "This is second card", R.drawable.food);
//        post2.setComments(mComment1);
//        post2.setComments(mComment2);
//        Post post3 = new Post("Third Post", "This is third card", R.drawable.food);
//        post3.setComments(mComment1);
//        post3.setComments(mComment2);
//
//        mPosts.add(post1);
//        mPosts.add(post2);
//        mPosts.add(post3);
//
//        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.user_posts);
//        PostAdapter mPostAdapter = new PostAdapter(UserActivity.this, mPosts);
//        mRecyclerView.setAdapter(mPostAdapter);
//
//        mRecyclerView.setHasFixedSize(true);
//        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}
