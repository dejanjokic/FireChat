package hr.tvz.firechat.ui.base

import io.reactivex.disposables.CompositeDisposable

open class BasePresenter<V : BaseContract.View> : BaseContract.Presenter<V> {

    var view: V? = null

    var compositeDisposable = CompositeDisposable()

    override val isAttached = view != null

    override fun attach(view: V) {
        this.view = view
    }

    override fun detach() {
        compositeDisposable.clear()
        view = null
    }
}