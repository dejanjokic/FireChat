package hr.tvz.firechat

import android.app.Application
import com.jakewharton.threetenabp.AndroidThreeTen
import hr.tvz.firechat.di.component.DaggerMainComponent
import hr.tvz.firechat.di.component.MainComponent
import hr.tvz.firechat.di.module.AppModule
import hr.tvz.firechat.util.log.CustomTimberTree
import timber.log.Timber

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) Timber.plant(CustomTimberTree())

        AndroidThreeTen.init(this)
    }

    val component: MainComponent
        get() = DaggerMainComponent.builder()
            .appModule(AppModule(this))
            .build()
}