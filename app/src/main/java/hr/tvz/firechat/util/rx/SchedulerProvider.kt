package hr.tvz.firechat.util.rx

import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

// TODO: https://github.com/jaredsburrows/android-gif-example/blob/master/src/main/java/com/burrowsapps/example/gif/SchedulerProvider.kt
// BasePresenter inject?
class SchedulerProvider : BaseSchedulerProvider {

    override fun io(): Scheduler = Schedulers.io()

    override fun ui(): Scheduler = AndroidSchedulers.mainThread()
}