package android.raul.caja.productoscrud.model

import com.github.kittinunf.fuel.core.ResponseDeserializable
import com.google.gson.Gson
import java.io.Serializable

data class RestResponse(
        var status: String,
        var respStatus: String,
        var listaprod: List<Producto>) : Serializable {

    class Deserializer: ResponseDeserializable<RestResponse> {
        override fun deserialize(content: String) = Gson().fromJson(content, RestResponse::class.java)
    }
}