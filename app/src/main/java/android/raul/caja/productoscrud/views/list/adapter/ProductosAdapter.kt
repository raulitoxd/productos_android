package android.raul.caja.productoscrud.views.list.adapter

import android.content.Context
import android.raul.caja.productoscrud.R
import android.raul.caja.productoscrud.model.Producto
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

class ProductoAdapter(
        context: Context,
        textViewResourceId: Int,
        private val items: ArrayList<Producto>) : ArrayAdapter<Producto>(context, textViewResourceId, items) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var v = convertView
        if (v == null) {
            v = LayoutInflater.from(parent?.context)
                    .inflate(R.layout.item_producto_recycler_view_list_productos_activity, null)
        }
        val itemView = items[position]
        if (itemView != null) {
            var textViewCodigoProductoViewHolderProducto: TextView = v?.findViewById(R.id.text_view_codProducto) as TextView
            var textViewNombreProductoViewHolderProducto: TextView = v.findViewById(R.id.text_view_nombProducto) as TextView
            var textViewCantidadViewHolderProducto: TextView = v.findViewById(R.id.text_view_cantidadProducto) as TextView
            var textViewDescripcionViewHolderProducto: TextView = v.findViewById(R.id.text_view_descripcion) as TextView
            textViewCodigoProductoViewHolderProducto.text = itemView.codProd.toString()
            textViewNombreProductoViewHolderProducto.text = itemView.NombreProd
            textViewCantidadViewHolderProducto.text = itemView.cantidad.toString()
            textViewDescripcionViewHolderProducto.text = itemView.Descripcion

        }
        return v!!
    }
}