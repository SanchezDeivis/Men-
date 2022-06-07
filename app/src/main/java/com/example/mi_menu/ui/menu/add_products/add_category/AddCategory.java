package com.example.mi_menu.ui.menu.add_products.add_category;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.widget.ContentLoadingProgressBar;

import com.example.mi_menu.R;
import com.example.mi_menu.util.FileUtils;
import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AddCategory extends AppCompatActivity implements AddCategoryMVP.View {


    private static final String TAG = AddCategory.class.getSimpleName();
    @BindView(R.id.iv_image_category)
    ImageView imageMenuCategory;

    @BindView(R.id.fly_add_category_main)
    FrameLayout mainFragment;

    @BindView(R.id.et_name_category)
    EditText et_name_category;

    @BindView(R.id.iv_add_image)
    ImageView btnAddImage;

    @BindView(R.id.btn_back)
    ImageButton btn_back;

    @BindView(R.id.btn_add_category)
    Button btn_add_category;

    @BindView(R.id.pb)
    ContentLoadingProgressBar pb;

    private Context context;
    private final int REQUEST_PERMISSIONS = 100;
    public static final int SELECT_PICTURE = 5253;
    SharedPreferences sharedPreferences;
    private String id_user = "";
    private AddCategoryMVP.Presenter presenter;
    private File fileSelect;
    private Intent intent;

    public AddCategory() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedPreferences = getSharedPreferences("VALUES", MODE_PRIVATE);
        int theme = sharedPreferences.getInt("THEME", 0);

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

        setContentView(R.layout.activity_add_category);
        ButterKnife.bind(this, this);
        this.context = this;
        this.presenter = new AddCategoryPresenter(this, context);


        loadViews();
        // Obtenemos el Intent
        this.intent = getIntent();
        if (intent != null && intent.getExtras() != null) {
            try {
                Bundle extras = intent.getExtras();
                id_user = extras.getString("id_user");

            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
                e.printStackTrace();
            }
        }

    }


    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    public void loadViews() {

        btn_back.setOnClickListener(view -> {
            onBackPressed();
        });

        btn_add_category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nameCategory = et_name_category.getText().toString();

                String completeData = validateDatas(id_user, nameCategory, fileSelect);

                if (completeData.equalsIgnoreCase("OK")) {
                    addNewCategoryToMenu(id_user, nameCategory, fileSelect);
                } else {
                    showInfoDialog("Atención", completeData,false);
                }
            }
        });

        btnAddImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkPermissions()) {
                    // Permission is already available, start camera preview
                    Snackbar.make(mainFragment,
                            R.string.permission_available,
                            Snackbar.LENGTH_SHORT).show();
                    showOptions();
                } else {
                    // Permission is missing and must be requested.
                    requestPermissionStorage();
                }
            }
        });

    }

    private String validateDatas(String id_user, String nameCategory, File fileSelect) {

        if (id_user==null||id_user.isEmpty())
            return "No se detectó su ID de usuario, intente iniciar sesión nuevamente.";

        if (nameCategory.isEmpty()) return "Ingrese el nombre de la categoría.";

        if (fileSelect == null) return "Selecciona una imagen para la categoría";

        return "OK";
    }

    private void addNewCategoryToMenu(String id_user, String nameCategory, File fileSelect) {
        if (presenter != null) {
            presenter.addCategoryToMenu(id_user, fileSelect, nameCategory);
        }
    }

    private void showOptions() {

        final CharSequence[] option = {"Imagenes", "Videos", "Gif", "Cancelar"};
        final android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);
        builder.setTitle("Eleige una opción");

        builder.setItems(option, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                String mimeType = "";

                if (option[which] == "Imagenes") {
                    mimeType = "image/*";
                    selectStorageFile(mimeType);
                } else if (option[which] == "Videos") {
                    mimeType = "video/*";
                    selectStorageFile(mimeType);
                } else if (option[which] == "Gif") {
                    mimeType = "image/gif";
                    selectStorageFile(mimeType);
                } else {
                    dialog.dismiss();
                }
            }
        });

        builder.show();
    }

    private void selectStorageFile(String mimeType) {
        Intent intent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        intent.setType(mimeType);
        startActivityForResult(intent.createChooser(intent, "Selecciona app de imagen"), SELECT_PICTURE);
    }

    private boolean checkPermissions() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int resultCamera = context.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE);
            int resultStorage = context.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);

            return (resultCamera == PackageManager.PERMISSION_GRANTED &&
                    resultStorage == PackageManager.PERMISSION_GRANTED);

        }
        return false;
    }

    private void requestPermissionStorage() {
        // Permission has not been granted and must be requested.
        if ((ActivityCompat.shouldShowRequestPermissionRationale(AddCategory.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) ||
                ActivityCompat.shouldShowRequestPermissionRationale(AddCategory.this,
                        Manifest.permission.READ_EXTERNAL_STORAGE))) {

            AlertDialog.Builder builder = new AlertDialog.Builder(context);

            builder.setCancelable(false)
                    .setTitle(context.getResources().getString(R.string.attention))
                    .setMessage(context.getResources().getString(R.string.grant_storage_permission))
                    .setPositiveButton(context.getResources().getString(R.string.action_accept), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            // Request the permission
                            ActivityCompat.requestPermissions(AddCategory.this, new String[]{
                                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                    Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_PERMISSIONS);
                        }
                    })
                    .setNegativeButton(R.string.action_cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });

            AlertDialog dialog = builder.create();
            dialog.show();

        } else {
            // Request the permission. The result will be received in onRequestPermissionResult().
            showPermissionSettings(context.getResources().getString(R.string.settins_grant_storage_permission));
        }
    }

    private void showPermissionSettings(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setCancelable(false)
                .setTitle(context.getResources().getString(R.string.attention))
                .setMessage(message)
                .setPositiveButton(context.getResources().getString(R.string.action_settings), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent();
                        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", context.getPackageName(), null);
                        intent.setData(uri);
                        startActivity(intent);
                    }
                })
                .setNegativeButton(R.string.action_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {

            case REQUEST_PERMISSIONS: {
                boolean storagePermissionGranted = true;

                for (int grantResult : grantResults) {
                    storagePermissionGranted &= grantResult == PackageManager.PERMISSION_GRANTED;
                }

                if (storagePermissionGranted) {
                    Toast.makeText(context, "Permisos aceptados", Toast.LENGTH_SHORT).show();
                } else {
                    showPermissionSettings(context.getResources().getString(
                            R.string.settins_grant_storage_permission));
                }
                break;
            }
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {

            case SELECT_PICTURE:
                if (data != null) {
                    Uri path = data.getData();
                    fileSelect = FileUtils.getFile(context, path);
                    // = new File(Objects.requireNonNull(data.getDataString()));
                    imageMenuCategory.setImageURI(path);
                    //updateAvatar(fileSelect);
                }
                break;

        }
    }

    public void showInfoDialog(String title, String message, boolean succeedStatus) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title)
                .setMessage(message)
                .setPositiveButton("ACEPTAR", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        if (succeedStatus){
                            Intent intent = new Intent();
                            intent.putExtra("result",RESULT_OK);
                            setResult(RESULT_OK,intent);
                            finish();
                        }
                        dialogInterface.dismiss();
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }


    public void checkPasswordUSerAdminSuccess() {

        /*EntitiesInfoPatientFragment entitiesInfoPatientFragment = new EntitiesInfoPatientFragment();
        Bundle args = new Bundle();
        args.putBoolean(EntitiesInfoPatientFragment.DATA_RECEIVE + "_user_exist", true);

        entitiesInfoPatientFragment.setArguments(args);
        assert (getActivity()) != null;
        (getActivity()).getSupportFragmentManager().beginTransaction()
                .replace(R.id.sign_up_fl_main, entitiesInfoPatientFragment)
                .setTransition(FragmentTransaction.TRANSIT_ENTER_MASK).addToBackStack(null)
                .commit();*/
    }

    @Override
    public void showProgress() {
        pb.setVisibility(View.VISIBLE);
        pb.show();
    }

    @Override
    public void hideProgress() {
        pb.setVisibility(View.GONE);
        pb.hide();
    }

    @Override
    public void successAddCategoryToMenu(String message) {
        if (message!=null&&!message.isEmpty())
            showInfoDialog("Atención", message,true);
        else {
            showInfoDialog("Atención", "Ocurrió un problema intenta nuevamente",false);
        }
    }

    @Override
    public void errorAddCategoryToMenu(String error) {
        if (error!=null&&!error.isEmpty())
        showInfoDialog("Atención", error,false);
        else {
            showInfoDialog("Atención", "Ocurrió un problema intenta nuevamente",false);
        }
    }
}
