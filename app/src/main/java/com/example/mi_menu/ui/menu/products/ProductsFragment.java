package com.example.mi_menu.ui.menu.products;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mi_menu.R;
import com.example.mi_menu.data.db.model.ResourcePosts;
import com.example.mi_menu.data.model.menu.MenuCategory;
import com.example.mi_menu.data.model.menu.Menu_Categoria;
import com.example.mi_menu.ui.menu.add_products.adapter.AddMenuAdapter;
import com.example.mi_menu.ui.menu.products.adapter.MenuAdapter;
import com.example.mi_menu.ui.settings.SettingsActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Sánchez Deivis machine on 14,noviembre,2021
 */

/**
 * A simple {@link Fragment} subclass.
 */
public class ProductsFragment extends Fragment {


    @BindView(R.id.tv_title)
    TextView tv_title;
    /*@BindView(R.id.poster_slider_viewpager) ViewPager2 viewPagerPosts;
    @BindView(R.id.tly_indicator_post) TabLayout tableLayoutPostsIndicator;*/
    @BindView(R.id.rv_menu)
    RecyclerView rv_itemMenu;
    @BindView(R.id.btn_more_menu)
    ImageButton btn_more_menu;

    SharedPreferences sharedPreferences;
    Context context;
    List<ResourcePosts> postsInfo = new ArrayList<>();

    private List<Menu_Categoria> itemListPosts = new ArrayList<>();
    private MenuAdapter menuAdapter;
    private String email = "";
    private String name_bussines = "";
    private String id_user = "";

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private FirebaseAuth mAuth;


    public ProductsFragment() {

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*sharedPreferences = context.getSharedPreferences("VALUES", Context.MODE_PRIVATE);
        int theme = sharedPreferences.getInt("THEME", 1);

       switch (theme){
            case 1: context.setTheme(R.style.AppTheme);
                break;
            case 2: context.setTheme(R.style.AppTheme2);
                break;
            case 3: context.setTheme(R.style.AppTheme3);
                break;
        }*/
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_products, container, false);

        ButterKnife.bind(this, view);

        this.firebaseDatabase = FirebaseDatabase.getInstance();
        this.databaseReference = firebaseDatabase.getReference();
        this.mAuth = FirebaseAuth.getInstance();

        if (getArguments() != null) {
            email = getArguments().getString("email");
            name_bussines = getArguments().getString("name_bussines");
            id_user = getArguments().getString("id_user");
        }

        if (name_bussines != null && !name_bussines.isEmpty()) {
            tv_title.setText(name_bussines);
        }

