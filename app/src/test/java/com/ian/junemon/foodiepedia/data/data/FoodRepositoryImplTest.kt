package com.ian.junemon.foodiepedia.data.data

import com.google.common.truth.Truth.assertThat
import com.ian.junemon.foodiepedia.core.data.data.repository.FoodRepositoryImpl
import com.ian.junemon.foodiepedia.data.datasource.cache.FakeFoodCacheDataSourceImpl
import com.ian.junemon.foodiepedia.data.datasource.remote.FakeFoodRemoteDataSourceImpl
import com.ian.junemon.foodiepedia.core.domain.repository.FoodRepository
import com.junemon.model.DataHelper
import com.junemon.model.data.dto.mapRemoteToCacheDomain
import com.junemon.model.data.dto.mapToCacheDomain
import com.junemon.model.domain.FoodRemoteDomain
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

/**
 * Created by Ian Damping on 05,February,2020
 * Github https://github.com/iandamping
 * Indonesia.
 */
class FoodRepositoryImplTest {

    private val fakeRemoteData1 = FoodRemoteDomain().apply {
        foodName = "remote"
        foodCategory = "remote"
        foodArea = "remote"
        foodImage = "remote"
        foodContributor = "remote"
        foodDescription = "remote"
    }
    private val fakeRemoteData2 = FoodRemoteDomain().apply {
        foodName = "remote2"
        foodCategory = "remote2"
        foodArea = "remote2"
        foodImage = "remote2"
        foodContributor = "remote2"
        foodDescription = "remote"
    }
    private val fakeRemoteData3 = FoodRemoteDomain().apply {
        foodName = "remote3"
        foodCategory = "remote3"
        foodArea = "remote3"
        foodImage = "remote3"
        foodContributor = "remote3"
        foodDescription = "remote"
    }

    private val listOfFakeRemote = listOf(fakeRemoteData1, fakeRemoteData2, fakeRemoteData3)
    private lateinit var cacheDataSource: FakeFoodCacheDataSourceImpl
    private lateinit var remoteDataSource: FakeFoodRemoteDataSourceImpl
    private lateinit var foodRepository: FoodRepository

    @Before
    fun createRepository() {
        cacheDataSource =
            FakeFoodCacheDataSourceImpl()
        remoteDataSource =
            FakeFoodRemoteDataSourceImpl(
                listOfFakeRemote.toMutableList()
            )
        // Get a reference to the class under test
        foodRepository = FoodRepositoryImpl(
            Dispatchers.Unconfined,
            remoteDataSource, cacheDataSource
        )
    }

    @Test
    @ExperimentalCoroutinesApi
    fun repositoryFoodPrefetch() = runBlocking {
        // Trigger the repository to load data that loads from remote
        val responseStatus = remoteDataSource.getFirebaseData()

        val job = launch {
            responseStatus.take(1).collect { data ->
                when (data) {
                    is DataHelper.RemoteSourceValue -> {
                        //data should match because we dont do anything
                        assertThat(data.data).hasSize(3)
                        assertThat(data.data[0]).isEqualTo(fakeRemoteData1)
                        assertThat(data.data[1]).isEqualTo(fakeRemoteData2)
                        assertThat(data.data[2]).isEqualTo(fakeRemoteData3)
                    }
                }

            }
        }
        job.cancel()
    }

    @Test
    fun repositoryFoodPrefetch_failScenario() = runBlocking {
        // Trigger the repository to load null data from remote
        remoteDataSource =
            FakeFoodRemoteDataSourceImpl(
                null
            )
        val responseStatus = remoteDataSource.getFirebaseData()
        val job = launch {
            responseStatus.collect { data ->
                assertThat(data).isInstanceOf(DataHelper.RemoteSourceError::class.java)
            }
        }
        job.cancel()
    }

    @Test
    @ExperimentalCoroutinesApi
    fun repositoryFoodGetCache() = runBlocking {
        val remoteData = remoteDataSource.getFirebaseData()
        val cacheData = cacheDataSource.getCache()
        val jobs = launch {
            // Trigger the repository to save data from remote
            remoteData.take(1).collect { data ->
                when (data) {
                    is DataHelper.RemoteSourceValue -> {
                        //data should match because we dont do anything
                        assertThat(data.data).hasSize(3)
                        assertThat(data.data[0]).isEqualTo(fakeRemoteData1)
                        assertThat(data.data[1]).isEqualTo(fakeRemoteData2)
                        assertThat(data.data[2]).isEqualTo(fakeRemoteData3)
                        cacheDataSource.setCache(*data.data.mapRemoteToCacheDomain().toTypedArray())
                    }
                }

            }
            // Trigger the repository to load data that loads from cache
            cacheData.take(1).collect { data ->
                assertThat(data).hasSize(1)
                assertThat(data[0].foodArea).isEqualTo(fakeRemoteData1.foodArea)
                assertThat(data[0].foodCategory).isEqualTo(fakeRemoteData1.foodCategory)
                assertThat(data[0].foodContributor).isEqualTo(fakeRemoteData1.foodContributor)
                assertThat(data[0].foodImage).isEqualTo(fakeRemoteData1.foodImage)
            }
        }

        jobs.cancel()
    }

    @Test
    @ExperimentalCoroutinesApi
    fun repositoryFoodGetCategorizeCache() = runBlocking {
        // Trigger the repository to load data that loads from remote
        val remoteData = remoteDataSource.getFirebaseData()

        val job = launch {
            remoteData.take(1).collect { data ->
                when (data) {
                    is DataHelper.RemoteSourceValue -> {
                        //data should match because we dont do anything
                        assertThat(data.data).hasSize(3)
                        assertThat(data.data[0]).isEqualTo(fakeRemoteData1)
                        assertThat(data.data[1]).isEqualTo(fakeRemoteData2)
                        assertThat(data.data[2]).isEqualTo(fakeRemoteData3)
                        cacheDataSource.setCache(data.data.toTypedArray()[1].mapToCacheDomain())
                    }
                }
            }
        }

        val job2 = launch{
            cacheDataSource.getCategirizeCache("remote2").take(1).collect { data ->
                assertThat(data[0]).isEqualTo(fakeRemoteData2)
            }
        }
        job2.cancel()
        job.cancel()
    }

}