package com.example.mi_menu.ui.menu.add_products

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mi_menu.R
import com.example.mi_menu.data.model.menu.MenuCategory
import com.example.mi_menu.data.model.menu.Menu_Categoria
import com.example.mi_menu.ui.menu.add_products.adapter.AddMenuAdapter
import com.example.mi_menu.ui.menu.add_products.add_category.AddCategory
import com.example.mi_menu.ui.menu.add_products.add_subcategory.AddSubcategory
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference


var recyclerView: RecyclerView? = null

var btn_add_category: Button? = null

var btn_back: ImageButton? = null


private var StorageMenuCategoria: StorageReference? = null
private var firebaseDatabase: FirebaseDatabase? = null
private var databaseReference: DatabaseReference? = null
private var mAuth: FirebaseAuth? = null

private val menuCategoryList: List<MenuCategory> = ArrayList()

class AddProducts : AppCompatActivity(),AddMenuAdapter.ItemClickListener {

    private val TAG = javaClass.simpleName

    /*@BindView(R.id.rv_add_menu)
    lateinit var recyclerView: RecyclerView

    @BindView(R.id.btn_add_categorys)
    var btn_add_category: Button? = null

    @JvmField
    @BindView(R.id.btn_back)
    var btn_back: ImageButton? = null*/

    private var addMenuAdapter: AddMenuAdapter? = null
    private val itemListPosts: MutableList<Menu_Categoria> = java.util.ArrayList()
    //private val itemListPosts: List<Menu_Categoria> = java.util.ArrayList()


    // TODO: Rename and change types of parameters
    private var email: String? = null
    private var bussinesName: String? = null
    private var id_user: String? = null
    private var context: Context? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        var sharedPreferences: SharedPreferences? = null

        sharedPreferences = getSharedPreferences("VALUES", MODE_PRIVATE)

        val theme = sharedPreferences.getInt("THEME", 0)

        when (theme) {
            0 -> setTheme(R.style.AppTheme)
            1 -> setTheme(R.style.AppTheme4)
            2 -> setTheme(R.style.AppTheme5)
            3 -> setTheme(R.style.AppTheme6)
            4 -> setTheme(R.style.AppTheme2)
            5 -> setTheme(R.style.AppTheme3)
        }
        setContentView(R.layout.activity_add_products)
        /*ButterKnife.bind(this, this)*/

        val bundle = intent.extras
        email = bundle?.getString("email")
        bussinesName = bundle?.getString("bussinesName")
        id_user = bundle?.getString("id_user")

        recyclerView=findViewById(R.id.rv_add_menu)

        btn_add_category  = findViewById(R.id.btn_add_categorys)

        btn_back = findViewById(R.id.btn_back)

        context = this

        //FirebaseApp.initializeApp(context);
        StorageMenuCategoria = FirebaseStorage.getInstance().reference
        firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = firebaseDatabase!!.reference
        mAuth = FirebaseAuth.getInstance()

        getMenuData()
        loadView()
    }

    private fun loadView() {
        btn_back!!.setOnClickListener { onBackPressed() }

        btn_add_category!!.setOnClickListener {
            val intent = Intent(context, AddCategory::class.java)
            intent.putExtra("email", email)
            intent.putExtra("bussinesName", bussinesName)
            intent.putExtra("id_user", id_user)
            getResult.launch(intent)
        }

        addMenuAdapter = AddMenuAdapter(itemListPosts,this)

        /*addMenuAdapter = AddMenuAdapter(itemListPosts, context!!,
            object : AddMenuAdapter.ItemClickListener {
                override fun onCLick(view: View?, position: Int) {
                    Toast.makeText(context, "Add submenú", Toast.LENGTH_SHORT).show()
                }
            })*/

        // Construimos el recyclerView
        val lLayout = GridLayoutManager(
            context, 2, RecyclerView.VERTICAL, false
        )

        recyclerView!!.adapter = addMenuAdapter
        recyclerView!!.setHasFixedSize(true)
        recyclerView!!.setHasTransientState(true)
        recyclerView!!.layoutManager = lLayout
    }

    // Receiver
    private val getResult = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()) {

        if (it.resultCode == Activity.RESULT_OK) {
            val value = it.data?.getStringExtra("input")
            Toast.makeText(context,"Categoría agregada exitosamente...",Toast.LENGTH_LONG).show()
            getMenuData()
        }

    }

    private fun getMenuData() {
        val menuData: MutableList<Menu_Categoria> = java.util.ArrayList()
        val id = mAuth!!.currentUser!!.uid
        try {
            println("idminusculo $id")
            databaseReference
                ?.child("usuario")
                ?.child(id_user!!)
                ?.child("Menu_Categoria")
                ?.addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        if (dataSnapshot.exists()) {
                            for (objDataSnapshot in dataSnapshot.children) {
                                val newCategory = objDataSnapshot.getValue(
                                    Menu_Categoria::class.java)

                                menuData.add(Menu_Categoria(newCategory!!.image, newCategory.name))
                            }

                            itemListPosts.clear()
                            itemListPosts.addAll(menuData)
                            addMenuAdapter?.notifyDataSetChanged()

                        } else {
                            showInfoDialog(
                                "Atención",
                                "Aun no ha agregado información de sus productos." +
                                        "\nIngresa en ajustes y gestiona tu menú.")
                        }
                    }

                    override fun onCancelled(databaseError: DatabaseError) {}
                })
        } catch (e: Exception) {
            Toast.makeText(
                context,
                "Ocurrió un problema, intente nuevamente",
                Toast.LENGTH_SHORT
            ).show()
        }
        //dialog.dismiss();


        /*itemListPosts.clear();
        itemListPosts.addAll(menuData);

        menuAdapter.addPositionAdapter(1);
        menuAdapter.addMenuList(itemListPosts);

        */
        /*menuAdapter.addAll(itemListPosts);*/ /*
        if (menuAdapter != null)
            menuAdapter.notifyDataSetChanged();
        //viewPagerPosts.getOnFocusChangeListener();*/
    }

    fun showInfoDialog(title: String?, message: String?) {
        val builder = AlertDialog.Builder(
            context!!
        )
        builder.setCancelable(false).setTitle(title)
            .setMessage(message)
            .setPositiveButton(
                "ACEPTAR"
            ) { dialogInterface, i -> dialogInterface.dismiss() }
        val dialog = builder.create()
        dialog.show()
    }

    companion object {
        const val REQUEST_CODE = 1020
    }

    override fun onCLick(data: Menu_Categoria, position: Int) {
        startActivity(Intent(context,AddSubcategory::class.java))
    }
}