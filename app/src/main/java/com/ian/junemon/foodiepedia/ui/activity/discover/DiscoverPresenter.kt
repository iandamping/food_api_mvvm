package com.ian.junemon.foodiepedia.ui.activity.discover

import androidx.lifecycle.Observer
import com.ian.junemon.foodiepedia.base.BasePresenter
import com.ian.junemon.foodiepedia.base.OnComplete
import com.ian.junemon.foodiepedia.base.OnShowCategoryFoodDetail
import com.ian.junemon.foodiepedia.data.viewmodel.AllFoodCategoryViewModel

/**
 *
Created by Ian Damping on 06/05/2019.
Github = https://github.com/iandamping
 */

class DiscoverPresenter(private val vm: AllFoodCategoryViewModel) : BasePresenter<DiscoverView>() {

    override fun onCreate() {
        view()?.initView()
        vm.getAllFoodCategoryDetail()
        funInitData()
    }

    private fun funInitData() {
        vm.liveDataState.observe(getLifeCycleOwner(), Observer {
            when (it) {
                is OnShowCategoryFoodDetail -> view()?.onShowDefaultFoodCategory(it.data)
                is OnComplete -> setDialogShow(it.show)
            }
        })
    }
}