package com.example.mi_menu.ui.menu.products.adapter;

import com.example.mi_menu.data.db.model.ResourcePosts;
import com.example.mi_menu.data.model.menu.Menu_Categoria;

import java.util.List;

/**
 * Created by Sánchez Deivis on 28,enero,2022
 */
public class MenuAdapterPositionUtil {
    private List<ResourcePosts> resourcePosts;
    private List<Menu_Categoria> menuCategoriaList;
    private Menu_Categoria menuCategoria;

    public MenuAdapterPositionUtil() {
    }

    public MenuAdapterPositionUtil(List<ResourcePosts> resourcePosts, List<Menu_Categoria> menuCategoriaList, Menu_Categoria menuCategoria) {
        this.setResourcePosts(resourcePosts);
        this.setMenuDataList(menuCategoriaList);
        this.setMenuData(menuCategoria);
    }

    public List<ResourcePosts> getResourcePosts() {
        return resourcePosts;
    }

    public void setResourcePosts(List<ResourcePosts> resourcePosts) {
        this.resourcePosts = resourcePosts;
    }

    public List<Menu_Categoria> getMenuDataList() {
        return menuCategoriaList;
    }

    public void setMenuDataList(List<Menu_Categoria> menuCategoriaList) {
        this.menuCategoriaList = menuCategoriaList;
    }

    public Menu_Categoria getMenuData() {
        return menuCategoria;
    }

    public void setMenuData(Menu_Categoria menuCategoria) {
        this.menuCategoria = menuCategoria;
    }
}
