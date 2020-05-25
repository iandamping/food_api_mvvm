package com.ian.junemon.foodiepedia.core.presentation

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

/**
 * Created by Ian Damping on 04,December,2019
 * Github https://github.com/iandamping
 * Indonesia.
 */
object PresentationConstant {
    const val RequestSelectGalleryImage = 102
    const val RequestOpenCamera = 234
    const val filterKey = "filter key"
    const val filterValueBreakfast = "Breakfast"
    const val filterValueDinner = "Dinner"
    const val filterValueLunch = "Lunch"
    const val filterValueSupper = "Supper"
    const val filterValueBrunch = "Brunch"

   /* val placeRvCallback = object : DiffUtil.ItemCallback<PlaceCachePresentation?>() {
        override fun areItemsTheSame(oldItem: PlaceCachePresentation, newItem: PlaceCachePresentation): Boolean {
            return oldItem.localPlaceID == newItem.localPlaceID
        }

        override fun areContentsTheSame(oldItem: PlaceCachePresentation, newItem: PlaceCachePresentation): Boolean {
            return oldItem == newItem
        }
    }

    val placePaginationRvCallback = object : DiffUtil.ItemCallback<PlaceCachePresentation>() {
        override fun areItemsTheSame(oldItem: PlaceCachePresentation, newItem: PlaceCachePresentation): Boolean {
            return oldItem.localPlaceID == newItem.localPlaceID
        }

        override fun areContentsTheSame(oldItem: PlaceCachePresentation, newItem: PlaceCachePresentation): Boolean {
            return oldItem == newItem
        }
    }*/
}

inline val Context.layoutInflater: LayoutInflater
    get() = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

internal fun ViewGroup.inflates(layout: Int): View {
    return LayoutInflater.from(context).inflate(layout, this, false)
}