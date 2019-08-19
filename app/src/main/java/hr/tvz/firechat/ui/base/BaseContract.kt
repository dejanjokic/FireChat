package hr.tvz.firechat.ui.base

interface BaseContract {
    interface View {
        fun showError(errorMessage: String?) {}
        fun showLoading() {}
        fun hideLoading() {}
    }
    interface Presenter<V : View> {
        val isAttached: Boolean
        fun attach(view: V)
        fun detach()
    }
}