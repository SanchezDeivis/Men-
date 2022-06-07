package com.example.mi_menu.ui.menu.add_products.adapter

import android.content.Context
import com.example.mi_menu.data.model.menu.Menu_Categoria
import androidx.recyclerview.widget.RecyclerView
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import com.example.mi_menu.R
import com.squareup.picasso.Picasso
import com.squareup.picasso.MemoryPolicy
import com.squareup.picasso.NetworkPolicy
import android.widget.TextView

class AddMenuAdapter(
    private val menuData: List<Menu_Categoria>,
    private val listener: ItemClickListener
    ):RecyclerView.Adapter<AddMenuAdapter.MyViewHolder>(){

    interface ItemClickListener {
        fun onCLick(data: Menu_Categoria, position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.item_menu_data,parent,false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val data = menuData[position]
        holder.bind(data,position)
    }

    override fun getItemCount(): Int = menuData.size

   inner class MyViewHolder(view: View):RecyclerView.ViewHolder(view) {

        private val title_name_menu=view.findViewById<TextView>(R.id.tv_title_name_menu)
        private val imageView=view.findViewById<ImageView>(R.id.iv_image_view_menu)
        private val ctx:Context = view.context

        fun bind(data: Menu_Categoria,position: Int) {

            title_name_menu.text=data.name
            if (data.image != null) {
                Picasso.get()
                    .load(data.image)
                    .error(ctx.resources.getDrawable(R.drawable.avatar_menu))
                    .memoryPolicy(MemoryPolicy.NO_CACHE).networkPolicy(NetworkPolicy.NO_CACHE)
                    .into(imageView)
            } else {
               imageView!!.setImageDrawable(ctx.resources.getDrawable(R.drawable.avatar_menu))
            }

            imageView.setOnClickListener({listener.onCLick(data,position)})
        }
    }
}

/*class AddMenuAdapter(
    private val menuData: List<Menu_Categoria>, private val ctx: Context,
    private val listener: ItemClickListener
) : RecyclerView.Adapter<AddMenuAdapter.MyViewHolder>() {

    interface ItemClickListener {
        fun onCLick(view: View?, position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_menu_data, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val data = menuData[position]
        if (data.image != null) {
            Picasso.get()
                .load(data.image)
                .error(ctx.resources.getDrawable(R.drawable.avatar_menu))
                .memoryPolicy(MemoryPolicy.NO_CACHE).networkPolicy(NetworkPolicy.NO_CACHE)
                .into(holder.imageView)
        } else {
            holder.imageView!!.setImageDrawable(ctx.resources.getDrawable(R.drawable.avatar_menu))
        }
        if (data != null) {
            holder.title_name_menu!!.text = data.name
        }
    }

    override fun getItemCount(): Int {
        return menuData.size
    }

    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {
        @JvmField
        @BindView(R.id.tv_title_name_menu)
        var title_name_menu: TextView? = null

        @JvmField
        @BindView(R.id.iv_image_view_menu)
        var imageView: ImageView? = null

        //@BindView(R.id.iv_image_view_menu) CircleImageView imageView;
        var itemview: View
        override fun onClick(view: View) {
            listener.onCLick(view, bindingAdapterPosition)
        }

        init {
            ButterKnife.bind(this, itemView)
            itemview = view
            itemView.setOnClickListener(this)

        }
    }
}*/
