package com.sample.knowquake.ui.earthquakes

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView

import com.sample.knowquake.R
import com.sample.knowquake.databinding.ItemEarthquakeBinding
import com.sample.knowquake.databinding.LoadMoreProgressBinding
import com.sample.knowquake.ui.OnItemClickListener
import com.sample.knowquake.util.TimeUtils
import com.sample.knowquake.vo.EqFeatures

internal class EarthQuakeAdapter(val itemClickListener: OnItemClickListener) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val VIEW_TYPE_ITEM = 0
        private const val VIEW_TYPE_LOADING = 1
    }

    private var featureProperties: ArrayList<*>

    init {
        this.featureProperties = ArrayList<Any>()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int
    ): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            VIEW_TYPE_ITEM -> createDiaViewHolder(inflater, parent)
            VIEW_TYPE_LOADING -> {
                val binding = DataBindingUtil.inflate<LoadMoreProgressBinding>(
                    inflater,
                    R.layout.load_more_progress,
                    parent,
                    false
                )
                LoadMoreViewHolder(binding)
            }
            else -> createDiaViewHolder(inflater, parent)
        }
    }

    private fun createDiaViewHolder(
        inflater: LayoutInflater,
        parent: ViewGroup
    ): FeaturePropertiesViewHolder {
        val binding: ItemEarthquakeBinding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.item_earthquake,
                parent,
                false
            )
        return FeaturePropertiesViewHolder(binding)
    }

    internal fun getFeatureProperties(): List<Any>? {
        return featureProperties
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder, position: Int
    ) {
        if (holder is FeaturePropertiesViewHolder) {
            holder.bind(featureProperties[position])
        } else if (holder is LoadMoreViewHolder) {
            holder.bindProgress()
        }
    }

    override fun getItemCount(): Int {
        return featureProperties.size
    }

    override fun getItemViewType(position: Int) = when (featureProperties[position]) {
        is EqFeatures -> VIEW_TYPE_ITEM
        is ProgressLoader -> VIEW_TYPE_LOADING
        else -> VIEW_TYPE_ITEM
    }

    fun clear() {
        featureProperties.clear()
    }

    fun setData(newFeatureProperties: ArrayList<Any>) {
        val diffResult = DiffUtil.calculateDiff(FeaturePropertiesDiff(newFeatureProperties, featureProperties))
        diffResult.dispatchUpdatesTo(this)
        this.featureProperties = newFeatureProperties
    }

    object ProgressLoader

    inner class FeaturePropertiesViewHolder(private val binding: ItemEarthquakeBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(featureProperties: Any) {
            when (featureProperties) {
                is EqFeatures -> {
                    binding.title.text = featureProperties.properties.title
                    binding.place.text = featureProperties.properties.place
                    binding.time.text = TimeUtils.getUnixTimestampToDate(
                        featureProperties.properties.time,
                        TimeUtils.DATE_TIME_FORMAT_2,
                        true
                    )
                }
            }
            itemView.setOnClickListener { itemClickListener.onItemClick(adapterPosition) }
        }
    }

    class FeaturePropertiesDiff(
        private val newList: List<Any>,
        private val oldList: List<Any>
    ) : DiffUtil.Callback() {

        override fun getOldListSize(): Int {
            return oldList.size
        }

        override fun getNewListSize(): Int {
            return newList.size
        }

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val oldItem = oldList[oldItemPosition]
            val newItem = newList[newItemPosition]
            return when {
                oldItem is EqFeatures && newItem is EqFeatures -> oldItem.id == newItem.id
                oldItem is ProgressLoader && newItem is ProgressLoader -> true
                else -> false
            }
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val oldItem = oldList[oldItemPosition]
            val newItem = newList[newItemPosition]
            return when {
                oldItem is EqFeatures && newItem is EqFeatures -> oldItem === newItem
                else -> true
            }
        }
    }
}