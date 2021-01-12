package com.ian.junemon.foodiepedia.data.datasource.remote

import android.content.Intent
import com.ian.junemon.foodiepedia.core.data.data.datasource.ProfileRemoteDataSource
import com.ian.junemon.foodiepedia.core.domain.model.UserProfileDataModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

/**
 * Created by Ian Damping on 06,February,2020
 * Github https://github.com/iandamping
 * Indonesia.
 */
class FakeProfileRemoteDataSourceImpl(var fakeProfile: UserProfileDataModel):ProfileRemoteDataSource {

    override suspend fun getUserProfile(): Flow<UserProfileDataModel> {
       return flowOf(fakeProfile)
    }

    override suspend fun initSignIn(): Intent {
       return Intent()
    }

    override suspend fun initLogout() {

    }
}