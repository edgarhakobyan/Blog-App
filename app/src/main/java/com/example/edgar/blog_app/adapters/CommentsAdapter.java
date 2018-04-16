package com.example.edgar.blog_app.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.edgar.blog_app.constants.Constants;
import com.example.edgar.blog_app.models.Comment;
import com.example.edgar.blog_app.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by edgar on 3/6/18.
 */

public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.CommentViewHolder> {

    private ArrayList<Comment> commentsList;
    private Context mContext;

    private FirebaseFirestore mFirebaseFirestore;

    public CommentsAdapter(ArrayList<Comment> commentsList) {
        this.commentsList = commentsList;
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_item, parent, false);
        mContext = parent.getContext();
        mFirebaseFirestore = FirebaseFirestore.getInstance();
        return new CommentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final CommentsAdapter.CommentViewHolder holder, int position) {
        holder.setIsRecyclable(false);

        // Get user name and image
        String userId = commentsList.get(position).getUserId();
        mFirebaseFirestore.collection(Constants.USERS).document(userId).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    String userName = task.getResult().getString(Constants.NAME);
                    String userImage = task.getResult().getString(Constants.IMAGE);
                    holder.setAuthorName(userName);
                    holder.setUserImage(userImage);
                }
            }
        });

        //Get message
        String message = commentsList.get(position).getMessage();
        holder.setCommentMessage(message);
    }

    @Override
    public int getItemCount() {
        if (commentsList != null) {
            return commentsList.size();
        }
        return 0;
    }

    public class CommentViewHolder extends RecyclerView.ViewHolder {
        private View mView;

        private CircleImageView authorImageView;
        private TextView authorView;
        private TextView messageView;

        CommentViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }

        @SuppressLint("CheckResult")
        public void setUserImage(String imagePath) {
            authorImageView = mView.findViewById(R.id.comment_image);
            RequestOptions placeholderOption = new RequestOptions();
            placeholderOption.placeholder(R.drawable.profile_placeholder);
            Glide.with(mContext).applyDefaultRequestOptions(placeholderOption).load(imagePath).into(authorImageView);
        }

        public void setAuthorName(String name) {
            authorView = mView.findViewById(R.id.comment_username);
            authorView.setText(name);
        }

        void setCommentMessage(String message) {
            messageView = mView.findViewById(R.id.comment_message);
            messageView.setText(message);
        }


    }
}
