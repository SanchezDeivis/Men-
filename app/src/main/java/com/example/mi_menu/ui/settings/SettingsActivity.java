package com.example.mi_menu.ui.settings;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.gridlayout.widget.GridLayout;

import com.example.mi_menu.R;
import com.example.mi_menu.data.model.user.Usuario;
import com.example.mi_menu.ui.login.LoginActivity;
import com.example.mi_menu.ui.login.LoginDialog;
import com.example.mi_menu.ui.login.ResetPasswordActivity;
import com.google.firebase.auth.FirebaseAuth;

import butterknife.BindView;
import butterknife.ButterKnife;


public class SettingsActivity extends AppCompatActivity {

    private final String TAG = getClass().getSimpleName();
    /*Toolbar toolbar;*/
    SharedPreferences sharedPreferences;
    Button buttonRed, buttonIndigo, button3;
    @BindView(R.id.btn_back)
    ImageButton btn_back;
    @BindView(R.id.btn_more_menu)
    ImageButton btn_more_menu;
    @BindView(R.id.gridLayout)
    GridLayout gridLayout;
    @BindView(R.id.cardAmarillo)
    CardView cardAmarillo;
    @BindView(R.id.cardCeleste)
    CardView cardCeleste;
    @BindView(R.id.cardRojo)
    CardView cardRojo;
    @BindView(R.id.cardVerde)
    CardView cardVerde;
    @BindView(R.id.cardAzul)
    TextView cardAzul;
    Context context;
    Intent intent;
    private FirebaseAuth mAuth;
    private String email = "";
    private String name_bussines = "";
    private String id_user = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedPreferences = getSharedPreferences("VALUES", MODE_PRIVATE);
        int theme = sharedPreferences.getInt("THEME", 0);
        mAuth = FirebaseAuth.getInstance();