        loadViews();
        //getURL();
        getMenuData();
        return view;
    }

    @Override
    public void onStop() {
        super.onStop();
    }


    private void getURL() {
        List<ResourcePosts> posts = new ArrayList<>();
        posts.add(new ResourcePosts(R.drawable.beer));
        posts.add(new ResourcePosts(R.drawable.goulash));
        posts.add(new ResourcePosts(R.drawable.hamburger));
        posts.add(new ResourcePosts(R.drawable.meat));
        posts.add(new ResourcePosts(R.drawable.cocktail));

        postsInfo.clear();
        postsInfo.addAll(posts);

        menuAdapter.addPositionAdapter(1);
        menuAdapter.addInstitutionBestQualifie(postsInfo);
        if (menuAdapter != null)
            menuAdapter.notifyDataSetChanged();

        //viewPagerPosts.getOnFocusChangeListener();
    }

    private void getMenuData() {
        List<Menu_Categoria> menuData = new ArrayList<>();

        String id = mAuth.getCurrentUser().getUid();
        try {
            System.out.println("idminusculo "+id);

            databaseReference
                    .child("usuario")
                    .child(id_user)
                    .child("Menu_Categoria")
                    .addValueEventListener(new ValueEventListener() {

                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    if (dataSnapshot.exists()) {

                        for (DataSnapshot objDataSnapshot : dataSnapshot.getChildren()) {
                            Menu_Categoria newCategory = objDataSnapshot.getValue(Menu_Categoria.class);

                            menuData.add(new Menu_Categoria(newCategory.getImage(), newCategory.getName()));

                        }

                        itemListPosts.clear();
                        itemListPosts.addAll(menuData);

                        menuAdapter.addPositionAdapter(1);
                        menuAdapter.addMenuList(itemListPosts);

                        /*menuAdapter.addAll(itemListPosts);*/
                        if (menuAdapter != null)
                            menuAdapter.notifyDataSetChanged();
                        //viewPagerPosts.getOnFocusChangeListener();


                    } else {
                        showInfoDialog("Atención","Aun no ha agregado información de sus productos.\n" +
                                "Ingresa en ajustes y gestiona tu menú.");
                        //Toast. makeText(getActivity(), "Aun no ha agregado información", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        } catch (Exception e) {
            Toast.makeText(getActivity(), "Ocurrió un problema, intente nuevamente", Toast.LENGTH_SHORT).show();
        }
        //dialog.dismiss();



        /*itemListPosts.clear();
        itemListPosts.addAll(menuData);

        menuAdapter.addPositionAdapter(1);
        menuAdapter.addMenuList(itemListPosts);

        *//*menuAdapter.addAll(itemListPosts);*//*
        if (menuAdapter != null)
            menuAdapter.notifyDataSetChanged();
        //viewPagerPosts.getOnFocusChangeListener();*/
    }

    private void loadViews() {

        btn_more_menu.setOnClickListener(view -> {
            Intent inten = new Intent(context, SettingsActivity.class);
            inten.putExtra("email", email);
            inten.putExtra("name_bussines", name_bussines);
            inten.putExtra("id_user", id_user);
            startActivity(inten);
            getActivity().finish();
        });

        ///ViewPager
        /*viewPagerPosts.setAdapter(new ViewPagerAdapter(
                context, postsInfo, viewPagerPosts,
                new ViewPagerAdapter.PagerAdapterViewOnItemClickListener() {
                    @Override
                    public void onCLickView(View view, int position) {
                        *//*ResourcePosts posts = postsInfo.getResourcePosts().get(position);
                        // Pasaremos de la actividad actual a OtraActivity
                        Intent intent = new Intent(context, ViewVideoPlayActivity.class);
                        intent.putExtra("url", posts.getImage());
                        intent.putExtra("name_institution", postsInfo.getInstitution().getName());
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);*//*
                    }
                }));

        new TabLayoutMediator(tableLayoutPostsIndicator,
                viewPagerPosts, true, true, (tab, i) -> {
        }).attach();

        viewPagerPosts.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);

            }

            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
            }
        });*/


        // Click en algún ítem de la lista
        menuAdapter = new MenuAdapter(/*itemListPosts,*/context,
                new MenuAdapter.viewOnItemClickListener() {

                    @Override
                    public void onCLick(View view, int position) {
                        /*String type = "me gustó";
                        postsInfoItem = postsInstitutionAdapter.getItemPostInfo(position);
                        int postId = postsInfoItem.getId();
                        reactToAPost(postsInfoItem, postId, type, position);*/
                    }

                    @Override
                    public void onClickViewMenuGrid(View view, Menu_Categoria itemMenuCategoria, int position) {
                        /* int postId = postsInstitutionAdapter.getItemPostInfo(position).getId();
                        TextView text = (TextView) rv_postInstitution.findViewHolderForAdapterPosition(position)
                                .itemView.findViewById(R.id.tv_description_post);

                        TextView textMore = (TextView) rv_postInstitution.findViewHolderForAdapterPosition(position)
                                .itemView.findViewById(R.id.tv_description_more);

                        TextView btnViewMore = (TextView) rv_postInstitution.findViewHolderForAdapterPosition(position)
                                .itemView.findViewById(R.id.tv_view_more);

                        if (text != null && textMore != null && btnViewMore != null)
                            if (text.getVisibility() == View.VISIBLE) {
                                text.setVisibility(View.GONE);
                                textMore.setVisibility(View.VISIBLE);
                                btnViewMore.setText("...Ver menos");
                            } else {
                                text.setVisibility(View.VISIBLE);
                                textMore.setVisibility(View.GONE);
                                btnViewMore.setText("...Ver mas");
                            }*/
                        Toast.makeText(context, itemMenuCategoria.getName(), Toast.LENGTH_SHORT).show();
                    }


                    @Override
                    public void onClickViewItemPosts(
                            View view, ResourcePosts item, int position) {
                        /*Verificamos si la institución tiene servicios por EPS o PARTICULAR*//*
                        presenter.getTypePayerInstitution(item);*/
                    }
                });

        // Construimos el recyclerView
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context,
                LinearLayoutManager.VERTICAL, false);
        rv_itemMenu.setLayoutManager(linearLayoutManager);
        rv_itemMenu.setAdapter(menuAdapter);

       /* // Construimos el recyclerView
        GridLayoutManager lLayout = new GridLayoutManager(getContext(), 2,
                RecyclerView.VERTICAL,false);
        rv_itemMenu.setAdapter(menuAdapter);
        rv_itemMenu.setHasFixedSize(true);
        rv_itemMenu.setLayoutManager(lLayout);*/

    }

    public void showInfoDialog(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(false).setTitle(title)
                .setMessage(message)
                .setPositiveButton("ACEPTAR", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onResume() {
        super.onResume();


    }

    @Override
    public void onStart() {
        super.onStart();

    }
}
