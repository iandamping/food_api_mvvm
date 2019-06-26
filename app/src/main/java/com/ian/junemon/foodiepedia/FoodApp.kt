package com.ian.junemon.foodiepedia

import android.app.Application
import com.crashlytics.android.Crashlytics
import com.google.android.gms.ads.MobileAds
import com.google.firebase.auth.FirebaseAuth
import com.google.gson.Gson
import com.ian.junemon.foodiepedia.di.allRepoModul
import com.ian.junemon.foodiepedia.di.allViewmodelModule
import com.ian.junemon.foodiepedia.di.databaseModule
import com.ian.junemon.foodiepedia.di.networkMod
import com.ian.junemon.foodiepedia.util.PreferenceHelper
import com.ian.junemon.foodiepedia.util.SecretKeyHelper.Admob
import io.fabric.sdk.android.Fabric
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

/**
 *
Created by Ian Damping on 06/05/2019.
Github = https://github.com/iandamping
 */

class FoodApp : Application() {
    private val applicationScope = CoroutineScope(Dispatchers.Default)

    private fun delayedInit(app: Application) {
        applicationScope.launch {
            Fabric.with(app, Crashlytics())
            MobileAds.initialize(app, Admob)
            mFirebaseAuth = FirebaseAuth.getInstance()
            prefHelper = PreferenceHelper(app)
            startKoin {
                androidContext(app)
                modules(listOf(networkMod,allViewmodelModule, allRepoModul, databaseModule))
            }
        }
    }

    companion object {
        val gson: Gson = Gson()
        lateinit var prefHelper: PreferenceHelper
        lateinit var mFirebaseAuth: FirebaseAuth

        init {
            System.loadLibrary("ian")
        }

    }

    override fun onCreate() {
        super.onCreate()
        delayedInit(this)
    }
}