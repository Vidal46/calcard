package br.com.calcard.android.app.di

import android.app.Application
import android.content.Context
import br.com.calcard.android.app.MyApplication
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule(private var application: MyApplication) {

    @Provides
    @Singleton
    fun provideApplication(): Application {
        return application
    }

    @Provides
    @Singleton
    fun provideContext(): Context {
        return application
    }
}
