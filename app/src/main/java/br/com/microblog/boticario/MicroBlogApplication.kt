package br.com.microblog.boticario

import android.app.Application
import br.com.microblog.boticario.di.AppInject.appInject
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MicroBlogApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@MicroBlogApplication)
            modules(appInject)
        }
    }
}