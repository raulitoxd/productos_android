package android.raul.caja.productoscrud.views.base

interface Presenter<T : View> {

    fun onAttach(view: View)

    fun onDetach()

}