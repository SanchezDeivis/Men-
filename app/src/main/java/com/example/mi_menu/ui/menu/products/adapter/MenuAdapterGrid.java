package com.example.mi_menu.ui.menu.products.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mi_menu.R;
import com.example.mi_menu.data.model.menu.Menu_Categoria;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MenuAdapterGrid extends RecyclerView.Adapter<MenuAdapterGrid.MyViewHolder> {

    public interface ItemClickListener {
        void onCLick(View view, int position);
    }

    private List<Menu_Categoria> menuData;


    private Context ctx;
    private ItemClickListener listener;

    public MenuAdapterGrid(List<Menu_Categoria> menuData, Context ctx,
                           @NonNull ItemClickListener listener) {
        this.menuData = menuData;
        this.ctx = ctx;
        this.listener = listener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_menu_data, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        Menu_Categoria data = menuData.get(position);

        if (data.getImage()!=null   ) {
            Picasso.get()
                    .load(data.getImage())
                    .error(ctx.getResources().getDrawable(R.drawable.avatar_menu))
                    .memoryPolicy(MemoryPolicy.NO_CACHE).networkPolicy(NetworkPolicy.NO_CACHE)
                    .into(holder.imageView);
        } else {
            holder.imageView.setImageDrawable(ctx.getResources().getDrawable
                    (R.drawable.avatar_menu));
        }

        if (data != null) {
            holder.title_name_menu.setText(data.getName());
        }

    }

    @Override
    public int getItemCount() {
        return menuData.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.tv_title_name_menu)
        TextView title_name_menu;

        @BindView(R.id.iv_image_view_menu) ImageView imageView;
        //@BindView(R.id.iv_image_view_menu) CircleImageView imageView;

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

            listener.onCLick(view, getBindingAdapterPosition());
        }
    }

}
