package com.ian.junemon.foodiepedia.foodie.di.module

import androidx.lifecycle.ViewModel
import com.ian.junemon.foodiepedia.core.presentation.di.factory.ViewModelKey
import com.ian.junemon.foodiepedia.foodie.vm.FoodViewModel
import com.ian.junemon.foodiepedia.foodie.vm.ProfileViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

/**
 * Created by Ian Damping on 28,January,2020
 * Github https://github.com/iandamping
 * Indonesia.
 */
@Module
abstract class FoodModule {

    @Binds
    @IntoMap
    @ViewModelKey(FoodViewModel::class)
    abstract fun bindFoodViewModel(foodViewModel: FoodViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ProfileViewModel::class)
    abstract fun bindProfileViewModel(profileViewModel: ProfileViewModel): ViewModel
}