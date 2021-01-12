package com.ian.junemon.foodiepedia.core.domain.usecase

import android.net.Uri
import androidx.lifecycle.LiveData
import com.ian.junemon.foodiepedia.core.domain.model.FoodCacheDomain
import com.ian.junemon.foodiepedia.core.domain.model.FoodRemoteDomain
import com.ian.junemon.foodiepedia.core.domain.model.SavedFoodCacheDomain
import com.ian.junemon.foodiepedia.core.domain.model.FirebaseResult
import com.ian.junemon.foodiepedia.core.domain.model.Results
import kotlinx.coroutines.flow.Flow

/**
 * Created by Ian Damping on 03,January,2021
 * Github https://github.com/iandamping
 * Indonesia.
 */
interface FoodUseCase {

    fun getCache(): Flow<Results<List<FoodCacheDomain>>>

    fun getCategorizeCache(foodCategory: String): Flow<Results<List<FoodCacheDomain>>>

    fun getSavedDetailCache(): Flow<Results<List<SavedFoodCacheDomain>>>

    fun uploadFirebaseData(data: FoodRemoteDomain, imageUri: Uri): LiveData<FirebaseResult<Nothing>>

    suspend fun setCacheDetailFood(vararg data: SavedFoodCacheDomain)

    suspend fun deleteSelectedId(selectedId: Int)

    fun loadSharedPreferenceFilter(): Flow<String>

    suspend fun setSharedPreferenceFilter(data: String)
}