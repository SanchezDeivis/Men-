package com.example.mi_menu.ui.login;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.ContentLoadingProgressBar;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.example.mi_menu.R;
import com.example.mi_menu.data.model.user.Usuario;
import com.example.mi_menu.ui.menu.container.ContainerActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class CreateAccountDialog extends DialogFragment {

    private static final String TAG = LoginActivity.class.getSimpleName();

    @BindView(R.id.btn_back)
    ImageButton btnBack;
    @BindView(R.id.btn_enter)
    Button btnCreateAccount;
    @BindView(R.id.et_business_name)
    EditText businessName;
    @BindView(R.id.et_email)
    EditText email;
    @BindView(R.id.et_pass)
    EditText pass;
    @BindView(R.id.et_confirm_pass)
    EditText passComfirm;
    @BindView(R.id.pb)
    ContentLoadingProgressBar pb;

    //declare_auth
    private FirebaseAuth mAuth;
    private DatabaseReference mdatabase;
    private StorageReference mStorage;

    Context context;

    public CreateAccountDialog() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;

        getDialog().getWindow().setAttributes((WindowManager.LayoutParams) params);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        View view = requireActivity().getLayoutInflater()
                .inflate(R.layout.fragment_create_account_dialog, null);

        this.context = getContext();
        ButterKnife.bind(this, view);

        mAuth = FirebaseAuth.getInstance();
        mdatabase = FirebaseDatabase.getInstance().getReference();
        mStorage = FirebaseStorage.getInstance().getReference();

        AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
        alert.setView(view);

        loadView();

        return alert.create();
    }

    private void loadView() {

        btnCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String strEmail, strPass, strBusinessName, strconfirmPass;
                strEmail = email.getText().toString();
                strPass = pass.getText().toString();
                strBusinessName = businessName.getText().toString();
                strconfirmPass = passComfirm.getText().toString();
                String verifiData = validateData(strEmail, strPass, strconfirmPass, strBusinessName);
                File bussinesUrl = null;

                if (verifiData.equalsIgnoreCase("OK"))
                    createAccount(bussinesUrl, strBusinessName,strEmail,strPass);
                else
                    Toast.makeText(getContext(), verifiData, Toast.LENGTH_SHORT).show();
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDialog().dismiss();
            }
        });
    }

    private String validateData(String strEmail, String strPass, String strconfirmPass, String strBusinessName) {

        if (strBusinessName.isEmpty())
            return "Falta el nombre del negocio";

        if (strEmail.isEmpty())
            return "Falta el correo electrónico";

        String regx = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";
        //Compile regular expression to get the pattern
        Pattern pattern = Pattern.compile(regx);
        //Create instance of matcher
        Matcher matcher = pattern.matcher(strEmail);
        boolean emailInvalid = matcher.matches();

        if (!emailInvalid)
            return "Correo electrónico no valido";

        if (strPass.isEmpty()) return "Falta la contraseña";

        if (strBusinessName.isEmpty()) return "Confirma la contraseña";

        if (!strPass.equalsIgnoreCase(strconfirmPass) &&
                !strconfirmPass.equalsIgnoreCase(strPass)) {
            return "Las contraseña no son correctas";
        }

        return "OK";
    }

   /* private void createAccount(String email, String password, String BusinessName) {
        //create_user_with_email
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(context, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            //updateUI(null);
                            getDialog().dismiss();
                        }
                    }
                });
    }*/

    //Registrar usuario nuevo
    private void createAccount(File bussinesUrl, String bussinesName, String email, String password) {

        pb.setVisibility(View.VISIBLE);
        pb.show();

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {
                    /*Map<String, Object> map = new HashMap<>();
                    map.put("nombre", nombre);
                    map.put("telefono", telefono);
                    map.put("email", email);
                    map.put("password", password);
                    map.put("imagen", imagen);*/
                    Usuario usuarioNuevo = new Usuario(/*bussinesUrl,*/bussinesName,email);

                    String id = mAuth.getCurrentUser().getUid();
                    mdatabase.child("usuario").child(id).setValue(usuarioNuevo).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task2) {

                            if (task2.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "createUserWithEmail:success");
                                FirebaseUser user = mAuth.getCurrentUser();
                                getUserInfo(user.getUid());
                                //updateUI(user);
                                Toast.makeText(context, "Muy bien", Toast.LENGTH_SHORT).show();
                                
                            } else {
                                Toast.makeText(context, "No se pudieron crear los datos", Toast.LENGTH_SHORT).show();
                                pb.setVisibility(View.GONE);
                                pb.hide();
                            }
                        }
                    });

                } else {
                    Toast.makeText(context, "No se pudo registrar este usuario", Toast.LENGTH_SHORT).show();
                    pb.setVisibility(View.GONE);
                    pb.hide();
                }
            }
        });
    }

    //Recuperar la informacion del usuario logueado
    public void getUserInfo(String id) {

        mdatabase.child("usuario").child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mdatabase.removeEventListener(this);
                if (dataSnapshot.exists()) {
                    Usuario usuarios = dataSnapshot.getValue(Usuario.class);
                   // String userloinpassword = dataSnapshot.child("password").getValue().toString();
                    updateUI(usuarios,id);

                } else {
                    System.out.println("no se consulto nada del idmayusculo " + id);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        //System.out.println("ID "+usuarios.getPassword()+"  idmayusculo "+id);

    }

    private void updateUI(Usuario usuarios,String id_user) {
        if (usuarios != null) {

            pb.setVisibility(View.GONE);
            pb.hide();

            Intent intent = new Intent(getContext(), ContainerActivity.class);
            intent.putExtra("user_parcel", usuarios);
            intent.putExtra("id_user", id_user);
            requireActivity().startActivity(intent);
            getDialog().dismiss();
        }
    }
}
