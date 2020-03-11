package br.com.calcard.android.app.di

import br.com.calcard.android.app.api.AppService
import br.com.calcard.android.app.features.cardblock.CardBlockDomainModel
import br.com.calcard.android.app.features.cardblock.CardBlockMVVM
import br.com.calcard.android.app.features.cardblock.CardBlockViewModel
import br.com.calcard.android.app.features.firstunlockcard.FirstUnlockCardDomainModel
import br.com.calcard.android.app.features.firstunlockcard.FirstUnlockCardMVVM
import br.com.calcard.android.app.features.firstunlockcard.FirstUnlockCardViewModel
import br.com.calcard.android.app.features.goals.GoalsDomainModel
import br.com.calcard.android.app.features.goals.GoalsMVVM
import br.com.calcard.android.app.features.goals.GoalsViewModel
import br.com.calcard.android.app.features.unlockcard.UnlockCardDomainModel
import br.com.calcard.android.app.features.unlockcard.UnlockCardMVVM
import br.com.calcard.android.app.features.unlockcard.UnlockCardViewModel
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class MVVMModule {

    @Provides
    @Singleton
    fun provideGoalsViewModel(model: GoalsMVVM.DomainModel): GoalsMVVM.ViewModel {
        return GoalsViewModel(model)
    }

    @Provides
    @Singleton
    fun provideGoalsDomainModel(appService: AppService): GoalsMVVM.DomainModel {
        return GoalsDomainModel(appService)
    }

    @Provides
    @Singleton
    fun provideCardBlockViewModel(model: CardBlockMVVM.DomainModel): CardBlockMVVM.ViewModel {
        return CardBlockViewModel(model)
    }

    @Provides
    @Singleton
    fun provideCardBlockDomainModel(appService: AppService): CardBlockMVVM.DomainModel {
        return CardBlockDomainModel(appService)
    }

    @Provides
    @Singleton
    fun provideFirstUnlockCardViewModel(model: FirstUnlockCardMVVM.DomainModel): FirstUnlockCardMVVM.ViewModel {
        return FirstUnlockCardViewModel(model)
    }

    @Provides
    @Singleton
    fun provideFirstUnlockCardDomainModel(appService: AppService): FirstUnlockCardMVVM.DomainModel {
        return FirstUnlockCardDomainModel(appService)
    }

    @Provides
    @Singleton
    fun provideUnlockCardViewModel(model: UnlockCardMVVM.DomainModel): UnlockCardMVVM.ViewModel {
        return UnlockCardViewModel(model)
    }

    @Provides
    @Singleton
    fun provideUnlockCardDomainModel(appService: AppService): UnlockCardMVVM.DomainModel {
        return UnlockCardDomainModel(appService)
    }
}