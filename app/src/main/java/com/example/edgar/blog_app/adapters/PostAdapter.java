package com.example.edgar.blog_app.adapters;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.edgar.blog_app.activities.PostMoreInfoActivity;
import com.example.edgar.blog_app.constants.Constants;
import com.example.edgar.blog_app.activities.CommentsActivity;
import com.example.edgar.blog_app.models.Post;
import com.example.edgar.blog_app.R;
import com.example.edgar.blog_app.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by edgar on 2/26/18.
 */

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {

    private List<Post> postList;
    private List<User> userList;

    private Context mContext;

    private FirebaseFirestore mFirebaseFirestore;
    private FirebaseAuth mFirebaseAuth;

    public PostAdapter(List<Post> postList, List<User> userList) {
        this.postList = postList;
        this.userList = userList;
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_item, parent, false);
        mContext = parent.getContext();
        mFirebaseFirestore = FirebaseFirestore.getInstance();
        mFirebaseAuth = FirebaseAuth.getInstance();
        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final PostViewHolder holder, final int position) {

        holder.setIsRecyclable(false);

        final String postId = postList.get(position).PostId;
        final String currentUserId = Objects.requireNonNull(mFirebaseAuth.getCurrentUser()).getUid();

        final String desc = postList.get(position).getDescription();
        holder.setDescriptionText(desc);

        final String imageUrl = postList.get(position).getImageUrl();
        String thumbUri = postList.get(position).getImageThumb();
        holder.setPostImage(imageUrl, thumbUri);

        String postUserId = postList.get(position).getUserId();

        if (postUserId.equals(currentUserId)) {
            holder.deletePostBtn.setVisibility(View.VISIBLE);
            holder.deletePostBtn.setEnabled(true);
        }

        final String likesUrl = String.format("%s/%s/%s", Constants.POSTS, postId, Constants.LIKES);
        final String commentsUrl = String.format("%s/%s/%s", Constants.POSTS, postId, Constants.COMMENTS);

        String userName = userList.get(position).getName();
        String userImage = userList.get(position).getImage();
        holder.setUserName(userName);
        holder.setUserImage(userImage);

        try {
            long milliseconds = postList.get(position).getTimestamp().getTime();
            String dateString = DateFormat.format(Constants.DATE_FORMAT, new Date(milliseconds)).toString();
            holder.setDate(dateString);
        } catch (Exception e) {
            Toast.makeText(mContext, "Exception : " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        // Get Likes count
        mFirebaseFirestore.collection(likesUrl).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot queryDocumentSnapshots, FirebaseFirestoreException e) {
                if (null != queryDocumentSnapshots) {
                    if (queryDocumentSnapshots.isEmpty()) {
                        holder.updatePostLikesCount(0);
                    } else {
                        int count = queryDocumentSnapshots.size();
                        holder.updatePostLikesCount(count);
                    }
                }
            }
        });

        //Get Likes
        mFirebaseFirestore.collection(likesUrl).document(currentUserId)
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(DocumentSnapshot documentSnapshot, FirebaseFirestoreException e) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && documentSnapshot != null) {
                    if (documentSnapshot.exists()) {
                        holder.postLikeImage.setImageDrawable(mContext.getDrawable(R.mipmap.ic_like_accent));
                    } else {
                        holder.postLikeImage.setImageDrawable(mContext.getDrawable(R.mipmap.ic_like_gray));
                    }
                }
            }
        });

        // Likes feature
        holder.postLikeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFirebaseFirestore.collection(likesUrl).document(currentUserId).get()
                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                        if (!task.getResult().exists()) {
                            Map<String, Object> likesMap = new HashMap<>();
                            likesMap.put(Constants.TIMESTAMP, FieldValue.serverTimestamp());

                            mFirebaseFirestore.collection(likesUrl).document(currentUserId).set(likesMap);
                        } else {
                            mFirebaseFirestore.collection(likesUrl).document(currentUserId).delete();
                        }

                    }
                });
            }
        });

        // Get Comments count
        mFirebaseFirestore.collection(commentsUrl).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot queryDocumentSnapshots, FirebaseFirestoreException e) {
                if (null != queryDocumentSnapshots) {
                    if (queryDocumentSnapshots.isEmpty()) {
                        holder.updatePostCommentsCount(0);
                    } else {
                        int count = queryDocumentSnapshots.size();
                        holder.updatePostCommentsCount(count);
                    }
                }
            }
        });

        //Comments feature
        holder.postCommentsImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, CommentsActivity.class);
                intent.putExtra(Constants.POST_ID, postId);
                mContext.startActivity(intent);
            }
        });

        //Delete post
        holder.deletePostBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFirebaseFirestore.collection(Constants.POSTS).document(postId).delete()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        postList.remove(position);
                        userList.remove(position);
                        notifyDataSetChanged();
                    }
                });
            }
        });

        //Open Post More Info
        holder.postDescriptionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPostMoreInfoPage(imageUrl, desc);
            }
        });

        //Open Post More Info
        holder.postImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPostMoreInfoPage(imageUrl, desc);
            }
        });

    }

    private void openPostMoreInfoPage(String imageUrl, String desc) {
        Intent intent = new Intent(mContext, PostMoreInfoActivity.class);
        intent.putExtra(Constants.IMAGE_URL, imageUrl);
        intent.putExtra(Constants.DESC, desc);
        mContext.startActivity(intent);
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    public class PostViewHolder extends RecyclerView.ViewHolder {

        private View mView;

        private TextView postDescriptionView;
        private TextView postDateView;
        private TextView postUserNameView;
        private TextView postLikeCount;
        private TextView postCommentsCount;

        private ImageView postImageView;
        private ImageView postLikeImage;
        private ImageView postCommentsImageView;

        private CircleImageView postUserImageView;

        private Button deletePostBtn;

        PostViewHolder(View itemView) {
            super(itemView);
            mView = itemView;

            postLikeImage = mView.findViewById(R.id.post_like_image);
            postCommentsImageView = mView.findViewById(R.id.post_comments_btn);
            deletePostBtn = mView.findViewById(R.id.delete_post_btn);
        }

        void setDescriptionText(String desc) {
            postDescriptionView = mView.findViewById(R.id.post_description);
            postDescriptionView.setText(desc);
        }

        @SuppressLint("CheckResult")
        void setPostImage(String downloadUrl, String thumbUri) {
            postImageView = mView.findViewById(R.id.post_image);

            RequestOptions placeholderOption = new RequestOptions();
            placeholderOption.placeholder(R.drawable.post_placeholder);

            Glide.with(mContext).applyDefaultRequestOptions(placeholderOption).load(downloadUrl).thumbnail(
                    Glide.with(mContext).load(thumbUri)
            ).into(postImageView);
        }

        public void setDate(String date) {
            postDateView = mView.findViewById(R.id.post_date_time);
            postDateView.setText(date);
        }

        public void setUserName(String name) {
            postUserNameView = mView.findViewById(R.id.post_user_name);
            postUserNameView.setText(name);
        }

        @SuppressLint("CheckResult")
        public void setUserImage(String imagePath) {
            postUserImageView = mView.findViewById(R.id.post_user_image);

            RequestOptions placeholderOption = new RequestOptions();
            placeholderOption.placeholder(R.drawable.profile_placeholder);

            Glide.with(mContext).applyDefaultRequestOptions(placeholderOption).load(imagePath).into(postUserImageView);
        }

        public void updatePostLikesCount(int count) {
            postLikeCount = mView.findViewById(R.id.post_like_count);
            postLikeCount.setText(String.format("%s Likes", count));
        }

        public void updatePostCommentsCount(int count) {
            postCommentsCount = mView.findViewById(R.id.post_comments_count);
            postCommentsCount.setText(String.format("%s Comments", count));
        }
    }

}
