package android.raul.caja.productoscrud.views.edit

import android.os.Bundle
import android.raul.caja.productoscrud.R
import android.raul.caja.productoscrud.model.Producto
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_producto_edit.*

class ProductoEditActivity : AppCompatActivity(), ProductoEditActivityView {

    private val TAG: String? = ProductoEditActivity::class.java.simpleName
    private var productoEditActivityViewPresenter: ProductoEditActivityPresenter? = null
    private lateinit var producto: Producto

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_producto_edit)
        initPresenter()
        onAttachView()
        doLoadData()
        initialListener()
        title = "Editar Producto"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun doLoadData() {
        val intentArgs = intent
        this.producto = intentArgs.getSerializableExtra("producto") as Producto
        text_input_edit_cod_producto.let { it.setText(producto.codProd.toString()) }
        text_input_edit_nombre_producto.let { it.setText(producto.NombreProd) }
        text_input_edit_cantidad_producto.let { it.setText(producto.cantidad.toString()) }
        text_input_edit_descripcion.let { it.setText(producto.Descripcion) }
    }

    private fun initialListener() {
        floating_action_button_save_activity_student_edit.setOnClickListener({
            val codProducto = text_input_edit_cod_producto.text.toString()
            val nombreProducto = text_input_edit_nombre_producto.text.toString()
            val cantidad = text_input_edit_cantidad_producto.text.toString()
            val descripcion = text_input_edit_descripcion.text.toString()
            val producto = Producto(codProd = Integer.parseInt(codProducto),
                    NombreProd = nombreProducto,
                    cantidad = Integer.parseInt(cantidad),
                    Descripcion = descripcion)
            productoEditActivityViewPresenter?.onUpdateData(this, producto.codProd.toString(), producto)
        })
    }

    private fun initPresenter() {
        productoEditActivityViewPresenter = ProductoEditActivityPresenter()
    }

    override fun onAttachView() {
        productoEditActivityViewPresenter?.onAttach(this)
    }

    override fun onDetachView() {
        productoEditActivityViewPresenter?.onDetach()
    }

    override fun onDestroy() {
        super.onDestroy()
        onDetachView()
    }

    override fun updateData() {
        Toast.makeText(this, "Producto actualizado!", Toast.LENGTH_LONG)
                .show()
        finish()
    }

    override fun updateDataFailed() {
        Toast.makeText(this, "No se actualizó el producto!", Toast.LENGTH_LONG)
                .show()
    }
}
