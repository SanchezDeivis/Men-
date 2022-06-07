package com.example.mi_menu.ui.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.ContentLoadingProgressBar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mi_menu.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ResetPasswordActivity extends AppCompatActivity {

    @BindView(R.id.btn_back)
    ImageButton btnBack;

    @BindView(R.id.btn_recovery)
    Button btnRecoveryPass;

    @BindView(R.id.et_email)
    EditText email;

    @BindView(R.id.pb)
    ContentLoadingProgressBar pb;

    private FirebaseAuth mAuth;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedPreferences = getSharedPreferences("VALUES", MODE_PRIVATE);
        int theme = sharedPreferences.getInt("THEME", 0);
        mAuth = FirebaseAuth.getInstance();

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

        setContentView(R.layout.activity_restart_password);

        ButterKnife.bind(this,this);

        mAuth = FirebaseAuth.getInstance();

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
                finish();
            }
        });

        btnRecoveryPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                pb.setVisibility(View.VISIBLE);
                pb.show();

                String strEmail, verifiData;
                strEmail = email.getText().toString();
                verifiData = validateData(strEmail);

                if (verifiData.equalsIgnoreCase("OK")) {
                    resetPassword(strEmail);
                }
                else {
                    pb.setVisibility(View.GONE);
                    pb.hide();
                    Toast.makeText(ResetPasswordActivity.this, verifiData, Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    private String validateData(String strEmail) {

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

        return "OK";
    }

    private void resetPassword(String email) {

        mAuth.setLanguageCode("es");
        mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(ResetPasswordActivity.this, "Revisa tu correo  para restablecer tu password", Toast.LENGTH_LONG).show();

                } else {
                    Toast.makeText(ResetPasswordActivity.this, "No se pudo enviar el correo para restablecer tu password", Toast.LENGTH_SHORT).show();
                }
                pb.setVisibility(View.GONE);
                pb.hide();
            }
        });
    }
}