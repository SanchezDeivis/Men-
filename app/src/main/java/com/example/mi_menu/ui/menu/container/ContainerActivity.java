package com.example.mi_menu.ui.menu.container;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.example.mi_menu.R;
import com.example.mi_menu.data.model.user.Usuario;
import com.example.mi_menu.ui.menu.products.ProductsFragment;
import com.google.firebase.auth.FirebaseUser;

public class ContainerActivity extends AppCompatActivity {

    private final String TAG = getClass().getSimpleName();
    private String tab_selected = null;
    SharedPreferences sharedPreferences;
    Intent intent;
    Usuario user;
   private String id_user;

   /* @BindView(R.id.)
    BottomNavigationView bnv_patient;*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = getSharedPreferences("VALUES", MODE_PRIVATE);
        int theme = sharedPreferences.getInt("THEME", 0);

        // Obtenemos el Intent
        intent = getIntent();

        if (intent != null && intent.getExtras() != null) {
            try {
                Bundle extras = intent.getExtras();
                // Obtenemos el objeto del médico que contiene el usuario
                user = (Usuario) extras.getParcelable("user_parcel");
                id_user = extras.getString("id_user");

            }catch (Exception e){
                Log.e(TAG,e.getMessage());
            }
        }

        switch (theme) {
            case 0:
                setTheme(R.style.AppTheme);
                break;
            case 1:
                setTheme(R.style.AppTheme4);
                break;
            case 2:
                setTheme(R.style.AppTheme5);
                break;
            case 3:
                setTheme(R.style.AppTheme6);
                break;
            case 4:
                setTheme(R.style.AppTheme2);
                break;
            case 5:
                setTheme(R.style.AppTheme3);
                break;
        }
        setContentView(R.layout.activity_container);

       // bnv_patient.setSelectedItemId(R.id.action_home);
        ProductsFragment productsFragment= new ProductsFragment();

        Bundle args = new Bundle();

        args.putString("email",user.getEmail() );
        args.putString("name_bussines", user.getBussinesName());
        args.putString("id_user", id_user);

        productsFragment.setArguments(args);

        getSupportFragmentManager().beginTransaction().replace
                (R.id.fl_activity_container, productsFragment,
                        "Productos")
                .setTransition(FragmentTransaction.TRANSIT_ENTER_MASK)
                /*.addToBackStack("Productos")*/
                .commit();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onStart() {
        super.onStart();  // Always call the superclass method first

    }

    @Override
    public void onRestart() {
        super.onRestart();  // Always call the superclass method first
        // Activity being restarted from stopped state
    }
}