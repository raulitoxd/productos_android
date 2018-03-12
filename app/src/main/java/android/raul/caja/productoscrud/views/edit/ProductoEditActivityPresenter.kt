package android.raul.caja.productoscrud.views.edit

import android.content.Context
import android.raul.caja.productoscrud.model.Producto
import android.raul.caja.productoscrud.views.base.Presenter
import android.raul.caja.productoscrud.views.base.View
import android.raul.caja.productoscrud.views.helper.DBHelper

class ProductoEditActivityPresenter : Presenter<ProductoEditActivityView> {

    private val TAG: String? = ProductoEditActivityPresenter::class.java.simpleName
    private var productoEditActivityView: ProductoEditActivityView? = null
    private lateinit var dbHelper: DBHelper
    private lateinit var context: Context

    override fun onAttach(view: View) {
        productoEditActivityView = view as ProductoEditActivityView
    }

    override fun onDetach() {
        productoEditActivityView = null
    }

    fun onUpdateData(context: Context, codProducto: String, producto: Producto) {
        initDbHelper(context)
        val execUpdateData = dbHelper.updateData(codProducto, producto)
        if (execUpdateData) {
            productoEditActivityView?.updateData()
        } else {
            productoEditActivityView?.updateDataFailed()
        }
    }

    private fun initDbHelper(context: Context) {
        dbHelper = DBHelper(context)
    }

}