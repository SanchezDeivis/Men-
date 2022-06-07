package com.example.mi_menu.data.model.menu;

/**
 * Created by Sánchez Deivis on 28,enero,2022
 */
public class Menu_Categoria {

    private String image;
    private String name;

    public Menu_Categoria() {
    }

    public Menu_Categoria(String image, String name) {
        this.setImage (image);
        this.setName(name);
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
