package com.sample.knowquake.ui.earthquakes

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.snackbar.Snackbar
import com.sample.knowquake.R
import com.sample.knowquake.databinding.ActivityEarthquakeBinding
import com.sample.knowquake.network.NoNetworkException
import javax.inject.Inject
import com.sample.knowquake.customs.EndlessRecyclerOnScrollListener
import com.sample.knowquake.result.EventObserver
import com.sample.knowquake.ui.OnItemClickListener
import com.sample.knowquake.ui.earthquakedetails.EarthQuakeDetailsActivity
import com.sample.knowquake.vo.DataStore
import com.sample.knowquake.vo.EqFeatures
import androidx.work.*
import com.sample.knowquake.worker.NewQuakeUpdateJob
import retrofit2.HttpException
import java.net.HttpURLConnection
import java.util.concurrent.TimeUnit
import android.app.AlertDialog
import android.net.Network
import com.aptus.droidils.utils.gone
import com.aptus.droidils.utils.visible
import com.sample.knowquake.base.BaseActivity
import com.sample.knowquake.util.provideViewModel

class EarthQuakeActivity : BaseActivity(), SwipeRefreshLayout.OnRefreshListener,
    OnItemClickListener {

    companion object {
        private const val LIMIT = 20
        private const val WORKER_TAG = "GET_NEW_EARTHQUAKE"
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var dataStore: DataStore

    @Inject
    lateinit var workerManager: WorkManager

    private val viewModel by lazy { provideViewModel<EarthQuakeViewModel>(viewModelFactory) }

    private lateinit var binding: ActivityEarthquakeBinding

    private lateinit var earthQuakeAdapter: EarthQuakeAdapter

    private var lastUpdatedList: ArrayList<Any>? = null

    private var currentList: ArrayList<Any>? = null

    private var endlessScroll: EndlessScroll? = null

    private var offset = 1

    private var isNetworkLost = false // Temp variable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_earthquake)
        binding.apply {
            viewModel = this.viewModel
            lifecycleOwner = this@EarthQuakeActivity
            setSupportActionBar(toolBar)
            eqRecyclerView.apply {
                earthQuakeAdapter = EarthQuakeAdapter(this@EarthQuakeActivity)
                setItemViewCacheSize(20)
                adapter = earthQuakeAdapter
                layoutManager = LinearLayoutManager(
                    this@EarthQuakeActivity,
                    RecyclerView.VERTICAL,
                    false
                ).apply { recycleChildrenOnDetach = true }
                //addItemDecoration(DividerItemDecoration(this@EarthQuakeActivity, RecyclerView.VERTICAL))
                itemAnimator = null
                endlessScroll = EndlessScroll()
                addOnScrollListener(endlessScroll!!)
            }
            swipeRefreshLayout.setOnRefreshListener(this@EarthQuakeActivity)
            retry.setOnClickListener { refreshOnNoNetworkConnection() } // Manual retry after getting network
        }
        lastUpdatedList = ArrayList()

        viewModel.apply {

            isProgressShown.observe(this@EarthQuakeActivity, EventObserver {
                if (it) {
                    binding.eqProgressBar.visible()
                } else {
                    binding.eqProgressBar.gone()
                }
            })

            earthQuakeError.observe(this@EarthQuakeActivity, Observer {
                binding.eqProgressBar.gone()
                binding.swipeRefreshLayout.isRefreshing = false
                when (it) {
                    is NoNetworkException -> {
                        binding.eqRecyclerView.gone()
                        binding.noNetworkConnection.visible()
                        isNetworkLost = true
                    }
                    is HttpException -> { // Handle Http exception here
                        when (it.response()?.code()) {
                            HttpURLConnection.HTTP_BAD_REQUEST -> {
                                // Show UI Here
                                Snackbar.make(
                                    binding.root,
                                    "Something went wrong.",
                                    Snackbar.LENGTH_LONG
                                )
                                    .show()
                            }
                            HttpURLConnection.HTTP_UNAUTHORIZED -> {
                                // Show UI Here
                            }
                            HttpURLConnection.HTTP_INTERNAL_ERROR -> {
                                // Show UI Here
                                Snackbar.make(
                                    binding.root,
                                    "Something went wrong.",
                                    Snackbar.LENGTH_LONG
                                )
                                    .show()
                            }

                            // More we can show here. We handle everything in one place will get update soon.
                        }
                    }
                }
            })

            earthQuake.observe(this@EarthQuakeActivity, Observer {
                binding.eqProgressBar.gone()
                val size = it.features.size
                if (size > 0) {
                    val newList = it.features
                    lastUpdatedList?.addAll(newList)
                    if (lastUpdatedList?.size!! > 0) {
                        lastUpdatedList?.remove(EarthQuakeAdapter.ProgressLoader)
                    }
                    lastUpdatedList?.add(EarthQuakeAdapter.ProgressLoader)
                    currentList = ArrayList(lastUpdatedList!!)
                    earthQuakeAdapter.setData(currentList!!)
                    binding.eqRecyclerView.visibility = View.VISIBLE
                } else {
                    removeProgressItem(currentList!!)
                }
                binding.swipeRefreshLayout.isRefreshing = false
            })
        }

        val constraints = Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED)
            .build()
        val orderSyncRequest =
            PeriodicWorkRequest.Builder(NewQuakeUpdateJob::class.java, 15, TimeUnit.MINUTES)
                .setInputData(Data.Builder().putString("macId", "Testing").build())
                .setConstraints(constraints)
                .addTag(WORKER_TAG)
                .build()
        workerManager.cancelAllWorkByTag(WORKER_TAG)
        workerManager.enqueue(orderSyncRequest)
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        viewModel.earthQuakeFeatures(LIMIT, offset, false)
    }

    override fun onAvailable(network: Network) {
        if (isNetworkLost) {
            runOnUiThread { refreshOnNoNetworkConnection() }
            isNetworkLost = false
        }// Auto refresh after getting network
    }

    override fun onUnavailable() {
    }

    override fun onLost(network: Network) {
    }

    override fun onBackPressed() {
        val builder = AlertDialog.Builder(this)
        builder.setCancelable(true)
        builder.setTitle("Confirm !")
        builder.setMessage("Are you sure to exit?").setCancelable(false)
        builder.setPositiveButton("Yes") { dialogInterface, _ ->
            dialogInterface.dismiss()
            this.finish()
        }
        builder.setNegativeButton(
            "No"
        ) { dialogInterface, _ -> dialogInterface.dismiss() }
        builder.create()
        builder.show()
    }

    override fun onRefresh() {
        refreshOnSwipeLayout()
    }

    private fun refresh(uiVisibility: () -> Unit) {
        lastUpdatedList?.clear()
        currentList.let { it?.clear() }
        currentList?.let { earthQuakeAdapter.setData(it) }
        offset = 1
        resetEndLessScroll()
        uiVisibility()
        viewModel.earthQuakeFeatures(LIMIT, offset, true)
    }

    private fun refreshOnNoNetworkConnection() {
        refresh {
            binding.noNetworkConnection.gone()
            binding.eqProgressBar.visible()
        }
    }

    private fun refreshOnSwipeLayout() {
        refresh { binding.eqRecyclerView.gone() }
    }

    override fun onItemClick(position: Int) {
        when (currentList!![position]) {
            is EqFeatures -> {
                dataStore.eqFeatures = currentList!![position] as EqFeatures
                startActivity(Intent(this, EarthQuakeDetailsActivity::class.java))
            }
        }
    }

    private fun removeProgressItem(lists: ArrayList<Any>) {
        if (lists.size > 0 && lists.contains(EarthQuakeAdapter.ProgressLoader)) {
            val position = lists.size
            lists.removeAt(position - 1)
            earthQuakeAdapter.notifyItemRemoved(position - 1)
        }
    }

    private fun resetEndLessScroll() {
        if (endlessScroll != null) {
            binding.eqRecyclerView.removeOnScrollListener(endlessScroll!!)
            endlessScroll = EndlessScroll()
            binding.eqRecyclerView.addOnScrollListener(endlessScroll!!)
        }
    }

    private inner class EndlessScroll : EndlessRecyclerOnScrollListener() {
        override fun onLoadMore() {
            offset += LIMIT
            viewModel.earthQuakeFeatures(LIMIT, offset, true)
        }
    }
}