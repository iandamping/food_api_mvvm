package com.example.junemon.foodapi_mvvm.ui.detailinformation

import android.content.Intent
import androidx.lifecycle.Observer
import com.example.junemon.foodapi_mvvm.base.*
import com.example.junemon.foodapi_mvvm.data.viewmodel.AllFoodListDataViewModel
import com.example.junemon.foodapi_mvvm.util.Constant
import com.example.junemon.foodapi_mvvm.util.Constant.areaDetail
import com.example.junemon.foodapi_mvvm.util.Constant.areaType
import com.example.junemon.foodapi_mvvm.util.Constant.categoryDetail
import com.example.junemon.foodapi_mvvm.util.Constant.categoryType
import com.example.junemon.foodapi_mvvm.util.Constant.goingToDetail
import com.example.junemon.foodapi_mvvm.util.Constant.ingredientDetail
import com.example.junemon.foodapi_mvvm.util.Constant.ingredientType

/**
 *
Created by Ian Damping on 06/05/2019.
Github = https://github.com/iandamping
 */

class DetailInformationPresenter(private val vm: AllFoodListDataViewModel) : BasePresenter<DetailInformationView>() {

    override fun onCreate() {
        view()?.initView()
        initData()
    }

    fun getData(intent: Intent) {
        val tmpCategoryData = intent.getStringExtra(categoryDetail)
        val tmpAreaData = intent.getStringExtra(areaDetail)
        val tmpIngredientData = intent.getStringExtra(ingredientDetail)
        when {
            !tmpCategoryData.isNullOrEmpty() -> vm.getCategoryData()
            !tmpAreaData.isNullOrEmpty() -> vm.getAreaData()
            !tmpIngredientData.isNullOrEmpty() -> vm.getIngredientData()
        }
    }


    private fun initData() {
        vm.liveDataState.observe(getLifeCycleOwner(), Observer {
            when (it) {
                is OnComplete -> {
                    setDialogShow(it.show)
                }
                is OnShowAreaFood -> {
                    view()?.getAreaData(it.data)
                }
                is OnShowCategoryFood -> {
                    view()?.getCategoryData(it.data)
                }
                is OnShowIngredientFood -> {
                    view()?.getIngredientData(it.data)
                }
            }
        })
    }
}