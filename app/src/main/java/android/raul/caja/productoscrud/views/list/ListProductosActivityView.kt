package android.raul.caja.productoscrud.views.list

import android.raul.caja.productoscrud.model.Producto
import android.raul.caja.productoscrud.views.base.View
import android.raul.caja.productoscrud.views.list.adapter.ProductoAdapter

interface ListProductosActivityView : View {
    fun cargarProductos(adapterDataProductoRecyclerView: ProductoAdapter)
    fun eliminarProducto()
    fun clickEliminar(producto: Producto)
    fun clickEditar(producto: Producto)
}

