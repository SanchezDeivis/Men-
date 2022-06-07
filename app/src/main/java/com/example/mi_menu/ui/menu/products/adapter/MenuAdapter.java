package com.example.mi_menu.ui.menu.products.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mi_menu.R;
import com.example.mi_menu.data.db.model.ResourcePosts;
import com.example.mi_menu.data.model.menu.Menu_Categoria;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MenuAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<MenuAdapterPositionUtil> menuAdapterPositionUtils;

    private List<Menu_Categoria> menuInfo = new ArrayList<>();
    private MenuAdapterGrid menuAdapterGrid;

    private List<ResourcePosts> postArrayList = new ArrayList<>();
    private PostsAdapter postsAdapter;

    private int positionAdapter = 0;
    private Context ctx;
    private viewOnItemClickListener viewOnItemClickListener;

    private static final int ITEM_LOADING = 0;
    private static final int ITEM_MENU = 1;
    private static final int ITEM_INFO = 3;
    private boolean isLoadingAdded = false;

    public interface viewOnItemClickListener {

        void onCLick(View view, int position);

        void onClickViewMenuGrid(View view, Menu_Categoria itemMenuCategoria, int position);

        void onClickViewItemPosts(View view, ResourcePosts item, int position);

    }

    public MenuAdapter(Context ctx, @NonNull viewOnItemClickListener viewOnItemClickListener) {
        this.ctx = ctx;
        this.viewOnItemClickListener = viewOnItemClickListener;
        //this.postsInfos = new LinkedList<>();
        this.menuAdapterPositionUtils = new LinkedList<>();
    }

    public void setMenuInfo(List<Menu_Categoria> menuInfo) {
        this.menuInfo = menuInfo;
    }

    public void setPostAdapterPositionUtils(List<MenuAdapterPositionUtil> utilsList) {
        this.menuAdapterPositionUtils = utilsList;
    }


    @Override
    public void onViewAttachedToWindow(@NonNull RecyclerView.ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull RecyclerView.ViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public void onDetachedFromRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case ITEM_LOADING:
                View viewLoading = inflater.inflate(R.layout.item_progress, parent, false);
                viewHolder = new LoadingViewHolder(viewLoading);
                break;
            case ITEM_MENU:
                View viewItem = inflater.inflate(R.layout.item_menu_grid, parent, false);
                viewHolder = new MenuGridViewHolder(viewItem);
                break;
            case ITEM_INFO:
                View viewItemInstitutionsBestQualified = inflater.inflate(R.layout.posts_recommended, parent, false);
                viewHolder = new PostsViewHolder(viewItemInstitutionsBestQualified);
                break;

        }
        return viewHolder;


       /* View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_post_institution, parent, false);

      return new MyViewHolder(itemView); */
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        switch (getItemViewType(position)) {

            case ITEM_INFO:
                PostsViewHolder institutionsViewHolder = (PostsViewHolder) holder;
                institutionsViewHolder.recyclerView.setVisibility(View.VISIBLE);
                break;

            case ITEM_MENU:
                int positem;
                MenuGridViewHolder gridViewHolder = (MenuGridViewHolder) holder;
                gridViewHolder.recyclerView.setVisibility(View.VISIBLE);
                break;

            case ITEM_LOADING:
                LoadingViewHolder loadingViewHolder = (LoadingViewHolder) holder;
                loadingViewHolder.progressBar.setVisibility(View.VISIBLE);
                break;

        }
    }

    @Override
    public int getItemCount() {
        return menuAdapterPositionUtils == null ? 0 : menuAdapterPositionUtils.size();
    }

    @Override
    public int getItemViewType(int position) {

        if (position == 0 && menuAdapterPositionUtils.get(position).getResourcePosts() != null
                && postArrayList != null) {

            return ITEM_INFO;

        } else if (position == getItemCount() && isLoadingAdded) {

            return ITEM_LOADING;

        } else return ITEM_MENU;
    }


    public void addPositionAdapter(int numPosition) {
        positionAdapter += numPosition;
    }


    public void addInstitutionBestQualifie(List<ResourcePosts> ArrayList) {
        //addPositionAdapter(1);
        postArrayList.addAll(ArrayList);
        menuAdapterPositionUtils.add(new MenuAdapterPositionUtil(
                postArrayList, null, null));
        notifyItemInserted(menuAdapterPositionUtils.size());
    }

    public void addMenuList(List<Menu_Categoria> ArrayList) {
        //addPositionAdapter(1);
        menuInfo.addAll(ArrayList);
        menuAdapterPositionUtils.add(new MenuAdapterPositionUtil(
                null, menuInfo, null));
        notifyItemInserted(menuAdapterPositionUtils.size());
    }

    public void addAll(List<Menu_Categoria> postsInfos) {
        for (Menu_Categoria result : postsInfos) {
            add(result);
        }
    }

    public void add(Menu_Categoria menuCategoria) {
        if (menuCategoria != null) {
            addPositionAdapter(1);
            menuInfo.add(menuCategoria);
            menuAdapterPositionUtils.add(new MenuAdapterPositionUtil(
                    null, null, menuCategoria));
            notifyItemInserted(menuAdapterPositionUtils.size());
        }
    }

    public void updateItemTolistPost(Menu_Categoria menuCategoria, int position) {

        if (menuCategoria != null) {
            MenuAdapterPositionUtil positionUtil = new MenuAdapterPositionUtil(
                    null, null, menuCategoria);
            menuAdapterPositionUtils.set(position, positionUtil);
            notifyItemChanged(menuAdapterPositionUtils.size());
        }
    }

    public void addLoadingFooter() {
        isLoadingAdded = true;
        //addPositionAdapter();
        add(new Menu_Categoria());
    }

    public void removeLoadingFooter() {
        isLoadingAdded = false;
        notifyDataSetChanged();
    }


    public void removeAllInstitutionBestQualified() {
        while (!postArrayList.isEmpty()) {
            postArrayList.clear();
            notifyDataSetChanged();
        }
        positionAdapter = 0;
    }

    public void removeAll() {
        while (!menuInfo.isEmpty()) {
            menuInfo.clear();
            notifyDataSetChanged();
            positionAdapter = positionAdapter - 1;
        }
        isLoadingAdded = false;
    }

    public void removeAllPostAdapter() {
        while (!menuAdapterPositionUtils.isEmpty()) {
            menuAdapterPositionUtils.clear();
            notifyDataSetChanged();
        }
        isLoadingAdded = false;
    }

    public Menu_Categoria getItemPostInfo(int position) {
        return menuAdapterPositionUtils.get(position).getMenuData();
    }

    public List<Menu_Categoria> getAllItemPostInfo() {
        return menuInfo;
    }

    public class MenuViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,
            View.OnAttachStateChangeListener {

        View view;

        public MenuViewHolder(View view) {
            super(view);

            ButterKnife.bind(this, itemView);
            this.view = view;

            itemView.setOnClickListener(this);

          /*  itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    recyclerViewOnItemClickListener.onCLick(view, getAdapterPosition());
                }
            });*/

            /*btn_view_more.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    recyclerViewOnItemClickListener.onClickViewDescription(view, getBindingAdapterPosition());
                }
            });*/
        }

        @Override
        public void onClick(View view) {
            viewOnItemClickListener.onCLick(view, getBindingAdapterPosition());
        }

        @Override
        public void onViewAttachedToWindow(View view) {

        }

        @Override
        public void onViewDetachedFromWindow(View view) {

        }
    }

    public class LoadingViewHolder extends RecyclerView.ViewHolder {

        private ProgressBar progressBar;

        public LoadingViewHolder(View itemView) {
            super(itemView);
            progressBar = (ProgressBar) itemView.findViewById(R.id.loadmore_progress);
        }
    }

    public class PostsViewHolder extends RecyclerView.ViewHolder implements
            View.OnClickListener {

        @BindView(R.id.rv_posts_recommended) RecyclerView recyclerView;

        View view;

        public PostsViewHolder(View viewItemInstitutionsBestQualified) {
            super(viewItemInstitutionsBestQualified);
            ButterKnife.bind(this, itemView);
            this.view = viewItemInstitutionsBestQualified;
            itemView.setOnClickListener(this);

            postsAdapter = new PostsAdapter(
                    postArrayList, ctx,
                    new PostsAdapter.ItemClickListenerPosts() {
                        @Override
                        public void onCLick(View view, int position) {
                            viewOnItemClickListener.onClickViewItemPosts(view,
                                    postArrayList.get(position), getBindingAdapterPosition());
                        }
                    });

            //recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setLayoutManager(new LinearLayoutManager(ctx,
                    LinearLayoutManager.HORIZONTAL, false));
            //rv.addItemDecoration(new DividerItemDecoration(ctx, DividerItemDecoration.VERTICAL));
            recyclerView.setAdapter(postsAdapter);

        }

        @Override
        public void onClick(View view) {

        }
    }

    public class MenuGridViewHolder extends RecyclerView.ViewHolder implements
            View.OnClickListener {

        @BindView(R.id.rv_menu_grid)
        RecyclerView recyclerView;

        View view;

        public MenuGridViewHolder(View viewItemInstitutionsBestQualified) {
            super(viewItemInstitutionsBestQualified);
            ButterKnife.bind(this, itemView);
            this.view = viewItemInstitutionsBestQualified;
            itemView.setOnClickListener(this);

            menuAdapterGrid = new MenuAdapterGrid(
                    menuInfo, ctx,
                    new MenuAdapterGrid.ItemClickListener() {
                        @Override
                        public void onCLick(View view, int position) {
                            viewOnItemClickListener.onClickViewMenuGrid(view,
                                    menuInfo.get(position), getBindingAdapterPosition());
                        }
                    });

            // Construimos el recyclerView
            GridLayoutManager lLayout = new GridLayoutManager(ctx, 2,
                    RecyclerView.VERTICAL, false);
            recyclerView.setAdapter(menuAdapterGrid);
            recyclerView.setHasFixedSize(true);
            recyclerView.setHasTransientState(true);
            recyclerView.setLayoutManager(lLayout);

        }

        @Override
        public void onClick(View view) {

        }
    }

}
