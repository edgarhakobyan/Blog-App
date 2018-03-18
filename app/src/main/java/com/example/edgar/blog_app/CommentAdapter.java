package com.example.edgar.blog_app;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by edgar on 3/6/18.
 */

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {
    private ArrayList<Comment> comments;

    public CommentAdapter(ArrayList<Comment> comments) {
        this.comments = comments;
    }

    @Override
    public CommentAdapter.CommentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CommentAdapter.CommentViewHolder(LayoutInflater.from(parent.getContext()), parent);
    }

    @Override
    public void onBindViewHolder(CommentAdapter.CommentViewHolder holder, int position) {
        holder.author.setText(comments.get(position).getAuthor());
        holder.comment.setText(comments.get(position).getComment());
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    public class CommentViewHolder extends RecyclerView.ViewHolder {

        private TextView author;
        private TextView comment;

        CommentViewHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.comment_item, parent, false));

            author = itemView.findViewById(R.id.comment_author);
            comment = itemView.findViewById(R.id.comment);
        }
    }
}
