package com.example.mi_menu.data.model.menu;

/**
 * Created by Sánchez Deivis on 08,abril,2022
 */
public class MenuCategory {

    private String image;
    private String name;

    public MenuCategory() {
    }

    public MenuCategory(String image, String name) {
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
