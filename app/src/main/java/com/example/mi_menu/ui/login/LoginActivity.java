package com.example.mi_menu.ui.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.ContentLoadingProgressBar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.mi_menu.R;
import com.example.mi_menu.data.model.user.Usuario;
import com.example.mi_menu.ui.menu.container.ContainerActivity;
import com.example.mi_menu.ui.settings.SettingsActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = LoginActivity.class.getSimpleName();
    //declare_auth
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabaseReference;
    private Query mMessagesQuery;
    private ValueEventListener mMessagesListener;
    private ChildEventListener mMessagesQueryListener;

    @BindView(R.id.lly_btn_login) LinearLayout btnLogin;
    @BindView(R.id.lly_btn_register) LinearLayout btnCreateAccount;
    @BindView(R.id.lly_btn_setting) LinearLayout btnSetting;
    @BindView(R.id.pb) ContentLoadingProgressBar pb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ButterKnife.bind(this, this);

        showProgress();

        mAuth = FirebaseAuth.getInstance();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();

        loadViews();

    }

    private void loadViews() {

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showLoginDialog();
            }
        });

        btnCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCreateAccountDialog();
            }
        });

        btnSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, SettingsActivity.class));
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            getUserInfo(currentUser.getUid());
        }else {
            hideProgress();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        cleanBasicListener();
        //cleanBasicQuery();
        // mDatabaseReference.removeEventListener((ValueEventListener) this);
    }

    public void cleanBasicListener() {
        // Clean up value listener
        // [START clean_basic_listen]
        if (mMessagesListener!=null)
        mDatabaseReference.removeEventListener(mMessagesListener);
        // [END clean_basic_listen]
    }

    public void cleanBasicQuery() {
        // Clean up query listener
        // [START clean_basic_query]
        mMessagesQuery.removeEventListener(mMessagesQueryListener);
        // [END clean_basic_query]
    }

    //Recuperar la informacion del usuario logueado
    public void getUserInfo(String id) {

        mDatabaseReference = mDatabaseReference.child("usuario").child(id);

        mMessagesListener=new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {
                    Usuario usuarios = dataSnapshot.getValue(Usuario.class);
                    reload(usuarios,id);

                } else {
                    System.out.println("no se consulto nada del idmayusculo " + id);
                    hideProgress();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        mDatabaseReference.addValueEventListener(mMessagesListener);

        /*mDatabaseReference.child("usuario").child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mDatabaseReference.removeEventListener(this);
                if (dataSnapshot.exists()) {
                    Usuario usuarios = dataSnapshot.getValue(Usuario.class);
                    reload(usuarios,id);

                } else {
                    System.out.println("no se consulto nada del idmayusculo " + id);
                    hideProgress();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });*/
        //System.out.println("ID "+usuarios.getPassword()+"  idmayusculo "+id);

    }

    private void reload(Usuario usuario,String id_user) {

        hideProgress();
        Intent intent = new Intent(LoginActivity.this, ContainerActivity.class);
        intent.putExtra("user_parcel", usuario);
        intent.putExtra("id_user", id_user);
        startActivity(intent);

        //startActivity(new Intent(LoginActivity.this, ContainerActivity.class));
        finish();
    }


    public void showLoginDialog() {

        LoginDialog infoDialogFrag = new LoginDialog();
        Bundle bundle = new Bundle();

        infoDialogFrag.setCancelable(false);
        //infoDialogFrag.setArguments(bundle);
        infoDialogFrag.show(getSupportFragmentManager().beginTransaction(), "WELCOME_DIALOG_FRAGMENT");
    }

    public void showCreateAccountDialog() {

        CreateAccountDialog infoDialogFrag = new CreateAccountDialog();
        Bundle bundle = new Bundle();

        infoDialogFrag.setCancelable(false);
        //infoDialogFrag.setArguments(bundle);
        infoDialogFrag.show(getSupportFragmentManager().beginTransaction(), "WELCOME_DIALOG_FRAGMENT");
    }

    public void showProgress(){
        pb.setVisibility(View.VISIBLE);
        pb.show();
    }
    public void hideProgress(){
        pb.setVisibility(View.GONE);
        pb.hide();
    }
}