package com.example.edgar.blog_app.adapters;


import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.edgar.blog_app.models.Post;
import com.example.edgar.blog_app.R;

import java.util.ArrayList;

/**
 * Created by edgar on 2/26/18.
 */

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {

    private ArrayList<Post> posts;

    public PostAdapter(ArrayList<Post> posts) {
        this.posts = posts;
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_item, parent, false);
        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {

        String desc = posts.get(position).getDescription();
        holder.setDescriptionText(desc);
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    public class PostViewHolder extends RecyclerView.ViewHolder {

        private View mView;

        private TextView descriptionView;

        public PostViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }

        public void setDescriptionText(String desc) {
            descriptionView = mView.findViewById(R.id.post_description);
            descriptionView.setText(desc);
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
////                    intent.putExtra(Constants.DISH_ID_KEY, dishes.get(getAdapterPosition()).getId());
////                    context.startActivity(intent);
////                }
////            });
////            userName.setOnClickListener(new View.OnClickListener() {
////                @Override
////                public void onClick(View view) {
////                    Intent intent = new Intent(context, ScrollingActivity.class);
////                    intent.putExtra(Constants.USER_ID_KEY, dishes.get(getAdapterPosition()).getOwner());
////                    context.startActivity(intent);
////                }
////            });
//        }
//    }
}
