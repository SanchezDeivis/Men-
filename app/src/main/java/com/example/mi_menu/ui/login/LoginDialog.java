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
import android.widget.ImageView;
import android.widget.TextView;
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

import java.util.Objects;
import java.util.concurrent.Executor;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class LoginDialog extends DialogFragment {

    private static final String TAG = LoginActivity.class.getSimpleName();

    @BindView(R.id.btn_back) ImageButton btnBack;
    @BindView(R.id.tv_forgot_pass) TextView btnForgotPass;
    @BindView(R.id.btn_enter) Button btnLogin;
    @BindView(R.id.et_email) EditText email;
    @BindView(R.id.et_pass) EditText pass;
    @BindView(R.id.pb) ContentLoadingProgressBar pb;

    //declare_auth
    private FirebaseAuth mAuth;
    private DatabaseReference mdatabase;
    Context context;

    public LoginDialog() {
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
                .inflate(R.layout.fragment_login_dialog, null);

        this.context = getContext();
        ButterKnife.bind(this, view);

        mAuth = FirebaseAuth.getInstance();
        mdatabase = FirebaseDatabase.getInstance().getReference();

        AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
        alert.setView(view);

        loadView();

        return alert.create();
    }

    private void loadView() {

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String strEmail, strPass;
                strEmail = email.getText().toString();
                strPass = pass.getText().toString();
                String verifiData = validateData(strEmail, strPass);

                if (verifiData.equalsIgnoreCase("OK"))
                    signIn(strEmail, strPass);
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

        btnForgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(context,ResetPasswordActivity.class));
                getDialog().dismiss();
            }
        });
    }

    private String validateData(String strEmail, String strPass) {

        if (strEmail.isEmpty())
            return "Falta el correo electr??nico";

        String regx = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";
        //Compile regular expression to get the pattern
        Pattern pattern = Pattern.compile(regx);
        //Create instance of matcher
        Matcher matcher = pattern.matcher(strEmail);
        boolean emailInvalid = matcher.matches();

        if (!emailInvalid)
            return "Correo electr??nico no valido";

        if (strPass.isEmpty())
            return "Falta la contrase??a";

        return "OK";
    }

    private void signIn(String email, String password) {
        //sign_in_with_email

        pb.setVisibility(View.VISIBLE);
        pb.show();

        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithEmail:success");
                    FirebaseUser user = mAuth.getCurrentUser();
                    getUserInfo(user.getUid());
                    //updateUI(user);
                } else {
                    // If sign in fails, display a message to the user.
                    String error = task.getException().getMessage().toString();
                    int errorCode = task.getException().hashCode();
                    Log.w(TAG, "signInWithEmail:failure__"+error, task.getException());
                    Log.w(TAG, "signInWithEmail:failure__code"+errorCode, task.getException().getCause());
                    Toast.makeText(context, "Authentication failed.", Toast.LENGTH_SHORT).show();
                    //updateUI(null);
                    //getDialog().dismiss();
                    pb.setVisibility(View.GONE);
                    pb.hide();
                }
            }
        });

    }

    //Recuperar la informacion del usuario logueado
    private void getUserInfo(String id) {

        mdatabase.child("usuario").child(id).addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mdatabase.removeEventListener(this);
                if (dataSnapshot.exists()) {
                    Usuario usuarios = dataSnapshot.getValue(Usuario.class);
                    //String userloinpassword = dataSnapshot.child("password").getValue().toString();
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

        if (usuarios!=null) {

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
