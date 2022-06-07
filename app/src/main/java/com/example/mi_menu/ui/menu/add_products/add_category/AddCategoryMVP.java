package com.example.mi_menu.ui.menu.add_products.add_category;

import com.example.mi_menu.data.model.menu.MenuCategory;

import java.io.File;

/**
 * Created by Sánchez Deivis on 07,abril,2022
 */
public interface AddCategoryMVP {

    interface View {
        void showProgress();

        void hideProgress();

        void successAddCategoryToMenu(String message);

        void errorAddCategoryToMenu(String error);
    }

    interface Presenter {
        void addCategoryToMenu(String id_user, File foto, String nameCategory);

        void saveToDataBaseRealTime(String id_user, MenuCategory newCategory);

        void onDetach();
    }
}
