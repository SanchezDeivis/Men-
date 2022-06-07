package com.example.mi_menu.ui.menu.add_products.add_category;

import android.content.Context;
import android.net.Uri;
import android.os.Build;

import androidx.annotation.NonNull;

import com.example.mi_menu.data.model.menu.MenuCategory;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;

/**
 * Created by Sánchez Deivis on 07,abril,2022
 */
public class AddCategoryPresenter implements AddCategoryMVP.Presenter {

    private Context context;
    private AddCategoryMVP.View view;
    private StorageReference StorageMenuCategoria;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private MenuCategory menuCategory;

    public AddCategoryPresenter(AddCategoryMVP.View view, Context context) {
        this.view = view;
        this.context = context;
        //FirebaseApp.initializeApp(context);
        this.StorageMenuCategoria = FirebaseStorage.getInstance().getReference();
        this.firebaseDatabase = FirebaseDatabase.getInstance();
        this.databaseReference = firebaseDatabase.getReference();
    }

    @Override
    public void addCategoryToMenu(String id_user, File foto, String nameCategory) {

        if (view != null) view.showProgress();

        Uri UriFoto = null;
        File fileLocal = foto;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            try {

                UriFoto = Uri.fromFile(fileLocal);

            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            UriFoto = Uri.fromFile(fileLocal);
        }

        final StorageReference ref = StorageMenuCategoria.child("Menu_Categoria").
                child(getNameRutaFoto(fileLocal.getAbsolutePath()));

        final UploadTask uploadTask = ref.putFile(UriFoto);
        final StorageReference finalFilePathFoto = ref;

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                String message = e.getMessage().toString();
                /*DetailsView.CrearToast("Error: " + message);*/
                if (view != null) {
                    view.hideProgress();
                    view.errorAddCategoryToMenu(message);
                }
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                // mensajeDeCargasDeImagenes

                Task<Uri> urLTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful()) {
                            throw task.getException();
                        }
                        return finalFilePathFoto.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {

                        if (task.isSuccessful()) {
                            String gotUrl = task.getResult().toString();
                            int i = 2;
                            System.out.println("getUrlFoto " + gotUrl);
                            menuCategory = new MenuCategory(gotUrl, nameCategory);

                            saveToDataBaseRealTime(id_user, menuCategory);

                        }
                    }
                });
            }
        });

    }

    @Override
    public void saveToDataBaseRealTime(String id_user, MenuCategory newCategory) {

        try {
            databaseReference.child("usuario")
                    .child(id_user)
                    .child("Menu_Categoria")
                    .child(newCategory.getName()).setValue(newCategory);

            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {

                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (view != null) {
                        view.hideProgress();
                        view.successAddCategoryToMenu("Categoría agregada exitosamente");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    if (view != null) {
                        view.hideProgress();
                        view.errorAddCategoryToMenu(databaseError.getMessage());
                    }
                }
            });

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public String getNameRutaFoto(String Ruta) {
        String nombre = "";
        int cant = 0;
        try {
            for (int i = 0; i < Ruta.length(); i++) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    if (cant >= 9) {
                        nombre += Ruta.charAt(i);

                    } else {
                        if (Ruta.charAt(i) == '/') {
                            cant++;
                        }
                    }
                } else {
                    if (cant >= 7) {
                        nombre += Ruta.charAt(i);

                    } else {
                        if (Ruta.charAt(i) == '/') {
                            cant++;
                        }
                    }
                }
            }

        } catch (Exception e) {
            return Ruta;
        }

        return nombre;
    }

    @Override
    public void onDetach() {

    }
}
