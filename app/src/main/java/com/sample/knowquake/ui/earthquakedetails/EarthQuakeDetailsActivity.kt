package com.sample.knowquake.ui.earthquakedetails

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.sample.knowquake.R
import com.sample.knowquake.databinding.ActivityEarthquakedeatilsBinding
import com.sample.knowquake.vo.DataStore
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject
import android.view.MenuItem
import com.sample.knowquake.util.TimeUtils
import com.sample.knowquake.util.provideViewModel
import java.util.*
import kotlin.math.roundToInt


class EarthQuakeDetailsActivity : DaggerAppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var dataStore: DataStore

    private val viewModel by lazy { provideViewModel<EarthquakeDetailsViewModel>(viewModelFactory) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityEarthquakedeatilsBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_earthquakedeatils)
        binding.apply {
            viewModel = this@EarthQuakeDetailsActivity.viewModel
            lifecycleOwner = this@EarthQuakeDetailsActivity
            setSupportActionBar(toolBar)
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(true)
        supportActionBar?.title = "EarthQuakeDetails"
        val features = dataStore.eqFeatures!!
        viewModel.setEarthquakeDetails(
            features.properties.title,
            String.format(
                "%s | %s km depth",
                TimeUtils.getUnixTimestampToDate(
                    features.properties.time,
                    TimeUtils.DATE_TIME_FORMAT_2,
                    true
                ),
                features.geometry.coordinates[features.geometry.coordinates.size - 1].toDouble()
                    .roundToInt()
            ),
            features.properties.status.toUpperCase(Locale.US),
            String.format(
                "%s %s",
                features.properties.mag.toDouble().roundToInt(),
                features.properties.magType
            ),
            features.properties.type
        )
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

}