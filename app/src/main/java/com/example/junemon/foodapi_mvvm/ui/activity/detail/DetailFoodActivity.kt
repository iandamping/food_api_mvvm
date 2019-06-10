package com.example.junemon.foodapi_mvvm.ui.activity.detail

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.junemon.foodapi_mvvm.R
import com.example.junemon.foodapi_mvvm.data.local_data.LocalFoodData
import com.example.junemon.foodapi_mvvm.data.viewmodel.DetailFoodViewModel
import com.example.junemon.foodapi_mvvm.data.viewmodel.LocalDataViewModel
import com.example.junemon.foodapi_mvvm.model.DetailFood
import com.example.junemon.foodapi_mvvm.model.toDatabaseModel
import com.example.junemon.foodapi_mvvm.ui.activity.MainActivity
import com.example.junemon.foodapi_mvvm.util.Constant.intentDetailKey
import com.example.junemon.foodapi_mvvm.util.initPresenter
import com.google.android.material.appbar.AppBarLayout
import com.ian.app.helper.util.fullScreen
import com.ian.app.helper.util.fullScreenAnimation
import com.ian.app.helper.util.loadWithGlide
import com.ian.app.helper.util.startActivity
import com.ian.recyclerviewhelper.helper.setUpVertical
import kotlinx.android.synthetic.main.activity_detailed_food.*
import kotlinx.android.synthetic.main.item_ingredient_adapter.view.*
import kotlinx.android.synthetic.main.item_measurement_adapter.view.*
import org.koin.android.viewmodel.ext.android.viewModel

/**
 *
Created by Ian Damping on 06/05/2019.
Github = https://github.com/iandamping
 */

class DetailFoodActivity : AppCompatActivity(), DetailFoodView {
    private val localVm: LocalDataViewModel by viewModel()
    private val vm: DetailFoodViewModel by viewModel()
    private lateinit var presenter: DetailFoodPresenter
    private var foodDataToSave: DetailFood.Meal? = null
    private var idForDeleteItem: Int? = null
    private var isFavorite: Boolean = false
    private var foodLocalData: MutableList<LocalFoodData> = mutableListOf()
    private var menuItem: Menu? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fullScreenAnimation()
        setContentView(R.layout.activity_detailed_food)
        presenter = this.initPresenter { DetailFoodPresenter(vm, localVm) }.apply {
            this.attachView(this@DetailFoodActivity, this@DetailFoodActivity)
            this.onCreate()
            this.setData(intent?.getStringExtra(intentDetailKey))
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.detail_saving_data_menu, menu)
        menuItem = menu

        return true
    }

    override fun onSuccessGetLocalData(data: List<LocalFoodData>) {
        this.foodLocalData = data.toMutableList()
    }

    private fun initToolbar() {
        setSupportActionBar(toolbarDetailed)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            android.R.id.home -> {
                startActivity<MainActivity> {
                }
                true
            }
            R.id.add_to_favorite -> {
                if (isFavorite) {
                    presenter.deleteLocalID(idForDeleteItem)
                    isFavorite = false
                    setFavoriteState()
                } else {
                    presenter.saveLocalData(foodDataToSave?.toDatabaseModel())
                    isFavorite = true
                    setFavoriteState()
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun showDetailData(data: DetailFood.Meal) {
        this.foodLocalData.forEach {
            if (it.idMeal == data.idMeal) {
                idForDeleteItem = it.localID
                isFavorite = true
                setFavoriteState()
            }
        }
        this.foodDataToSave = data
        ivDetailedFood.loadWithGlide(data.strMealThumb, this@DetailFoodActivity)
        ivDetailedFood.setOnClickListener {
            fullScreen(data.strMealThumb)
        }
        tvDetailTittles.text = data.strMeal
        tvDetailedFoodCategory.text = "Food category : ${data.strCategory}"
        tvDetailedFoodArea.text = "Common food in ${data.strArea}"
        tvDetailedFoodInstruction.text = data.strInstructions

        appbarDetailLayout.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { appBarLayout, i ->
            var isShow = true
            var scrollRange: Int = -1

            if (scrollRange == -1) {
                scrollRange = appBarLayout.totalScrollRange
            }
            if (scrollRange + i == 0) {
                collapsingToolbar.title = data.strMeal
                tvDetailTittles.visibility = View.GONE
                isShow = true
            } else if (isShow) {
                collapsingToolbar.title = " "
                tvDetailTittles.visibility = View.VISIBLE
                isShow = false
            }
        })

    }

    override fun initView() {
        initToolbar()
    }

    override fun showIngredientData(dataIngredient: List<String>, dataMeasurement: List<String>) {
        rvDetailedIngredients.isNestedScrollingEnabled = false
        rvDetailedMeasurement.isNestedScrollingEnabled = false


        dataIngredient.let { data ->
            rvDetailedIngredients.setUpVertical(data, R.layout.item_ingredient_adapter, {
                with(this) {
                    tvIngredientAdapter.text = it
                }
            })
        }
        dataMeasurement.let { data ->
            rvDetailedMeasurement.setUpVertical(data, R.layout.item_measurement_adapter, {
                with(this) {
                    tvMeasurementAdapter.text = it
                }
            })
        }
    }

    private fun setFavoriteState() {
        if (isFavorite) {
            menuItem?.getItem(0)?.icon = ContextCompat.getDrawable(this, R.drawable.ic_bookmarked)
        } else {
            menuItem?.getItem(0)?.icon = ContextCompat.getDrawable(this, R.drawable.ic_unbookmark)
        }
    }

}