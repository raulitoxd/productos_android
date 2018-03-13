package android.raul.caja.productoscrud.views.list

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.AsyncTask
import android.os.Bundle
import android.raul.caja.productoscrud.R
import android.raul.caja.productoscrud.model.Producto
import android.raul.caja.productoscrud.model.RestResponse
import android.raul.caja.productoscrud.views.edit.ProductoEditActivity
import android.raul.caja.productoscrud.views.helper.DBHelper
import android.raul.caja.productoscrud.views.list.adapter.ProductoAdapter
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import com.baoyz.swipemenulistview.SwipeMenu
import com.baoyz.swipemenulistview.SwipeMenuCreator
import com.baoyz.swipemenulistview.SwipeMenuItem
import com.baoyz.swipemenulistview.SwipeMenuListView
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.FuelError
import kotlinx.android.synthetic.main.activity_list_productos.*


class ListProductosActivity : AppCompatActivity(), ListProductosActivityView {

    var listProductosActivityPresenter: ListProductosActivityPresenter? = null

    fun initPresenter() {
        listProductosActivityPresenter = ListProductosActivityPresenter()
    }

    override fun onAttachView() {
        listProductosActivityPresenter?.onAttach(this)
    }

    override fun onDetachView() {
        TODO("not implemented")
    }

    override fun eliminarProducto() {
        Toast.makeText(this, "Producto eliminado!", Toast.LENGTH_LONG)
                .show()
    }

    override fun clickEliminar(producto: Producto) {
        Log.d("Eliminando producto ", producto.NombreProd)
        val result = listProductosActivityPresenter?.eliminarProducto(producto)
        if (result == true) {
            doLoadData()
            eliminarProducto()
        }

    }

    //Comunicandome con ProductoEditActivity seteando en Intent
    override fun clickEditar(producto: Producto) {
        startActivity(Intent(this, ProductoEditActivity::class.java)
                .putExtra("producto", producto))
    }

    override fun cargarProductos(adapterDataProductoRecyclerView: ProductoAdapter) {
        Log.d("cargando productos", "loadData view")

        val creator = SwipeMenuCreator { menu ->

            val openItem = SwipeMenuItem(
                    applicationContext)
            openItem.background = ColorDrawable(Color.rgb(0x00, 0x66,
                    0xff))
            openItem.width = 170
            openItem.title = "Editar"
            openItem.titleSize = 18
            openItem.titleColor = Color.WHITE
            menu.addMenuItem(openItem)

            val deleteItem = SwipeMenuItem(
                    applicationContext)
            deleteItem.title = "Eliminar"
            deleteItem.titleSize = 18
            deleteItem.titleColor = Color.WHITE
            deleteItem.background = ColorDrawable(Color.rgb(0xF9,
                    0x3F, 0x25))
            deleteItem.width = 170
            menu.addMenuItem(deleteItem)
        }
        recycler_view_data_producto_activity_listProductos.setMenuCreator(creator);
        recycler_view_data_producto_activity_listProductos.setOnMenuItemClickListener(
                object : SwipeMenuListView.OnMenuItemClickListener {
                    override fun onMenuItemClick(position: Int, menu: SwipeMenu, index: Int): Boolean {
                        println("estoy usando el menu " + menu.getMenuItem(index).title)
                        val producto = listProductosActivityPresenter!!.listaProductos[position];
                        when (index) {
                            0 -> {
                                println("Se editara el producto " + producto.NombreProd)
                                clickEditar(producto);
                            }
                            1 -> {
                                clickEliminar(producto)
                            }
                        }
                        return false
                    }
                })
        recycler_view_data_producto_activity_listProductos.adapter = adapterDataProductoRecyclerView
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_productos)

        LoadProductosAsyncTask().execute(this.applicationContext);
        initPresenter();
        onAttachView()

    }

    override fun onResume() {
        doLoadData()
        if (listProductosActivityPresenter?.listaProductos!!.isEmpty()) {
            onResume()
        }
        super.onResume()
    }

    private fun doLoadData() {
        listProductosActivityPresenter?.onLoadData(this)
    }

    class LoadProductosAsyncTask : AsyncTask<Context, Void, List<Producto>>() {
        var reIntentos: Int = 0;
        private lateinit var context: Context

        override fun doInBackground(vararg params: Context?): List<Producto> {

            context = params[0] as Context
            val sharedPreferences = context.getSharedPreferences("productos_preferences", 0)
            var dbHelper = DBHelper(context)
            var restResponse: RestResponse = RestResponse("", "", ArrayList<Producto>())
            val CALL_REST = "CALL_REST"
            if (sharedPreferences.contains(CALL_REST)) {
                return dbHelper.getAllData()
            }

            val get = Fuel.get("http://api.myjson.com/bins/12d94h")
            var exito: Boolean = false
            do {
                try {
                    var responseObject = get.responseObject(RestResponse.Deserializer())
                    restResponse = responseObject.third.get()
                    exito = true

                } catch (fuelError: FuelError) {
                    fuelError.printStackTrace()
                } finally {
                    println(String.format("Intentando consumir rest por %d vez", ++reIntentos))
                }
            } while (reIntentos < 3 && !exito)

            restResponse.listaprod.all { producto ->
                dbHelper.insertData(producto)
            }
            with(sharedPreferences.edit()) {
                putInt(CALL_REST, 1)
                commit()
            }
            return restResponse.listaprod;
        }

        override fun onPostExecute(result: List<Producto>?) {
            super.onPostExecute(result)
            println("onPostExecute")
            println(result)
        }
    }

}
