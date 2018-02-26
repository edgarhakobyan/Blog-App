package com.example.edgar.blog_app;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by edgar on 2/26/18.
 */

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.CardViewHolder> {

    private Context context;
    private ArrayList<Card> cards;

    public CardAdapter(Context context, ArrayList<Card> cards) {
        this.context = context;
        this.cards = cards;
    }

    @Override
    public CardAdapter.CardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CardViewHolder(LayoutInflater.from(parent.getContext()), parent);
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void onBindViewHolder(CardAdapter.CardViewHolder holder, int position) {
        holder.title.setText(cards.get(position).getTitle());
        holder.description.setText(cards.get(position).getDescription());
        holder.image.setImageResource(cards.get(position).getImage());
        //holder.comments.setText(cards.get(position).getComment());
        holder.likes.setText(String.format("%d", cards.get(position).getLikes()));
    }

    @Override
    public int getItemCount() {
        return cards.size();
    }

    public class CardViewHolder extends RecyclerView.ViewHolder {

        private TextView title;
        private TextView description;
        private ImageView image;
        //private TextView comments;
        private TextView likes;

        CardViewHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.card_item, parent, false));

            title = itemView.findViewById(R.id.card_title);
            description = itemView.findViewById(R.id.card_description);
            image = itemView.findViewById(R.id.card_image);
            //comments = itemView.findViewById(R.id.card_comment);
            likes = itemView.findViewById(R.id.card_likes);
//            viewBackground = itemView.findViewById(R.id.view_background);
//            viewForeground = itemView.findViewById(R.id.view_foreground);
//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    Intent intent = new Intent(context, ScrollingActivity.class);
//                    intent.putExtra(Constants.DISH_ID_KEY, dishes.get(getAdapterPosition()).getId());
//                    context.startActivity(intent);
//                }
//            });
//            userName.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    Intent intent = new Intent(context, ScrollingActivity.class);
//                    intent.putExtra(Constants.USER_ID_KEY, dishes.get(getAdapterPosition()).getOwner());
//                    context.startActivity(intent);
//                }
//            });
        }
    }
}
