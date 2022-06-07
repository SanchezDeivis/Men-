package com.example.mi_menu.ui.menu.products.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mi_menu.R;
import com.example.mi_menu.data.db.model.ResourcePosts;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.MyViewHolder> {

    public interface ItemClickListenerPosts {
        void onCLick(View view, int position);
    }

    private List<ResourcePosts> posts;

    private Context ctx;
    private ItemClickListenerPosts listenerPosts;

    public PostsAdapter(List<ResourcePosts> resourcePosts, Context ctx,
                        @NonNull ItemClickListenerPosts listenerPosts) {
        this.posts = resourcePosts;
        this.ctx = ctx;
        this.listenerPosts = listenerPosts;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_posts, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        ResourcePosts resourcePosts = posts.get(position);

        if (resourcePosts != null) {
            Picasso.get()
                    .load(resourcePosts.getImage())
                    /*.resize(200, 200)*/
                    /*.centerCrop()*/
                    .error(ctx.getResources().getDrawable(R.drawable.avatar_menu))
                    .memoryPolicy(MemoryPolicy.NO_CACHE).networkPolicy(NetworkPolicy.NO_CACHE)
                    .into(holder.avatar);
        } else {
            holder.avatar.setImageDrawable(ctx.getResources().getDrawable
                    (R.drawable.avatar_menu));
        }

        /*if (resourcePosts != null) {
            holder.institutionName.setText(resourcePosts.getName());
        }*/

    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.civ_post)
        CircleImageView avatar;

        View itemview;

        public MyViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, itemView);
            this.itemview = view;

            itemView.setOnClickListener(this);

            /*itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listenerInstBestQualified.onCLick(view, getAdapterPosition());
                }
            });*/

        }

        @Override
        public void onClick(View view) {
            listenerPosts.onCLick(view, getBindingAdapterPosition());
        }
    }

}
