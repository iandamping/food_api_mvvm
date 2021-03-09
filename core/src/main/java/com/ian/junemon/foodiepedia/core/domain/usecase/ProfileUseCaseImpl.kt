package com.ian.junemon.foodiepedia.core.domain.usecase

import android.content.Intent
import androidx.lifecycle.LiveData
import com.ian.junemon.foodiepedia.core.data.datasource.remote.firebaseuser.AuthenticatedUserInfo
import com.ian.junemon.foodiepedia.core.domain.repository.ProfileRepository
import com.ian.junemon.foodiepedia.core.domain.model.ProfileResults
import javax.inject.Inject

/**
 * Created by Ian Damping on 27,January,2020
 * Github https://github.com/iandamping
 * Indonesia.
 */
class ProfileUseCaseImpl @Inject constructor(private val repo: ProfileRepository) : ProfileUseCase {

    override fun getUserProfile(): LiveData<ProfileResults<AuthenticatedUserInfo>> =
        repo.getUserProfile()

    override suspend fun initSignIn(): Intent = repo.initSignIn()

    override suspend fun initLogout(onComplete: () -> Unit) = repo.initLogout(onComplete)
}