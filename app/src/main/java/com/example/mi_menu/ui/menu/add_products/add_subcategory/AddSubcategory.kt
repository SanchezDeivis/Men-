package com.example.mi_menu.ui.menu.add_products.add_subcategory

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.mi_menu.R

class AddSubcategory : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_subcategory)
        Toast.makeText(this, "Debemos configurar para agregar la nueva subcategoria del menu.", Toast.LENGTH_SHORT).show()
    }
}