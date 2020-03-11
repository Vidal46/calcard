package br.com.calcard.android.app.di

import br.com.calcard.android.app.features.goals.GoalsActivity
import br.com.calcard.android.app.ui.cards.HomeActivity
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, MVVMModule::class, NetworkModule::class])
interface AppComponent {

    fun inject(activity: GoalsActivity)

    fun inject(activity: HomeActivity)

}