        // Obtenemos el Intent
        intent = getIntent();
        if (intent != null && intent.getExtras() != null) {
            try {
                Bundle extras = intent.getExtras();
                // Obtenemos el objeto del médico que contiene el usuario
                email = extras.getString("email");
                name_bussines = extras.getString("name_bussines");
                id_user = extras.getString("id_user");

            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
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

        setContentView(R.layout.settings_activity);
        this.context = this;
        ButterKnife.bind(this, this);
        /*gridLayout=new GridLayout(this);*/

        /*// Setup Toolbar
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);*/

        // Fix issues of navigation and status bars
       // navigationBarStatusBar();

        // Setup buttons to apply themes.
        setMyTheme();

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //onBackPressed();
                Intent intent = new Intent(SettingsActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });

        btn_more_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(SettingsActivity.this, btn_more_menu);
                popupMenu.getMenuInflater().inflate(R.menu.menu_main, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(menuItem -> {

                    switch (menuItem.getItemId()/*getTitle().toString()*/) {

                        /*case "Cerrar sesión":
                            dialogSignOut();
                            break;

                        case "Gestión del menú":
                            //setting();
                            break;*/
                        case R.id.action_log_out:
                            dialogSignOut();
                            break;
                        case R.id.action_settings_menu:
                            dialogSettingMenu();
                            break;
                        default:
                            break;
                    }
                    return true;
                });

                popupMenu.show();
            }
        });

    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        // Checks the orientation of the screen
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            Toast.makeText(this, "landscape", Toast.LENGTH_SHORT).show();
            //gridLayout.setColumnCount(2);
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            Toast.makeText(this, "portrait", Toast.LENGTH_SHORT).show();
            //gridLayout.setColumnCount(2);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings_menu) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


   /* public void navigationBarStatusBar() {

        // Fix portrait issues
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            // Fix issues for KitKat setting Status Bar color primary
            if (Build.VERSION.SDK_INT >= 19) {
                TypedValue typedValue19 = new TypedValue();
                SettingsActivity.this.getTheme().resolveAttribute(R.attr.colorPrimary, typedValue19, true);
                final int color = typedValue19.data;
                FrameLayout statusBar = (FrameLayout) findViewById(R.id.statusBar);
                statusBar.setBackgroundColor(color);
            }

            // Fix issues for Lollipop, setting Status Bar color primary dark
            if (Build.VERSION.SDK_INT >= 21) {
                TypedValue typedValue21 = new TypedValue();
                SettingsActivity.this.getTheme().resolveAttribute(R.attr.colorPrimaryDark, typedValue21, true);
                final int color = typedValue21.data;
                FrameLayout statusBar = (FrameLayout) findViewById(R.id.statusBar);
                statusBar.setBackgroundColor(color);
            }
        }

        // Fix landscape issues (only Lollipop)
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            if (Build.VERSION.SDK_INT >= 19) {
                TypedValue typedValue19 = new TypedValue();
                SettingsActivity.this.getTheme().resolveAttribute(R.attr.colorPrimary, typedValue19, true);
                final int color = typedValue19.data;
                FrameLayout statusBar = (FrameLayout) findViewById(R.id.statusBar);
                statusBar.setBackgroundColor(color);
            }
            if (Build.VERSION.SDK_INT >= 21) {
                TypedValue typedValue = new TypedValue();
                SettingsActivity.this.getTheme().resolveAttribute(R.attr.colorPrimaryDark, typedValue, true);
                final int color = typedValue.data;
                FrameLayout statusBar = (FrameLayout) findViewById(R.id.statusBar);
                statusBar.setBackgroundColor(color);
            }
        }
    }*/

    private void setMyTheme() {

        cardAzul.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sharedPreferences.edit().putInt("THEME", 1).apply();
                setTheme(R.style.AppTheme4);
                /*Intent intent = new Intent(MainActivity.this, MainActivity.class);
                startActivity(intent);*/
                recreate();
            }
        });

        cardAmarillo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sharedPreferences.edit().putInt("THEME", 2).apply();
                setTheme(R.style.AppTheme5);
                /*Intent intent = new Intent(MainActivity.this, MainActivity.class);
                startActivity(intent);*/
                recreate();
            }
        });

        cardVerde.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sharedPreferences.edit().putInt("THEME", 3).apply();
                setTheme(R.style.AppTheme6);
                /*Intent intent = new Intent(MainActivity.this, MainActivity.class);
                startActivity(intent);*/
                recreate();
            }
        });


        cardRojo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sharedPreferences.edit().putInt("THEME", 4).apply();
                setTheme(R.style.AppTheme2);
               /* Intent intent = new Intent(MainActivity.this, MainActivity.class);
                startActivity(intent);*/

                recreate();
            }
        });


        cardCeleste.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sharedPreferences.edit().putInt("THEME", 5).apply();
                setTheme(R.style.AppTheme3);
               /* Intent intent = new Intent(MainActivity.this, MainActivity.class);
                startActivity(intent);*/

                recreate();
            }
        });
    }

    public void dialogSignOut() {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Atención")
                .setMessage("¿Estás seguro de cerrar sesión?")
                .setPositiveButton("SI", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mAuth.signOut();
                        /*startActivity(new Intent(SettingsActivity.this, LoginActivity.class));
                        finish();*/

                        Intent intent = new Intent(SettingsActivity.this, LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        dialogInterface.dismiss();
                        finish();

                    }
                })
                .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();

    }

    private void dialogSettingMenu() {

        if (email != null && !email.isEmpty()) {

            ConfirmPasswordDialog confirmPasswordDialog=new ConfirmPasswordDialog();
            Bundle bundle = new Bundle();
            bundle.putString("email",email);
            bundle.putString("bussinesName",name_bussines);
            bundle.putString("id_user",id_user);
            confirmPasswordDialog.setArguments(bundle);
            confirmPasswordDialog.setCancelable(true);
            confirmPasswordDialog.show(getSupportFragmentManager().beginTransaction(), "WELCOME_DIALOG_FRAGMENT");

        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Atención")
                    .setMessage("Si desea administrar su menú debe iniciar sesión")
                    .setPositiveButton("ACEPTAR", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            dialogInterface.dismiss();
                        }
                    });
            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }
}