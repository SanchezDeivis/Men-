package com.example.mi_menu.ui.settings;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
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
import com.example.mi_menu.ui.menu.add_products.AddProducts;
import com.example.mi_menu.R;
import com.example.mi_menu.ui.login.LoginActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class ConfirmPasswordDialog extends DialogFragment {

    private static final String TAG = LoginActivity.class.getSimpleName();

    @BindView(R.id.btn_back)
    ImageButton btnBack;
    @BindView(R.id.btn_enter)
    Button btnConfirmPass;
    @BindView(R.id.et_pass)
    EditText pass;
    @BindView(R.id.pb)
    ContentLoadingProgressBar pb;

    //declare_auth
    private FirebaseAuth mAuth;
    private String email;
    private String bussinesName;
    private String id_user;
    Context context;

    public ConfirmPasswordDialog() {
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
                .inflate(R.layout.fragment_confirm_pass_dialog, null);

        this.context = getContext();
        ButterKnife.bind(this, view);

        if (getArguments() != null) {
            Bundle b = getArguments();

            email = b.getString("email");
            bussinesName = b.getString("bussinesName");
            id_user = b.getString("id_user");

        } else {
            getDialog().dismiss();
        }

        mAuth = FirebaseAuth.getInstance();

        AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
        alert.setView(view);

        loadView();

        return alert.create();
    }

    private void loadView() {

        btnConfirmPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String strEmail, strPass;
                strEmail = email;
                strPass = pass.getText().toString();

                if (email!=null&&!email.isEmpty()) {
                    String verifiData = validateData(strPass);

                    if (verifiData.equalsIgnoreCase("OK"))
                        signIn(strEmail, strPass);
                    else
                        Toast.makeText(getContext(), verifiData, Toast.LENGTH_SHORT).show();

                }else {
                    androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(context);
                    builder.setTitle("Atención")
                            .setMessage("Si desea administrar su menú debe iniciar sesión")
                            .setPositiveButton("ACEPTAR", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                    dialogInterface.dismiss();
                                    getDialog().dismiss();
                                }
                            });
                    androidx.appcompat.app.AlertDialog dialog = builder.create();
                    dialog.show();
                }
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDialog().dismiss();
            }
        });
    }

    private String validateData(String strPass) {

        if (strPass.isEmpty())
            return "Falta la contraseña";

        return "OK";
    }

    private void signIn(String email, String password) {
        //sign_in_with_email

        showProgress();

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithEmail:success");
                    FirebaseUser user = mAuth.getCurrentUser();
                    settingMenu(user);
                } else {
                    // If sign in fails, display a message to the user.
                    String error = task.getException().getMessage().toString();
                    int errorCode = task.getException().hashCode();
                    Log.w(TAG, "signInWithEmail:failure__" + error, task.getException());
                    Log.w(TAG, "signInWithEmail:failure__code" + errorCode, task.getException().getCause());
                    Toast.makeText(context, "Authentication failed.", Toast.LENGTH_SHORT).show();
                    //updateUI(null);
                    //getDialog().dismiss();
                    pb.setVisibility(View.GONE);
                    pb.hide();
                }
            }
        });
    }

    private void settingMenu(FirebaseUser user) {

        if (user != null) {

            hideProgress();

            Intent intent = new Intent(context, AddProducts.class);
            intent.putExtra("email", email);
            intent.putExtra("bussinesName", bussinesName);
            intent.putExtra("id_user", id_user);
            startActivity(intent);

            getDialog().dismiss();
        }
    }

    private void hideProgress() {
        pb.setVisibility(View.GONE);
        pb.hide();
    }
    private void showProgress() {
        pb.setVisibility(View.VISIBLE);
        pb.show();
    }
}
