package android.raul.caja.productoscrud.views.list

import android.content.Context
import android.raul.caja.productoscrud.R
import android.raul.caja.productoscrud.model.Producto
import android.raul.caja.productoscrud.views.base.Presenter
import android.raul.caja.productoscrud.views.base.View
import android.raul.caja.productoscrud.views.helper.DBHelper
import android.raul.caja.productoscrud.views.list.adapter.ProductoAdapter

class ListProductosActivityPresenter : Presenter<ListProductosActivityView> {

    private var listProductosActivityView: ListProductosActivityView? = null
    private var dbHelper: DBHelper? = null
    private lateinit var context: Context
    private lateinit var productoAdapter: ProductoAdapter
    lateinit var listaProductos: ArrayList<Producto>

    override fun onAttach(view: View) {
        listProductosActivityView = view as ListProductosActivityView
    }

    override fun onDetach() {
        listProductosActivityView = null
    }

    fun eliminarProducto(producto: Producto): Boolean{
        initDbHelper(this.context)
        return dbHelper!!.deleteData(producto)
    }
    fun onLoadData(context: Context) {
        this.context = context
        initDbHelper(context)
        listaProductos = dbHelper?.getAllData()!!
        var adapterProductos = ProductoAdapter(
                context = this.context,
                textViewResourceId = R.id.relative_layout_container_item_data_producto,
                items = listaProductos!!
        )

        listProductosActivityView?.cargarProductos(adapterProductos)
    }

    private fun initDbHelper(context: Context) {
        if (dbHelper == null) {
            dbHelper = DBHelper(context)
        }
    }

}