package com.example.edgar.blog_app.adapters;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.edgar.blog_app.Constants;
import com.example.edgar.blog_app.models.Post;
import com.example.edgar.blog_app.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by edgar on 2/26/18.
 */

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {

    private ArrayList<Post> posts;

    private Context mContext;

    private FirebaseFirestore mFirebaseFirestore;

    public PostAdapter(ArrayList<Post> posts) {
        this.posts = posts;
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_item, parent, false);
        mContext = parent.getContext();
        mFirebaseFirestore = FirebaseFirestore.getInstance();
        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final PostViewHolder holder, int position) {

        String desc = posts.get(position).getDescription();
        holder.setDescriptionText(desc);

        String imageUrl = posts.get(position).getImageUrl();
        holder.setPostImage(imageUrl);

        String userId = posts.get(position).getUserId();
        mFirebaseFirestore.collection(Constants.USERS).document(userId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    String userName = task.getResult().getString(Constants.NAME);
                    String userImage = task.getResult().getString(Constants.IMAGE);
                    holder.setUserName(userName);
                    holder.setUserImage(userImage);
                }
            }
        });

        long milliseconds = posts.get(position).getTimestamp().getTime();
        String dateString = DateFormat.format(Constants.DATE_FORMAT, new Date(milliseconds)).toString();
        holder.setDate(dateString);

    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    public class PostViewHolder extends RecyclerView.ViewHolder {

        private View mView;

        private TextView postDescriptionView;
        private ImageView postImageView;
        private TextView postDateView;
        private CircleImageView postUserImageView;
        private TextView postUserNameView;

        public PostViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }

        public void setDescriptionText(String desc) {
            postDescriptionView = mView.findViewById(R.id.post_description);
            postDescriptionView.setText(desc);
        }

        public void setPostImage(String downloadUrl) {
            postImageView = mView.findViewById(R.id.post_image);
            RequestOptions placeholderOption = new RequestOptions();
            placeholderOption.placeholder(R.drawable.post_placeholder);
            Glide.with(mContext).applyDefaultRequestOptions(placeholderOption).load(downloadUrl).into(postImageView);
        }

        public void setDate(String date) {
            postDateView = mView.findViewById(R.id.post_date_time);
            postDateView.setText(date);
        }

        public void setUserName(String name) {
            postUserNameView = mView.findViewById(R.id.post_user_name);
            postUserNameView.setText(name);
        }

        public void setUserImage(String imagePath) {
            postUserImageView = mView.findViewById(R.id.post_user_image);
            RequestOptions placeholderOption = new RequestOptions();
            placeholderOption.placeholder(R.drawable.profile_placeholder);
            Glide.with(mContext).applyDefaultRequestOptions(placeholderOption).load(imagePath).into(postUserImageView);
        }
    }

//    private Context context;
//    private ArrayList<Post> posts;
//
//    public PostAdapter(Context context, ArrayList<Post> posts) {
//        this.context = context;
//        this.posts = posts;
//    }
//
//    @Override
//    public PostViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        return new PostViewHolder(LayoutInflater.from(parent.getContext()), parent);
//    }
//
//    @SuppressLint("DefaultLocale")
//    @Override
//    public void onBindViewHolder(PostViewHolder holder, @SuppressLint("RecyclerView") final int position) {
//        holder.title.setText(posts.get(position).getTitle());
//        holder.title.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(context, UserActivity.class);
//                context.startActivity(intent);
//            }
//        });
//        holder.description.setText(posts.get(position).getDescription());
//        holder.image.setImageResource(posts.get(position).getImage());
//        holder.likesCount.setText(String.format("%d", posts.get(position).getLikes()));
//        holder.comments.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(context, CommentsActivity.class);
//                intent.putExtra("position", position);
//                context.startActivity(intent);
//            }
//        });
//    }
//
//    @Override
//    public int getItemCount() {
//        return posts.size();
//    }
//
//    public class PostViewHolder extends RecyclerView.ViewHolder {
//
//        private TextView title;
//        private TextView description;
//        private ImageView image;
//        private TextView comments;
//        private TextView likesCount;
//        private TextView like;
//
//        PostViewHolder(LayoutInflater inflater, ViewGroup parent) {
//            super(inflater.inflate(R.layout.post_item, parent, false));
//
//            title = itemView.findViewById(R.id.post_title);
//            description = itemView.findViewById(R.id.post_description);
//            image = itemView.findViewById(R.id.post_image);
//            comments = itemView.findViewById(R.id.post_all_comments);
//            likesCount = itemView.findViewById(R.id.post_likes_count);
//            like = itemView.findViewById(R.id.post_like);
////            viewBackground = itemView.findViewById(R.id.view_background);
////            viewForeground = itemView.findViewById(R.id.view_foreground);
////            itemView.setOnClickListener(new View.OnClickListener() {
////                @Override
////                public void onClick(View view) {
////                    Intent intent = new Intent(context, ScrollingActivity.class);
////                    context.startActivity(intent);
////                }
////            });
////            userName.setOnClickListener(new View.OnClickListener() {
////                @Override
////                public void onClick(View view) {
////                    Intent intent = new Intent(context, ScrollingActivity.class);
////                    context.startActivity(intent);
////                }
////            });
//        }
//    }
}
