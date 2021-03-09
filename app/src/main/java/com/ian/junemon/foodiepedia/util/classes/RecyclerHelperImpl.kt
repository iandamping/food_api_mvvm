package com.ian.junemon.foodiepedia.util.classes

import android.view.View
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.ian.junemon.foodiepedia.base.adapter.BaseKotlinListAdapter
import com.ian.junemon.foodiepedia.util.interfaces.RecyclerHelper
import javax.inject.Inject

class RecyclerHelperImpl @Inject constructor() :
    RecyclerHelper {
    override fun <T> RecyclerView.setUpVerticalListAdapter(
        items: List<T>,
        diffUtil: DiffUtil.ItemCallback<T>,
        layoutResId: Int,
        bindHolder: View.(T) -> Unit,
        itemClick: T.() -> Unit,
        manager: RecyclerView.LayoutManager
    ): BaseKotlinListAdapter<T> {



        return BaseKotlinListAdapter(
            layout = layoutResId,
            bindHolder = { bindHolder(it) },
            diffUtil = diffUtil,
            itemClicks = { itemClick() }).apply {
            layoutManager = manager
            adapter = this
            submitList(items)
            notifyDataSetChanged()
        }
    }

    override fun <T> RecyclerView.setUpSkidAdapter(
        items: List<T>,
        diffUtil: DiffUtil.ItemCallback<T>,
        layoutResId: Int,
        bindHolder: View.(T) -> Unit,
        itemClick: T.() -> Unit,
        manager: RecyclerView.LayoutManager
    ): BaseKotlinListAdapter<T> {

        return BaseKotlinListAdapter(
            layout = layoutResId,
            bindHolder = { bindHolder(it) },
            diffUtil = diffUtil,
            itemClicks = { itemClick() }).apply {
            layoutManager = manager
            adapter = this
            submitList(items)
            notifyDataSetChanged()
        }
    }

    override fun <T> RecyclerView.setUpVerticalGridAdapter(
        items: List<T>,
        diffUtil: DiffUtil.ItemCallback<T>,
        layoutResId: Int,
        gridSize: Int,
        bindHolder: View.(T) -> Unit,
        itemClick: T.() -> Unit,
        manager: RecyclerView.LayoutManager
    ): BaseKotlinListAdapter<T> {



        return BaseKotlinListAdapter(
            layout = layoutResId,
            bindHolder = { bindHolder(it) },
            diffUtil = diffUtil,
            itemClicks = { itemClick() }).apply {
            layoutManager = manager
            adapter = this
            submitList(items)
            notifyDataSetChanged()
        }
    }

    override fun <T> RecyclerView.setUpHorizontalListAdapter(
        items: List<T>,
        diffUtil: DiffUtil.ItemCallback<T>,
        layoutResId: Int,
        bindHolder: View.(T) -> Unit,
        itemClick: T.() -> Unit,
        manager: RecyclerView.LayoutManager
    ): BaseKotlinListAdapter<T> {
        if (this.onFlingListener == null) {
            RecyclerHorizontalSnapHelper()
                .attachToRecyclerView(this)
        }

        return BaseKotlinListAdapter(
            layout = layoutResId,
            bindHolder = { bindHolder(it) },
            diffUtil = diffUtil,
            itemClicks = { itemClick() }).apply {
            layoutManager = manager
            adapter = this
            submitList(items)
            notifyDataSetChanged()
        }
    }
}