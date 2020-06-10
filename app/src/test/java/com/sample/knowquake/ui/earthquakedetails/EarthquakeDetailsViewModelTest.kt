package com.sample.knowquake.ui.earthquakedetails

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.sample.knowquake.provider.ResourceProvider
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import com.sample.knowquake.util.LiveDataTestUtil
import org.mockito.Mock


@RunWith(MockitoJUnitRunner::class)
class EarthquakeDetailsViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule() // Execute on main thread instantly

    private lateinit var earthquakeDetailsViewModel: EarthquakeDetailsViewModel

    @Mock
    lateinit var resourceProvider: ResourceProvider

    @Before
    fun setUp() { // This method is call before each test run.
        earthquakeDetailsViewModel = EarthquakeDetailsViewModel(resourceProvider)
    }

    @After
    fun tearDown() {
    }

    @Test
    fun setEarthquakeDetails() {
        earthquakeDetailsViewModel.setEarthquakeDetails("test", "test1", "test2", "test3", "test4")
        val title = LiveDataTestUtil.getValue(earthquakeDetailsViewModel.title)
        assertEquals("test", title)

        val timeDepth = LiveDataTestUtil.getValue(earthquakeDetailsViewModel.timeDepth)
        assertEquals("test1", timeDepth)

        val reviewStatus = LiveDataTestUtil.getValue(earthquakeDetailsViewModel.reviewStatus)
        assertEquals("test2", reviewStatus)

        val magnitude = LiveDataTestUtil.getValue(earthquakeDetailsViewModel.magnitude)
        assertEquals("test3", magnitude)

        val type = LiveDataTestUtil.getValue(earthquakeDetailsViewModel.type)
        assertEquals("test4", type)
    }

}