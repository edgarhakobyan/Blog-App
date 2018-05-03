package com.example.edgar.blog_app.activities;

import android.annotation.SuppressLint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.edgar.blog_app.R;
import com.example.edgar.blog_app.constants.Constants;

public class PostMoreInfoActivity extends AppCompatActivity {

    @SuppressLint("CheckResult")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_more_info);

        ImageView postImage = findViewById(R.id.post_item_home_image);
        TextView description = findViewById(R.id.post_item_home_desc);

        String imageUrl = getIntent().getStringExtra(Constants.IMAGE_URL);
        String desc = getIntent().getStringExtra(Constants.DESC);

        RequestOptions placeholderRequest = new RequestOptions();
        placeholderRequest.placeholder(R.drawable.post_placeholder);
        Glide.with(this).setDefaultRequestOptions(placeholderRequest).load(imageUrl).into(postImage);

        description.setText(desc);
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