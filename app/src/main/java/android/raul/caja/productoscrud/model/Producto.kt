package android.raul.caja.productoscrud.model

import java.io.Serializable

data class Producto(
        val codProd: Int,
        var NombreProd: String,
        var Descripcion: String,
        val cantidad: Int
        ) : Serializable
