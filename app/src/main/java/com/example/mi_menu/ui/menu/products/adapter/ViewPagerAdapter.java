package com.example.mi_menu.ui.menu.products.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.mi_menu.R;
import com.example.mi_menu.data.db.model.ResourcePosts;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ViewPagerAdapter extends RecyclerView.Adapter<ViewPagerAdapter.MyViewHolder> {
    Context context;
    LayoutInflater mLayoutInflater;
    private List<ResourcePosts> resourcePosts;
    private ViewPager2 viewPager2;
    private Boolean volume_off_down = false;
    private MediaController mediaController;
    // FirebaseStorage storage = FirebaseStorage.getInstance();

    private PagerAdapterViewOnItemClickListener listener;

    public interface PagerAdapterViewOnItemClickListener {
        void onCLickView(View view, int position);
    }

    public ViewPagerAdapter() {
    }

    public ViewPagerAdapter(Context context, List<ResourcePosts> postsInfos, ViewPager2 viewPager2,
                            @NonNull PagerAdapterViewOnItemClickListener listener) {
        this.context = context;
        this.resourcePosts = postsInfos;
        this.viewPager2 = viewPager2;
        //mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.mLayoutInflater = LayoutInflater.from(context);
        this.listener = listener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mLayoutInflater.inflate(R.layout.item_pager, parent, false);
        itemView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));

        return new MyViewHolder(itemView);
    }

    @Override
    public void onViewAttachedToWindow(@NonNull MyViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        //Let's trigger to play source
        // play();
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull MyViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public void onDetachedFromRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        //Stop playing
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        int posts = resourcePosts.get(position).getImage();
        //String posts = resourcePosts.get(position).getImage();
        //String url = (ConfigUtil.BASE_URL_IMAGES + posts);
        if (posts==1){
            holder.image_View.setVisibility(View.VISIBLE);
            Picasso.get()
                    .load(R.drawable.beer)
                    /*.fit()
                    .centerCrop()*/
                    .into(holder.image_View);
        }
        if (posts==2){
            holder.image_View.setVisibility(View.VISIBLE);
            Picasso.get()
                    .load(R.drawable.goulash)
                    /*.fit()
                    .centerCrop()*/
                    .into(holder.image_View);
        }
        if (posts==3){
            holder.image_View.setVisibility(View.VISIBLE);
            Picasso.get()
                    .load(R.drawable.hamburger)
                    /*.fit()
                    .centerCrop()*/
                    .into(holder.image_View);
        }
        if (posts==4){
            holder.image_View.setVisibility(View.VISIBLE);
            Picasso.get()
                    .load(R.drawable.meat)
                    /*.fit()
                    .centerCrop()*/
                    .into(holder.image_View);
        }

        if (posts==5){
            holder.image_View.setVisibility(View.VISIBLE);
            Picasso.get()
                    .load(R.drawable.cocktail)
                    /*.fit()
                    .centerCrop()*/
                    .into(holder.image_View);
        }


    }

    @Override
    public int getItemCount() {
        return resourcePosts == null ? 0 : resourcePosts.size();
    }

    public ResourcePosts getItemResource(int position) {
        return resourcePosts.get(position);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.poster_slider_image_view)
        ImageView image_View;
        @BindView(R.id.poster_slider_video_view)
        VideoView video_View;
        //@BindView(R.id.poster_slider_video_view) VideoView video_View;
        View itemview;

        public MyViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, itemView);
            this.itemview = view;
            itemView.setOnClickListener(this);
            //viewPager2.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
            /*play_video.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onCLickView(view, getAdapterPosition());
                }
            });*/
        }

        @Override
        public void onClick(View view) {
            // listener.onCLickView(view, getAdapterPosition());
        }
    }


//      @Override
//    public int getCount() {
//        return resourcePosts == null ? 0 : resourcePosts.size();
//    }
//
//    @Override
//    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
//        return view == ((LinearLayout) object);
//    }
//
//    @Override
//    public void startUpdate(@NonNull ViewGroup container) {
//        super.startUpdate(container);
//    }
//
//    @Override
//    public int getItemPosition(@NonNull Object object) {
//        return super.getItemPosition(object);
//    }
//
//
//
//    @NonNull
//    @Override
//    public Object instantiateItem(@NonNull ViewGroup container, int position) {
//        View itemView = mLayoutInflater.inflate(R.layout.item_post_pager, container, false);
//
//        imageView = itemView.findViewById(R.id.poster_slider_image_view);
//        videoView = itemView.findViewById(R.id.poster_slider_video_view);
//
//        String posts = resourcePosts.get(position).getImage();
//        String url = (ConfigUtil.BASE_URL_IMAGES + posts);
//        try {
//            File file = new File(url);
//            String minetype = FileUtils.getMimeType(file);
//            if (minetype.equalsIgnoreCase("video/mp4")) {
//                imageView.setVisibility(View.GONE);
//                videoView.setVisibility(View.VISIBLE);
//
//                if (videoView != null) {
//                    //add controls to our video player.
//                    videoView.setVideoPath(url);
//                   /* mediacontroller = new MediaController(context);
//                    mediacontroller.setAnchorView(videoView);
//                    videoView.setMediaController(mediacontroller);
//                    // controles como pausa, detener, avanzar, retroceder, etc.
//                    mediacontroller.setAnchorView(videoView);
//                    mediacontroller.show();*/
//                    // videoView.setVideoURI(Uri.parse(url));
//                    videoView.requestFocus();
//                    //videoView.start();
//                    videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
//                        @Override
//                        public void onPrepared(MediaPlayer mediaPlayer) {
//                            videoView.seekTo(0);
//                            mediaPlayer.setLooping(true);
//                            mediaPlayer.setVolume(10, 10);
//                            mediaPlayer.start();
//                        }
//                    });
//                    videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
//                        @Override
//                        public void onCompletion(MediaPlayer mediaPlayer) {
//                            MediaController mediacontroller = new MediaController(itemView.getContext());
//                            mediacontroller.setAnchorView(videoView);
//                            videoView.setMediaController(mediacontroller);
//                            // controles como pausa, detener, avanzar, retroceder, etc.
//                            mediacontroller.setAnchorView(videoView);
//                            mediacontroller.show();
//                            videoView.requestFocus();
//                            videoView.start();
//                        }
//                    });
//                } else { //toast or print "mVideoView is null"
//                }
//
//            } else {
//                videoView.setVisibility(View.GONE);
//                imageView.setVisibility(View.VISIBLE);
//                Picasso.get()
//                        .load(url)
//                        .fit()
//                        .centerCrop()
//                        .into(imageView);
//            }
//        } catch (Exception e) {
//            // posters.add(new RemoteVideo(Uri.parse(url)));
//        }
//
//
//        videoView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (!videoView.isPlaying()) {
//                    videoView.start();
//                } else if (videoView.isPlaying()) {
//                    videoView.pause();
//                } else {
//                    MediaPlayer mediaPlayer = new MediaPlayer();
//                    mediaPlayer.setLooping(true);
//                    mediaPlayer.start();
//                }
//            }
//        });
//
//        Objects.requireNonNull(container).addView(itemView);
//        return itemView;
//    }
//
//   @Override
//    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
//        container.removeView((LinearLayout) object);
//    }

}
